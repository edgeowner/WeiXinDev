package me.chanjar.weixin.open.builder.outxml;

import me.chanjar.weixin.open.bean.message.WxOpenXmlOutVoiceMessage;

/**
 * 语音消息builder
 *
 * @author chanjarster
 */
public final class VoiceBuilder extends BaseBuilder<VoiceBuilder, WxOpenXmlOutVoiceMessage> {

    private String mediaId;

    public VoiceBuilder mediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    @Override
    public WxOpenXmlOutVoiceMessage build() {
        WxOpenXmlOutVoiceMessage m = new WxOpenXmlOutVoiceMessage();
        setCommon(m);
        m.setMediaId(this.mediaId);
        return m;
    }

}
