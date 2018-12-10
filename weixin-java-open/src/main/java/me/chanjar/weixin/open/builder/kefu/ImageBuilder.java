package me.chanjar.weixin.open.builder.kefu;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;

/**
 * 获得消息builder
 * <pre>
 * 用法: WxOpenKefuMessage m = WxOpenKefuMessage.IMAGE().mediaId(...).toUser(...).build();
 * </pre>
 *
 * @author chanjarster
 */
public final class ImageBuilder extends BaseBuilder<ImageBuilder> {
    private String mediaId;

    public ImageBuilder() {
        this.msgType = WxConsts.CUSTOM_MSG_IMAGE;
    }

    public ImageBuilder mediaId(String media_id) {
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
