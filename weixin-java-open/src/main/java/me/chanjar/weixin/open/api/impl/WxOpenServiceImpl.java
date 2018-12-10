package me.chanjar.weixin.open.api.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.StandardSessionManager;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.RandomUtils;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.common.util.http.*;
import me.chanjar.weixin.open.api.*;
import me.chanjar.weixin.open.bean.component.*;
import me.chanjar.weixin.open.bean.result.WxOpenUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.locks.Lock;

//TODO 待添加摇一摇 //微信小店 微信WIFI相关接口 //小程序
public class WxOpenServiceImpl implements WxOpenService {

    private static final JsonParser JSON_PARSER = new JsonParser();

    private static final String API_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/component";

    private static final String OAUTH2_API_URL_PREFIX = "https://api.weixin.qq.com/sns/oauth2/component";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected WxSessionManager sessionManager = new StandardSessionManager();

    private WxOpenConfigStorage configStorage;

    private WxOpenKefuService kefuService = new WxOpenKefuServiceImpl(this);
    private WxOpenMaterialService materialService = new WxOpenMaterialServiceImpl(this);
    private WxOpenMenuService menuService = new WxOpenMenuServiceImpl(this);
    private WxOpenUserService userService = new WxOpenUserServiceImpl(this);
    private WxOpenUserTagService userTagService = new WxOpenUserTagServiceImpl(this);
    private WxOpenQrcodeService qrCodeService = new WxOpenQrcodeServiceImpl(this);
    private WxOpenCardService cardService = new WxOpenCardServiceImpl(this);
    private WxOpenPayService payService = new WxOpenPayServiceImpl(this);
    private WxOpenStoreService storeService = new WxOpenStoreServiceImpl(this);
    private WxOpenDataCubeService dataCubeService = new WxOpenDataCubeServiceImpl(this);
    private WxOpenUserBlacklistService blackListService = new WxOpenUserBlacklistServiceImpl(this);
    private WxOpenTemplateMsgService templateMsgService = new WxOpenTemplateMsgServiceImpl(this);

    //
    private WxOpenMassService massService = new WxOpenMassServiceImpl(this);
    private WxOpenIntelligentService intelligentService = new WxOpenIntelligentServiceImpl(this);
    private WxOpenShopService shopService = new WxOpenShopServiceImpl(this);


    private CloseableHttpClient httpClient;
    private HttpHost httpProxy;
    private int retrySleepMillis = 1000;
    private int maxRetryTimes = 5;

    @Override
    public boolean checkSignature(String timestamp, String nonce, String signature) {
        String componentToken = this.configStorage.getComponentToken();
        return !StringUtils.isAnyBlank(componentToken, timestamp, nonce, signature) && SHA1.gen(componentToken, timestamp, nonce).equals(signature);
    }

    @Override
    public String getAccessToken() throws WxErrorException {
        return this.getAccessToken(false);
    }

    @Override
    public String getAccessToken(boolean forceRefresh) throws WxErrorException {
        Lock lock = this.configStorage.getAccessTokenLock();
        try {
            lock.lock();
            if (forceRefresh) {
                this.configStorage.expireAccessToken();
            }
            if (this.configStorage.isAccessTokenExpired()) {
                String url = API_URL_PREFIX + "/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";
                JsonObject json = new JsonObject();
                json.addProperty("component_appid", configStorage.getComponentAppId());
                json.addProperty("authorizer_appid", configStorage.getAppId());
                json.addProperty("authorizer_refresh_token", configStorage.getRefreshToken());
                String resultContent = post(url, json.toString());
                WxOpenAuthorizationAccessToken accessToken = WxOpenAuthorizationAccessToken.fromJson(resultContent);
                this.configStorage.updateAccessToken(accessToken.getAuthorizerAccessToken(), accessToken.getExpiresIn(), accessToken.getAuthorizerRefreshToken());
            }
        } finally {
            lock.unlock();
        }
        return this.configStorage.getAccessToken();
    }

    @Override
    public String getComponentAccessToken() throws WxErrorException {
        return this.getComponentAccessToken(false);
    }

