package me.chanjar.weixin.open.demo;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenMessageHandler;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutImageMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;

import java.util.Map;

public class DemoImageHandler implements WxOpenMessageHandler {
    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context,
                                      WxOpenService wxOpenService, WxSessionManager sessionManager) {
        try {
            WxMediaUploadResult wxMediaUploadResult = wxOpenService.getMaterialService()
                    .mediaUpload(WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG, ClassLoader.getSystemResourceAsStream("mm.jpeg"));
            WxOpenXmlOutImageMessage m
                    = WxOpenXmlOutMessage
                    .IMAGE()
                    .mediaId(wxMediaUploadResult.getMediaId())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
            return m;
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    }
}
