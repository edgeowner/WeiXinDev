package me.chanjar.weixin.open.api;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;

import java.util.Map;

/**
 * 微信消息拦截器，可以用来做验证
 *
 * @author Daniel Qian
 */
public interface WxOpenMessageInterceptor {

    /**
     * 拦截微信消息
     *
     * @param wxMessage
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param wxOpenService
     * @param sessionManager
     * @return true代表OK，false代表不OK
     */
    boolean intercept(WxOpenXmlMessage wxMessage,
                      Map<String, Object> context,
                      WxOpenService wxOpenService,
                      WxSessionManager sessionManager) throws WxErrorException;

}
