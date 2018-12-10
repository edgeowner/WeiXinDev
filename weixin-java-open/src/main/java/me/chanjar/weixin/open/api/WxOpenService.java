package me.chanjar.weixin.open.api;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.open.bean.component.*;
import me.chanjar.weixin.open.bean.result.WxOpenUser;
import org.apache.http.HttpHost;

/**
 * 微信API的Service
 */
public interface WxOpenService {

    /**
     * <pre>
     * 验证消息的确来自微信服务器
     * 详情请见: ${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421135319&token=&lang=zh_CN}
     * </pre>
     */
    boolean checkSignature(String timestamp, String nonce, String signature);

    /**
     * 获取access_token, 不强制刷新access_token
     *
     * @see #getAccessToken(boolean)
     */
    String getAccessToken() throws WxErrorException;

    /**
     * <pre>
     * 获取access_token，本方法线程安全
     * 且在多线程同时刷新时只刷新一次，避免超出2000次/日的调用次数上限
     *
     * 另：本service的所有方法都会在access_token过期是调用此方法
     *
     * 程序员在非必要情况下尽量不要主动调用此方法
     *
     * 详情请见:${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183&token=&lang=zh_CN}
     * </pre>
     *
     * @param forceRefresh 强制刷新
     */
    String getAccessToken(boolean forceRefresh) throws WxErrorException;

    String getComponentAccessToken() throws WxErrorException;

    //开方平台获取AccessToken
    String getComponentAccessToken(boolean forceRefresh) throws WxErrorException;

    /**
     * 获得jsapi_ticket,不强制刷新jsapi_ticket
     *
     * @see #getJsapiTicket(boolean)
     */
    String getJsapiTicket() throws WxErrorException;

    /**
     * <pre>
     * 获得jsapi_ticket
     * 获得时会检查jsapiToken是否过期，如果过期了，那么就刷新一下，否则就什么都不干
     *
     * 详情请见：${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141115&token=&lang=zh_CN}
     * </pre>
     *
     * @param forceRefresh 强制刷新
     */
    String getJsapiTicket(boolean forceRefresh) throws WxErrorException;

    /**
     * <pre>
     * 创建调用jsapi时所需要的签名
     *
     * 详情请见：${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141115&token=&lang=zh_CN}
     * </pre>
     */
    WxJsapiSignature createJsapiSignature(String url) throws WxErrorException;


    /**
     * <pre>
     * 构造第三方使用网站应用授权登录的url
     * 详情请见: <a href="https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=&lang=zh_CN">网站应用微信登录开发指南</a>
     * URL格式为：${@code https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect}
     * </pre>
     *
     * @param redirectURI 用户授权完成后的重定向链接，无需urlencode, 方法内会进行encode
     * @param scope       应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login即可
     * @param state       非必填，用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
     * @return url
     */
    String buildQrConnectUrl(String redirectURI, String scope, String state);


    /**
     * <pre>
     * 用oauth2获取用户信息, 当前面引导授权时的scope是snsapi_userinfo的时候才可以
     * </pre>
     *
     * @param lang zh_CN, zh_TW, en
     */
    WxOpenUser oauth2getUserInfo(WxOpenOAuth2AccessToken oAuth2AccessToken, String lang) throws WxErrorException;

    /**
     * <pre>
     * 验证oauth2的access token是否有效
     * </pre>
     */
    boolean oauth2validateAccessToken(WxOpenOAuth2AccessToken oAuth2AccessToken) throws WxErrorException;

    /**
     * <pre>
     * 获取微信服务器IP地址
     * http://mp.weixin.qq.com/wiki/0/2ad4b6bfd29f30f71d39616c2a0fcedc.html
     * </pre>
     */
    String[] getCallbackIP() throws WxErrorException;

    String createComponentLoginPageUrl(String pre_auth_code, String redirect_uri);

    WxOpenPreAuthCode getPreAuthCode() throws WxErrorException;

