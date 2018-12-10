package me.chanjar.weixin.open.api;

import me.chanjar.weixin.common.util.http.ApacheHttpClientBuilder;
import me.chanjar.weixin.open.bean.component.WxOpenAccessToken;
import me.chanjar.weixin.open.bean.component.WxOpenAuthorizationAccessToken;
import redis.clients.jedis.JedisPool;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.concurrent.locks.Lock;

/**
 * 微信客户端配置存储
 *
 * @author chanjarster
 */
public interface WxOpenConfigStorage {


    JedisPool getJedisPool();

    String getParentKeyPath();

    boolean autoRefreshToken();

    String getAppId();

    String getPartnerId();

    String getPartnerKey();

    String getAccessToken();

    Lock getAccessTokenLock();

    boolean isAccessTokenExpired();

    void updateAccessToken(WxOpenAuthorizationAccessToken accessToken);

    void updateAccessToken(String accessToken, int expiresInSeconds, String refreshToken);

    void expireAccessToken();

    String getRefreshToken();

    long getExpiresTime();

    String getComponentAccessToken();

    Lock getComponentAccessTokenLock();

    long getComponentAccessTokenExpiresTime();

    void expireComponentAccessToken();

    boolean isComponentAccessTokenExpired();

    void updateComponentAccessToken(WxOpenAccessToken accessToken);

    void updateComponentAccessToken(String componentAccessToken, int componentAccessTokenExpiresTime);

    String getComponentAppId();

    String getComponentAppSecret();

    String getComponentEncodingAesKey();

    String getComponentToken();

    String getComponentVerifyTicket();

    void updateComponentVerifyTicket(String componentVerifyTicket);

    String getJsapiTicket();

    Lock getJsapiTicketLock();

    long getJsapiTicketExpiresTime();

    boolean isJsapiTicketExpired();

    void updateJsapiTicket(String jsapiTicket, int expiresInSeconds);

    void expireJsapiTicket();

    String getCardApiTicket();

    Lock getCardApiTicketLock();

    boolean isCardApiTicketExpired();

    void updateCardApiTicket(String cardApiTicket, int expiresInSeconds);

    void expireCardApiTicket();

    String getOauth2redirectUri();

    File getTmpDirFile();

    String getHttpProxyHost();

    int getHttpProxyPort();

    String getHttpProxyUsername();

    String getHttpProxyPassword();

    SSLContext getSSLContext();

    ApacheHttpClientBuilder getApacheHttpClientBuilder();
}