    @Override
    public String getComponentAccessToken(boolean forceRefresh) throws WxErrorException {
        Lock lock = this.configStorage.getComponentAccessTokenLock();
        try {
            lock.lock();
            if (forceRefresh) {
                this.configStorage.expireComponentAccessToken();
            }
            if (this.configStorage.isComponentAccessTokenExpired()) {
                String url = API_URL_PREFIX + "/api_component_token";
                JsonObject json = new JsonObject();
                json.addProperty("component_appid", this.configStorage.getComponentAppId());
                json.addProperty("component_appsecret", this.configStorage.getComponentAppSecret());
                json.addProperty("component_verify_ticket", this.configStorage.getComponentVerifyTicket());
                String responseContent = this.post(url, json.toString());
                WxOpenAccessToken componentAccessToken = WxOpenAccessToken.fromJson(responseContent);
                this.configStorage.updateComponentAccessToken(componentAccessToken.getComponentAccessToken(), componentAccessToken.getExpiresIn());
            }
        } finally {
            lock.unlock();
        }
        return this.configStorage.getComponentAccessToken();
    }

    @Override
    public String getJsapiTicket() throws WxErrorException {
        return this.getJsapiTicket(false);
    }

    @Override
    public String getJsapiTicket(boolean forceRefresh) throws WxErrorException {
        Lock lock = this.configStorage.getJsapiTicketLock();
        try {
            lock.lock();
            if (forceRefresh) {
                this.configStorage.expireJsapiTicket();
            }
            if (this.configStorage.isJsapiTicketExpired()) {
                String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
                String responseContent = execute(new SimpleGetRequestExecutor(), url, null);
                JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
                JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
                String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
                int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
                this.configStorage.updateJsapiTicket(jsapiTicket, expiresInSeconds);
            }
        } finally {
            lock.unlock();
        }
        return this.configStorage.getJsapiTicket();
    }