    WxOpenAuthorization getAuthorization(String authorization_code) throws WxErrorException;

    WxOpenAuthorizerInfo getAuthorizerInfo(String authorizer_appid) throws WxErrorException;

    WxOpenAuthorizerOption getAuthorizerOption(String authorizer_appid, String option_name, String option_value) throws WxErrorException;

    WxOpenSetAuthorizerOption setAuthorizerOption(String authorizer_appid, String option_name) throws WxErrorException;

    String oauth2buildAuthorizationUrl(String redirectURI, String scope, String state);


    WxOpenOAuth2AccessToken getOAuth2AccessToken(String code) throws WxErrorException;


    WxOpenOAuth2AccessToken refreshOauth2AccessToken(String refresh_token) throws WxErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有微信API中的GET请求
     */
    String get(String url, String queryParam) throws WxErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有微信API中的POST请求
     */
    String post(String url, String postData) throws WxErrorException;

    /**
     * <pre>
     * Service没有实现某个API的时候，可以用这个，
     * 比{@link #get}和{@link #post}方法更灵活，可以自己构造RequestExecutor用来处理不同的参数和不同的返回类型。
     * 可以参考，{@link me.chanjar.weixin.common.util.http.MediaUploadRequestExecutor}的实现方法
     * </pre>
     */
    <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException;

    /**
     * 获取代理对象
     */
    HttpHost getHttpProxy();

    /**
     * <pre>
     * 设置当微信系统响应系统繁忙时，要等待多少 retrySleepMillis(ms) * 2^(重试次数 - 1) 再发起重试
     * 默认：1000ms
     * </pre>
     */
    void setRetrySleepMillis(int retrySleepMillis);

    /**
     * <pre>
     * 设置当微信系统响应系统繁忙时，最大重试次数
     * 默认：5次
     * </pre>
     */
    void setMaxRetryTimes(int maxRetryTimes);

    /**
     * 获取WxOpenConfigStorage 对象
     *
     * @return WxOpenConfigStorage
     */
    WxOpenConfigStorage getWxOpenConfigStorage();

    /**
     * 注入 {@link WxOpenConfigStorage} 的实现
     */
    void setWxOpenConfigStorage(WxOpenConfigStorage wxConfigProvider);

    /**
     * 返回客服接口方法实现类，以方便调用其各个接口
     *
     * @return WxOpenKefuService
     */
    WxOpenKefuService getKefuService();

    /**
     * 返回素材相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenMaterialService
     */
    WxOpenMaterialService getMaterialService();

    /**
     * 返回菜单相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenMenuService
     */
    WxOpenMenuService getMenuService();

    /**
     * 返回用户相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenUserService
     */
    WxOpenUserService getUserService();

    /**
     * 返回用户标签相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenUserTagService
     */
    WxOpenUserTagService getUserTagService();

    /**
     * 返回二维码相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenQrcodeService
     */
    WxOpenQrcodeService getQrCodeService();

    /**
     * 返回卡券相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenCardService
     */
    WxOpenCardService getCardService();

    /**
     * 返回微信支付相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenPayService
     */
    WxOpenPayService getPayService();

    /**
     * 返回数据分析统计相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenDataCubeService
     */
    WxOpenDataCubeService getDataCubeService();

    /**
     * 返回用户黑名单管理相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenUserBlacklistService
     */
    WxOpenUserBlacklistService getBlackListService();

    /**
     * 返回门店管理相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenStoreService
     */
    WxOpenStoreService getStoreService();

    /**
     * 返回模板消息相关接口方法的实现类对象，以方便调用其各个接口
     *
     * @return WxOpenTemplateMsgService
     */
    WxOpenTemplateMsgService getTemplateMsgService();


    WxOpenMassService getMassService();

    WxOpenIntelligentService getIntelligentService();

    WxOpenShopService getShopService();
}
