/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.chanjar.weixin.open.api;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import me.chanjar.weixin.common.util.http.ApacheHttpClientBuilder;
import me.chanjar.weixin.open.bean.component.WxOpenAccessToken;
import me.chanjar.weixin.open.bean.component.WxOpenAuthorizationAccessToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于Jedis的微信配置provider，在实际生产环境中应该将这些配置持久化
 *
 * @author chanjarster
 */
public class WxOpenJedisConfigStorage implements WxOpenConfigStorage {


    private static final String ACCESS_TOKEN_KEY = "WX_OPEN_ACCESS_TOKEN";
    private static final String ACCESS_REFRESH_TOKEN_KEY = "WX_OPEN_ACCESS_REFRESH_TOKEN";
    private static final String ACCESS_TOKEN_EXPIRES_TIME_KEY = "WX_OPEN_ACCESS_TOKEN_EXPIRES_TIME";
    private static final String COMPONENT_ACCESS_TOKEN_KEY = "WX_COMPONENT_ACCESS_TOKEN";
    private static final String COMPONENT_ACCESS_TOKEN_EXPIRES_TIME_KEY = "WX_COMPONENT_ACCESS_TOKEN_EXPIRES_TIME";
    private static final String COMPONENT_VERIFY_YICKIT = "WX_COMONENT_VERIFY_TICKET";
    private static final String JS_API_TICKET_KEY = "WX_OPEN_JS_API_TICKET";
    private static final String JS_API_TICKET_EXPIRES_TIME_KEY = "WX_OPEN_JS_API_TICKET_EXPIRES_TIME";
    private static final String CARD_API_TICKET_KEY = "WX_CARD_API_TICKET";
    private static final String CARD_API_TICKET_EXPIRES_TIME_KEY = "WX_CARD_API_TICKET_EXPIRES_TIME";

    //JEDIS 缓冲池
    private final JedisPool jedisPool;
    /**
     * 公众号AppId
     */
    protected volatile String appId;
    /**
     * 支付商户号
     */
    protected volatile String partnerId;
    /**
     * 支付秘钥
     */
    protected volatile String partnerKey;
    /**
     * 开放平台公众号接口授权令牌
     */
    protected volatile String accessToken;
    /**
     * 开放平台公众号接口授权令牌存活时间
     */
    protected volatile long expiresTime;
    /**
     * 开放平台接口授权刷新令牌
     */
    protected volatile String refreshToken;
    /**
     * JSSDK Ticket
     */
    protected volatile String jsapiTicket;
    /**
     * JSSDK Ticket 存活时间
     */
    protected volatile long jsapiTicketExpiresTime;
    /**
     * 卡券 Ticket
     */
    protected volatile String cardApiTicket;
    /**
     * 卡券 Ticket 存活时间
     */
    protected volatile long cardApiTicketExpiresTime;
    /**
     * 开方平台授权兑换凭证
     */
    protected volatile String componentVerifyTicket;
    /**
     * 开方平台 AppId
     */
    protected volatile String componentAppId;
    /**
     * 开放平台安全校验码
     */
    protected volatile String componentAppSecret;
    /**
     * 开放平台消息加密码
     */
    protected volatile String componentEncodingAesKey;
    /**
     * 开放平台令牌
     */
    protected volatile String componentToken;
    /**
     * 开放平台接口授权令牌
     */
    protected volatile String componentAccessToken;
    /**
     * 开放平台接口授权令牌存活时间
     */
    protected volatile long componentAccessTokenExpiresTime;
    /**
     * 开放平台接口授权令牌刷新锁
     */
    protected Lock componentAccessTokenLock = new ReentrantLock();
    /**
     * 开放平台公众号令牌刷新锁
     */
    protected Lock accessTokenLock = new ReentrantLock();
    /**
     * JSSDK Ticket 刷新锁
     */
    protected Lock jsapiTicketLock = new ReentrantLock();
    /**
     * 卡券 Ticket 刷新锁
     */
    protected Lock cardApiTicketLock = new ReentrantLock();
    /**
     * 用户授权回调地址
     */
    protected volatile String oauth2redirectUri;
    /**
     * SSL
     */
    protected volatile SSLContext sslContext;
    /**
     * 临时文件目录
     */
    protected volatile File tmpDirFile;
    /**
     * HTTP 连接
     */
    protected volatile ApacheHttpClientBuilder apacheHttpClientBuilder;
    /**
     * 代理服务器地址
     */
    protected volatile String httpProxyHost;
    /**
     * 代理服务器端口
     */
    protected volatile int httpProxyPort;
    /**
     * 代理服务器账号
     */
    protected volatile String httpProxyUsername;
    /**
     * 代理服务器密码
     */
    protected volatile String httpProxyPassword;

