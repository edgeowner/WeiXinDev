package me.chanjar.weixin.open.builder.kefu;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;

/**
 * 图文消息builder
 * <pre>
 * 用法:
 * WxOpenKefuMessage m = WxOpenKefuMessage.NEWS().mediaId("xxxxx").toUser(...).build();
 * </pre>
 *
 * @author Binary Wang
 */
public final class OpenNewsBuilder extends BaseBuilder<OpenNewsBuilder> {
    private String mediaId;

    public OpenNewsBuilder() {
        this.msgType = WxConsts.CUSTOM_MSG_MPNEWS;
    }

    public OpenNewsBuilder mediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    @Override
    public WxOpenKefuMessage build() {
        WxOpenKefuMessage m = super.build();
        m.setOpenNewsMediaId(this.mediaId);
        return m;
    }
}
