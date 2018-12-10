package me.chanjar.weixin.open.builder.kefu;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;

/**
 * 语音消息builder
 * <pre>
 * 用法: WxOpenKefuMessage m = WxOpenKefuMessage.VOICE().mediaId(...).toUser(...).build();
 * </pre>
 *
 * @author chanjarster
 */
public final class VoiceBuilder extends BaseBuilder<VoiceBuilder> {
    private String mediaId;

    public VoiceBuilder() {
        this.msgType = WxConsts.CUSTOM_MSG_VOICE;
    }

    public VoiceBuilder mediaId(String media_id) {
        this.mediaId = media_id;
        return this;
    }

    @Override
    public WxOpenKefuMessage build() {
        WxOpenKefuMessage m = super.build();
        m.setMediaId(this.mediaId);
        return m;
    }
}