    protected volatile String parentKeyPath;

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_TOTAL = 1024;
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    private static int TIMEOUT = 10000;

    public WxOpenJedisConfigStorage(String host, int port, String password) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_TOTAL);
        config.setMaxIdle(MAX_IDLE);
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(TEST_ON_BORROW);
        if (StringUtils.isBlank(password)){
            this.jedisPool = new JedisPool(config, host, port, TIMEOUT);
        }else {
            this.jedisPool = new JedisPool(config, host, port, TIMEOUT, password);
        }
    }

    public void destroy() {
        this.jedisPool.destroy();
    }

    @Override
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    @Override
    public String getParentKeyPath() {
        return parentKeyPath;
    }

    public WxOpenJedisConfigStorage setParentKeyPath(String parentKeyPath) {
        this.parentKeyPath = parentKeyPath;
        return this;
    }

    @Override
    public boolean autoRefreshToken() {
        return true;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    public WxOpenJedisConfigStorage setAppId(String appId) {
        this.appId = appId == null ? "" : appId;
        return this;
    }

    @Override
    public String getPartnerId() {
        return partnerId;
    }

    public WxOpenJedisConfigStorage setPartnerId(String partnerId) {
        this.partnerId = partnerId == null ? "" : partnerId;
        return this;
    }

    @Override
    public String getPartnerKey() {
        return partnerKey;
    }

    public WxOpenJedisConfigStorage setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey == null ? "" : partnerKey;
        return this;
    }

    @Override
    public String getAccessToken() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(ACCESS_TOKEN_KEY);
        }
    }

    @Override
    public Lock getAccessTokenLock() {
        return this.accessTokenLock;
    }

    @Override
    public boolean isAccessTokenExpired() {
        return getBoolean(ACCESS_TOKEN_EXPIRES_TIME_KEY);
    }

    @Override
    public synchronized void updateAccessToken(WxOpenAuthorizationAccessToken accessToken) {
        updateAccessToken(accessToken.getAuthorizerAccessToken(), accessToken.getExpiresIn(), accessToken.getAuthorizerRefreshToken());
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds, String refreshToken) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(ACCESS_TOKEN_KEY, accessToken);
            jedis.set(ACCESS_TOKEN_EXPIRES_TIME_KEY, (System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L) + "");
            jedis.set(ACCESS_REFRESH_TOKEN_KEY, refreshToken);
        }
    }

    @Override
    public void expireAccessToken() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(ACCESS_TOKEN_EXPIRES_TIME_KEY, "0");
        }
    }

    @Override
    public long getExpiresTime() {
        return getLong(ACCESS_TOKEN_EXPIRES_TIME_KEY);
    }


    @Override
    public String getRefreshToken() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(ACCESS_REFRESH_TOKEN_KEY);
        }
    }

    @Override
    public String getComponentAccessToken() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(COMPONENT_ACCESS_TOKEN_KEY);
        }
    }

    @Override
    public Lock getComponentAccessTokenLock() {
        return componentAccessTokenLock;
    }

    @Override
    public synchronized void updateComponentAccessToken(WxOpenAccessToken accessToken) {
        updateComponentAccessToken(accessToken.getComponentAccessToken(), accessToken.getExpiresIn());
    }

    @Override
    public synchronized void updateComponentAccessToken(String componentAccessToken, int componentAccessTokenExpiresTime) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(COMPONENT_ACCESS_TOKEN_KEY, componentAccessToken);
            jedis.set(COMPONENT_ACCESS_TOKEN_EXPIRES_TIME_KEY, (System.currentTimeMillis() + (componentAccessTokenExpiresTime - 200) * 1000L) + "");
        }
    }

    @Override
    public void expireComponentAccessToken() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(COMPONENT_ACCESS_TOKEN_EXPIRES_TIME_KEY, "0");
        }
    }

    @Override
    public boolean isComponentAccessTokenExpired() {
        return getBoolean(COMPONENT_ACCESS_TOKEN_EXPIRES_TIME_KEY);
    }

    @Override
    public long getComponentAccessTokenExpiresTime() {
        return getLong(COMPONENT_ACCESS_TOKEN_EXPIRES_TIME_KEY);
    }

    @Override
    public String getComponentAppId() {
        return componentAppId;
    }

    public WxOpenJedisConfigStorage setComponentAppId(String componentAppId) {
        this.componentAppId = componentAppId == null ? "" : componentAppId;
        return this;
    }

    @Override
    public String getComponentAppSecret() {
        return componentAppSecret;
    }

    public WxOpenJedisConfigStorage setComponentAppSecret(String componentAppSecret) {
        this.componentAppSecret = componentAppSecret == null ? "" : componentAppSecret;
        return this;
    }

    @Override
    public String getComponentEncodingAesKey() {
        return componentEncodingAesKey;
    }

    public WxOpenJedisConfigStorage setComponentEncodingAesKey(String componentEncodingAesKey) {
        this.componentEncodingAesKey = componentEncodingAesKey == null ? "" : componentEncodingAesKey;
        return this;
    }

    @Override
    public String getComponentToken() {
        return componentToken;
    }

    public WxOpenJedisConfigStorage setComponentToken(String componentToken) {
        this.componentToken = componentToken == null ? "" : componentToken;
        return this;
    }

    @Override
    public String getComponentVerifyTicket() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(COMPONENT_VERIFY_YICKIT);
        }
    }

    @Override
    public synchronized void updateComponentVerifyTicket(String componentVerifyTicket) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(COMPONENT_VERIFY_YICKIT, componentVerifyTicket);
        }
    }

    @Override
    public String getJsapiTicket() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(JS_API_TICKET_KEY);
        }
    }

    @Override
    public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(JS_API_TICKET_KEY, jsapiTicket);
            jedis.set(JS_API_TICKET_EXPIRES_TIME_KEY, (System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L + ""));
        }
    }

    @Override
    public Lock getJsapiTicketLock() {
        return this.jsapiTicketLock;
    }

    @Override
    public long getJsapiTicketExpiresTime() {
        return getLong(JS_API_TICKET_EXPIRES_TIME_KEY);
    }

    @Override
    public boolean isJsapiTicketExpired() {
        return getBoolean(JS_API_TICKET_EXPIRES_TIME_KEY);
    }

    @Override
    public void expireJsapiTicket() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(JS_API_TICKET_EXPIRES_TIME_KEY, "0");
        }
    }

    @Override
    public String getCardApiTicket() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(CARD_API_TICKET_KEY);
        }
    }

    @Override
    public Lock getCardApiTicketLock() {
        return this.cardApiTicketLock;
    }

    @Override
    public boolean isCardApiTicketExpired() {
        return getBoolean(CARD_API_TICKET_EXPIRES_TIME_KEY);
    }

    @Override
    public synchronized void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(CARD_API_TICKET_KEY, cardApiTicket);
            jedis.set(CARD_API_TICKET_EXPIRES_TIME_KEY, (System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L + ""));
        }
    }

    @Override
    public void expireCardApiTicket() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(CARD_API_TICKET_EXPIRES_TIME_KEY, "0");
        }
    }

    @Override
    public String getOauth2redirectUri() {
        return this.oauth2redirectUri;
    }

    public void setOauth2redirectUri(String oauth2redirectUri) {
        this.oauth2redirectUri = oauth2redirectUri;
    }

    @Override
    public File getTmpDirFile() {
        return this.tmpDirFile;
    }

    public void setTmpDirFile(File tmpDirFile) {
        this.tmpDirFile = tmpDirFile;
    }

    @Override
    public String getHttpProxyHost() {
        return this.httpProxyHost;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    @Override
    public int getHttpProxyPort() {
        return this.httpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    @Override
    public String getHttpProxyUsername() {
        return this.httpProxyUsername;
    }

    public void setHttpProxyUsername(String httpProxyUsername) {
        this.httpProxyUsername = httpProxyUsername;
    }

    @Override
    public String getHttpProxyPassword() {
        return this.httpProxyPassword;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    @Override
    public SSLContext getSSLContext() {
        return this.sslContext;
    }

    public void setSSLContext(SSLContext context) {
        this.sslContext = context;
    }

    @Override
    public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
        return this.apacheHttpClientBuilder;
    }

    public void setApacheHttpClientBuilder(ApacheHttpClientBuilder apacheHttpClientBuilder) {
        this.apacheHttpClientBuilder = apacheHttpClientBuilder;
    }

    private boolean getBoolean(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            String expiresTimeStr = jedis.get(key);
            if (expiresTimeStr != null) {
                Long expiresTime = Long.parseLong(expiresTimeStr);
                return System.currentTimeMillis() > expiresTime;
            }
            return true;
        }
    }

    private long getLong(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            String expiresTimeStr = jedis.get(key);
            if (expiresTimeStr != null) {
                return Long.parseLong(expiresTimeStr);
            }
            return 0L;
        }
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