    @Override
    public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
        long timestamp = System.currentTimeMillis() / 1000;
        String noncestr = RandomUtils.getRandomStr();
        String jsapiTicket = getJsapiTicket(false);
        String signature = SHA1.genWithAmple("jsapi_ticket=" + jsapiTicket,
                "noncestr=" + noncestr, "timestamp=" + timestamp, "url=" + url);
        WxJsapiSignature jsapiSignature = new WxJsapiSignature();
        jsapiSignature.setAppid(this.configStorage.getAppId());
        jsapiSignature.setTimestamp(timestamp);
        jsapiSignature.setNoncestr(noncestr);
        jsapiSignature.setUrl(url);
        jsapiSignature.setSignature(signature);
        return jsapiSignature;
    }


    @Override
    public String buildQrConnectUrl(String redirectURI, String scope,
                                    String state) {
        StringBuilder url = new StringBuilder();
        url.append("https://open.weixin.qq.com/connect/qrconnect?");
        url.append("appid=").append(this.configStorage.getAppId());
        url.append("&redirect_uri=").append(URIUtil.encodeURIComponent(redirectURI));
        url.append("&response_type=code");
        url.append("&scope=").append(scope);
        if (state != null) {
            url.append("&state=").append(state);
        }

        url.append("#wechat_redirect");
        return url.toString();
    }

    @Override
    public WxOpenUser oauth2getUserInfo(WxOpenOAuth2AccessToken oAuth2AccessToken, String lang) throws WxErrorException {
        StringBuilder url = new StringBuilder();
        url.append("https://api.weixin.qq.com/sns/userinfo?");
        url.append("access_token=").append(oAuth2AccessToken.getAccessToken());
        url.append("&openid=").append(oAuth2AccessToken.getOpenId());
        if (lang == null) {
            url.append("&lang=zh_CN");
        } else {
            url.append("&lang=").append(lang);
        }

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            String responseText = executor.execute(getHttpclient(), this.httpProxy, url.toString(), null);
            return WxOpenUser.fromJson(responseText);
        } catch (IOException e) {
            throw new WxErrorException(new WxError().setErrorCode(100001).setErrorMsg(MessageFormat.format("连接建立失败,{0}", e.getMessage())));
        } catch (WxErrorException e) {
            throw new WxErrorException(e.getError());
        }
    }

    @Override
    public boolean oauth2validateAccessToken(WxOpenOAuth2AccessToken oAuth2AccessToken) throws WxErrorException {
        StringBuilder url = new StringBuilder();
        url.append("https://api.weixin.qq.com/sns/auth?");
        url.append("access_token=").append(oAuth2AccessToken.getAccessToken());
        url.append("&openid=").append(oAuth2AccessToken.getOpenId());

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            executor.execute(getHttpclient(), this.httpProxy, url.toString(), null);
        } catch (IOException e) {
            throw new WxErrorException(new WxError().setErrorCode(100001).setErrorMsg(MessageFormat.format("连接建立失败,{0}", e.getMessage())));
        } catch (WxErrorException e) {
            this.logger.warn("当前用户AccessToken已失效");
            return false;
        }
        return true;
    }

    @Override
    public String[] getCallbackIP() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
        String responseContent = get(url, null);
        JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
        JsonArray ipList = tmpJsonElement.getAsJsonObject().get("ip_list").getAsJsonArray();
        String[] ipArray = new String[ipList.size()];
        for (int i = 0; i < ipList.size(); i++) {
            ipArray[i] = ipList.get(i).getAsString();
        }
        return ipArray;
    }

    @Override
    public String createComponentLoginPageUrl(String pre_auth_code, String redirect_uri) {
        return "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="
                + this.configStorage.getComponentAppId()
                + "&pre_auth_code=" + pre_auth_code + "&redirect_uri=" + redirect_uri;
    }

    @Override
    public WxOpenPreAuthCode getPreAuthCode() throws WxErrorException {
        String url = API_URL_PREFIX + "/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
        JsonObject json = new JsonObject();
        json.addProperty("component_appid", this.configStorage.getComponentAppId());
        String responseContent = this.post(url, json.toString());
        return WxOpenPreAuthCode.fromJson(responseContent);
    }

    @Override
    public WxOpenAuthorization getAuthorization(String authorization_code) throws WxErrorException {
        String url = API_URL_PREFIX + "/api_query_auth?component_access_token=COMPONENT_ACCESS_TOKEN";
        JsonObject json = new JsonObject();
        json.addProperty("component_appid", this.configStorage.getComponentAppId());
        json.addProperty("authorization_code", authorization_code);
        String responseContent = this.post(url, json.toString());
        return WxOpenAuthorization.fromJson(responseContent);
    }

    @Override
    public WxOpenAuthorizerInfo getAuthorizerInfo(String authorizer_appid) throws WxErrorException {
        String url = API_URL_PREFIX + "/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";
        JsonObject json = new JsonObject();
        json.addProperty("component_appid", this.configStorage.getComponentAppId());
        json.addProperty("authorizer_appid", authorizer_appid);
        String responseContent = this.post(url, json.toString());
        return WxOpenAuthorizerInfo.fromJson(responseContent);
    }

    @Override
    public WxOpenAuthorizerOption getAuthorizerOption(String authorizer_appid, String option_name, String option_value) throws WxErrorException {
        String url = API_URL_PREFIX + "/api_set_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";
        JsonObject json = new JsonObject();
        json.addProperty("component_appid", this.configStorage.getComponentAppId());
        json.addProperty("authorizer_appid", authorizer_appid);
        json.addProperty("option_name", option_name);
        json.addProperty("option_value", option_value);
        String responseContent = this.post(url, json.toString());
        return WxOpenAuthorizerOption.fromJson(responseContent);
    }

    @Override
    public WxOpenSetAuthorizerOption setAuthorizerOption(String authorizer_appid, String option_name) throws WxErrorException {
        String url = API_URL_PREFIX + "/api_get_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";
        JsonObject json = new JsonObject();
        json.addProperty("component_appid", this.configStorage.getComponentAppId());
        json.addProperty("authorizer_appid", authorizer_appid);
        json.addProperty("option_name", option_name);
        String responseContent = this.post(url, json.toString());
        return WxOpenSetAuthorizerOption.fromJson(responseContent);
    }

    @Override
    public String oauth2buildAuthorizationUrl(String redirectURI, String scope, String state) {
        StringBuilder url = new StringBuilder();
        url.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
        url.append("appid=").append(this.configStorage.getAppId());
        url.append("&redirect_uri=").append(URIUtil.encodeURIComponent(redirectURI));
        url.append("&response_type=code");
        url.append("&scope=").append(scope);
        if (state != null) {
            url.append("&state=").append(state);
        }
        url.append("&component_appid=").append(this.configStorage.getComponentAppId());
        url.append("#wechat_redirect");
        return url.toString();
    }

    @Override
    public WxOpenOAuth2AccessToken getOAuth2AccessToken(String code) throws WxErrorException {
        String url = OAUTH2_API_URL_PREFIX + "/access_token?appid="
                + this.configStorage.getAppId()
                + "&code=" + code + "&grant_type=authorization_code&component_appid="
                + this.configStorage.getComponentAppId()
                + "&component_access_token=COMPONENT_ACCESS_TOKEN";
        String responseContent = this.get(url, null);
        return WxOpenOAuth2AccessToken.fromJson(responseContent);
    }

    @Override
    public WxOpenOAuth2AccessToken refreshOauth2AccessToken(String refresh_token) throws WxErrorException {
        String url = OAUTH2_API_URL_PREFIX + "/refresh_token?appid="
                + this.configStorage.getAppId()
                + "&grant_type=refresh_token&component_appid="
                + this.configStorage.getComponentAppId()
                + "&component_access_token=COMPONENT_ACCESS_TOKEN&refresh_token=" + refresh_token;
        String responseContent = this.get(url, null);
        return WxOpenOAuth2AccessToken.fromJson(responseContent);
    }

    //TODO 待实现获取微信公众号自动回复规则接口
    //https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token=ACCESS_TOKEN

    @Override
    public String get(String url, String queryParam) throws WxErrorException {
        return execute(new SimpleGetRequestExecutor(), url, queryParam);
    }

    @Override
    public String post(String url, String postData) throws WxErrorException {
        return execute(new SimplePostRequestExecutor(), url, postData);
    }

    /**
     * 向微信端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
     */
    @Override
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
        int retryTimes = 0;
        do {
            try {
                T result = executeInternal(executor, uri, data);
                this.logger.debug("\n[URL]:  {}\n[PARAMS]: {}\n[RESPONSE]: {}", uri, data, result);
                return result;
            } catch (WxErrorException e) {
                if (retryTimes + 1 > this.maxRetryTimes) {
                    this.logger.warn("重试达到最大次数【{}】", this.maxRetryTimes);
                    throw new WxErrorException(new WxError().setErrorCode(100002).setErrorMsg(MessageFormat.format("重试达到最大次数【{0}】,微信服务端异常，超出重试次数", this.maxRetryTimes)));
                }
                WxError error = e.getError();
                // -1 系统繁忙, 1000ms后重试
                if (error.getErrorCode() == -1) {
                    int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
                    try {
                        this.logger.debug("微信系统繁忙，{}ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new WxErrorException(new WxError().setErrorCode(100003).setErrorMsg(MessageFormat.format("创建等待线程异常,{0}", e1.getMessage())));
                    }
                } else {
                    throw e;
                }
            }
        } while (retryTimes++ < this.maxRetryTimes);

        this.logger.warn("重试达到最大次数【{}】", this.maxRetryTimes);
        throw new WxErrorException(new WxError().setErrorCode(100002).setErrorMsg(MessageFormat.format("重试达到最大次数【{0}】,微信服务端异常，超出重试次数", this.maxRetryTimes)));
    }

    protected synchronized <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
        if (!uri.contains("component_access_token") && uri.contains("access_token=")) {
            throw new IllegalArgumentException("uri参数中不允许有access_token: " + uri);
        }
        String uriWithAccessToken = uri;
        if (!uri.contains("component_access_token") && !uri.contains("api_component_token")) {
            String accessToken = this.getAccessToken(false);
            uriWithAccessToken += uri.indexOf('?') == -1 ? "?access_token=" + accessToken : "&access_token=" + accessToken;
        } else if (!uri.contains("api_component_token")) {
            uriWithAccessToken = uri.replace("COMPONENT_ACCESS_TOKEN", this.getComponentAccessToken(false));
        }
        try {
            return executor.execute(getHttpclient(), this.httpProxy, uriWithAccessToken, data);
        } catch (WxErrorException e) {
            WxError error = e.getError();
            this.logger.error("\n[URL]:  {}\n[PARAMS]: {}\n[RESPONSE]: {}", uriWithAccessToken, data, error);
            /*
             * 发生以下情况时尝试刷新access_token
             * 40001 获取access_token时AppSecret错误，或者access_token无效
             * 42001 access_token超时
             */
            if (error.getErrorCode() == 42001 || error.getErrorCode() == 40001) {
                // 强制设置wxOpenConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
                this.configStorage.expireAccessToken();
                if (this.configStorage.autoRefreshToken()) {
                    return this.execute(executor, uri, data);
                }
            }

            if (error.getErrorCode() != 0) {
                this.logger.error("\n[URL]:  {}\n[PARAMS]: {}\n[RESPONSE]: {}", uri, data, error);
                throw new WxErrorException(error);
            }
            return null;
        } catch (IOException e) {
            this.logger.error("\n[URL]:  {}\n[PARAMS]: {}\n[EXCEPTION]: {}", uri, data, e.getMessage());
            throw new WxErrorException(new WxError().setErrorCode(100001).setErrorMsg(MessageFormat.format("连接建立失败,{0}", e.getMessage())));
        }

    }

    @Override
    public HttpHost getHttpProxy() {
        return this.httpProxy;
    }

    public CloseableHttpClient getHttpclient() {
        return this.httpClient;
    }

    private void initHttpClient() {
        ApacheHttpClientBuilder apacheHttpClientBuilder = this.configStorage
                .getApacheHttpClientBuilder();
        if (null == apacheHttpClientBuilder) {
            apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
        }

        apacheHttpClientBuilder.httpProxyHost(this.configStorage.getHttpProxyHost())
                .httpProxyPort(this.configStorage.getHttpProxyPort())
                .httpProxyUsername(this.configStorage.getHttpProxyUsername())
                .httpProxyPassword(this.configStorage.getHttpProxyPassword());

        if (this.configStorage.getSSLContext() != null) {
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    this.configStorage.getSSLContext(), new String[]{"TLSv1"}, null,
                    new DefaultHostnameVerifier());
            apacheHttpClientBuilder.sslConnectionSocketFactory(sslsf);
        }

        if (this.configStorage.getHttpProxyHost() != null && this.configStorage.getHttpProxyPort() > 0) {
            this.httpProxy = new HttpHost(this.configStorage.getHttpProxyHost(), this.configStorage.getHttpProxyPort());
        }

        this.httpClient = apacheHttpClientBuilder.build();
    }

    @Override
    public WxOpenConfigStorage getWxOpenConfigStorage() {
        return this.configStorage;
    }

    @Override
    public void setWxOpenConfigStorage(WxOpenConfigStorage wxConfigProvider) {
        this.configStorage = wxConfigProvider;
        this.initHttpClient();
    }

    @Override
    public void setRetrySleepMillis(int retrySleepMillis) {
        this.retrySleepMillis = retrySleepMillis;
    }

    @Override
    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    @Override
    public WxOpenKefuService getKefuService() {
        return this.kefuService;
    }

    @Override
    public WxOpenMaterialService getMaterialService() {
        return this.materialService;
    }

    @Override
    public WxOpenMenuService getMenuService() {
        return this.menuService;
    }

    @Override
    public WxOpenUserService getUserService() {
        return this.userService;
    }

    @Override
    public WxOpenUserTagService getUserTagService() {
        return this.userTagService;
    }

    @Override
    public WxOpenCardService getCardService() {
        return this.cardService;
    }

    @Override
    public WxOpenPayService getPayService() {
        return this.payService;
    }

    @Override
    public WxOpenDataCubeService getDataCubeService() {
        return this.dataCubeService;
    }

    @Override
    public WxOpenUserBlacklistService getBlackListService() {
        return this.blackListService;
    }

    @Override
    public WxOpenStoreService getStoreService() {
        return this.storeService;
    }

    @Override
    public WxOpenTemplateMsgService getTemplateMsgService() {
        return this.templateMsgService;
    }

    @Override
    public WxOpenQrcodeService getQrCodeService() {
        return this.qrCodeService;
    }

    @Override
    public WxOpenMassService getMassService() {
        return this.massService;
    }

    @Override
    public WxOpenIntelligentService getIntelligentService() {
        return this.intelligentService;
    }

    @Override
    public WxOpenShopService getShopService() {
        return this.shopService;
    }
}
