package me.chanjar.weixin.open.api;

import me.chanjar.weixin.common.util.http.ApacheHttpClientBuilder;
import me.chanjar.weixin.open.bean.component.WxOpenAccessToken;
import me.chanjar.weixin.open.bean.component.WxOpenAuthorizationAccessToken;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于内存的微信配置provider，在实际生产环境中应该将这些配置持久化
 *
 * @author chanjarster
 */
public class WxOpenInMemoryConfigStorage implements WxOpenConfigStorage {


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

    //////////////////////////////////////////////////////////
    //////////////              AppId           //////////////
    //////////////////////////////////////////////////////////
    /**
     * 临时文件目录
     */
    protected volatile File tmpDirFile;
    /**
     * SSL
     */
    protected volatile SSLContext sslContext;

    //////////////////////////////////////////////////////////
    //////////////            支付相关          //////////////
    //////////////////////////////////////////////////////////
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


    //////////////////////////////////////////////////////////
    //////////////  开放平台公众号接口授权令牌  //////////////
    //////////////////////////////////////////////////////////

    @Override
    public JedisPool getJedisPool() {
        return null;
    }

    @Override
    public String getParentKeyPath() {
        return parentKeyPath;
    }

    public WxOpenInMemoryConfigStorage setParentKeyPath(String parentKeyPath) {
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

    public WxOpenInMemoryConfigStorage setAppId(String appId) {
        this.appId = appId == null ? "" : appId;
        return this;
    }

    @Override
    public String getPartnerId() {
        return partnerId;
    }

    public WxOpenInMemoryConfigStorage setPartnerId(String partnerId) {
        this.partnerId = partnerId == null ? "" : partnerId;
        return this;
    }

    @Override
    public String getPartnerKey() {
        return partnerKey;
    }

    public WxOpenInMemoryConfigStorage setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey == null ? "" : partnerKey;
        return this;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    public WxOpenInMemoryConfigStorage setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? "" : accessToken;
        return this;
    }

    @Override
    public Lock getAccessTokenLock() {
        return this.accessTokenLock;
    }

    //////////////////////////////////////////////////////////
    //////////////     开放平台接口令牌相关     //////////////
    //////////////////////////////////////////////////////////

    @Override
    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > this.expiresTime;
    }

    @Override
    public synchronized void updateAccessToken(WxOpenAuthorizationAccessToken accessToken) {
        updateAccessToken(accessToken.getAuthorizerAccessToken(), accessToken.getExpiresIn(), accessToken.getAuthorizerRefreshToken());
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds, String refreshToken) {
        this.accessToken = accessToken;
        this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
        this.refreshToken = refreshToken;
    }

    @Override
    public void expireAccessToken() {
        this.expiresTime = 0;
    }

    @Override
    public long getExpiresTime() {
        return this.expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    public WxOpenInMemoryConfigStorage setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? "" : refreshToken;
        return this;
    }

    @Override
    public String getComponentAccessToken() {
        return componentAccessToken;
    }

    //////////////////////////////////////////////////////////
    //////////////         开放平台相关         //////////////
    //////////////////////////////////////////////////////////

    public WxOpenInMemoryConfigStorage setComponentAccessToken(String componentAccessToken) {
        this.componentAccessToken = componentAccessToken == null ? "" : componentAccessToken;
        return this;
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
        this.componentAccessToken = componentAccessToken;
        this.componentAccessTokenExpiresTime = System.currentTimeMillis() + (componentAccessTokenExpiresTime - 200) * 1000L;
    }

    @Override
    public void expireComponentAccessToken() {
        this.componentAccessTokenExpiresTime = 0;
    }

    @Override
    public boolean isComponentAccessTokenExpired() {
        return System.currentTimeMillis() > this.componentAccessTokenExpiresTime;
    }

    @Override
    public long getComponentAccessTokenExpiresTime() {
        return componentAccessTokenExpiresTime;
    }

    public WxOpenInMemoryConfigStorage setComponentAccessTokenExpiresTime(long componentAccessTokenExpiresTime) {
        this.componentAccessTokenExpiresTime = componentAccessTokenExpiresTime;
        return this;
    }

    @Override
    public String getComponentAppId() {
        return componentAppId;
    }

    public WxOpenInMemoryConfigStorage setComponentAppId(String componentAppId) {
        this.componentAppId = componentAppId == null ? "" : componentAppId;
        return this;
    }

    @Override
    public String getComponentAppSecret() {
        return componentAppSecret;
    }

    //////////////////////////////////////////////////////////
    //////////////             JSSDK            //////////////
    //////////////////////////////////////////////////////////

    public WxOpenInMemoryConfigStorage setComponentAppSecret(String componentAppSecret) {
        this.componentAppSecret = componentAppSecret == null ? "" : componentAppSecret;
        return this;
    }

    @Override
    public String getComponentEncodingAesKey() {
        return componentEncodingAesKey;
    }

    public WxOpenInMemoryConfigStorage setComponentEncodingAesKey(String componentEncodingAesKey) {
        this.componentEncodingAesKey = componentEncodingAesKey == null ? "" : componentEncodingAesKey;
        return this;
    }

    @Override
    public String getComponentToken() {
        return componentToken;
    }

    public WxOpenInMemoryConfigStorage setComponentToken(String componentToken) {
        this.componentToken = componentToken == null ? "" : componentToken;
        return this;
    }

    @Override
    public String getComponentVerifyTicket() {
        return componentVerifyTicket;
    }

    public WxOpenInMemoryConfigStorage setComponentVerifyTicket(String componentVerifyTicket) {
        this.componentVerifyTicket = componentVerifyTicket == null ? "" : componentVerifyTicket;
        return this;
    }

    @Override
    public synchronized void updateComponentVerifyTicket(String componentVerifyTicket) {
        this.componentVerifyTicket = componentVerifyTicket;
    }

    //////////////////////////////////////////////////////////
    //////////////             卡券             //////////////
    //////////////////////////////////////////////////////////

    @Override
    public String getJsapiTicket() {
        return this.jsapiTicket;
    }

    public WxOpenInMemoryConfigStorage setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket == null ? "" : jsapiTicket;
        return this;
    }

    @Override
    public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        this.jsapiTicket = jsapiTicket;
        // 预留200秒的时间
        this.jsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
    }

    @Override
    public Lock getJsapiTicketLock() {
        return this.jsapiTicketLock;
    }

    @Override
    public long getJsapiTicketExpiresTime() {
        return this.jsapiTicketExpiresTime;
    }


    //////////////////////////////////////////////////////////
    //////////////             其他             //////////////
    //////////////////////////////////////////////////////////

    public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
        this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
    }

    @Override
    public boolean isJsapiTicketExpired() {
        return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
    }

    @Override
    public void expireJsapiTicket() {
        this.jsapiTicketExpiresTime = 0;
    }

    @Override
    public String getCardApiTicket() {
        return this.cardApiTicket;
    }

    @Override
    public Lock getCardApiTicketLock() {
        return this.cardApiTicketLock;
    }

    @Override
    public boolean isCardApiTicketExpired() {
        return System.currentTimeMillis() > this.cardApiTicketExpiresTime;
    }

    @Override
    public synchronized void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
        this.cardApiTicket = cardApiTicket;
        // 预留200秒的时间
        this.cardApiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
    }

    @Override
    public void expireCardApiTicket() {
        this.cardApiTicketExpiresTime = 0;
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


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
