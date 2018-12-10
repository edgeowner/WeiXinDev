package me.chanjar.weixin.open.demo;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenMessageHandler;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;

import java.util.Map;

/**
 * Created by qianjia on 15/1/22.
 */
public class DemoOAuth2Handler implements WxOpenMessageHandler {
    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage,
                                      Map<String, Object> context, WxOpenService wxOpenService,
                                      WxSessionManager sessionManager) {
        String href = "<a href=\"" + wxOpenService.oauth2buildAuthorizationUrl(
                wxOpenService.getWxOpenConfigStorage().getOauth2redirectUri(),
                WxConsts.OAUTH2_SCOPE_USER_INFO, null) + "\">测试oauth2</a>";
        return WxOpenXmlOutMessage.TEXT().content(href)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
    }
}
