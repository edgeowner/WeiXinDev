package me.chanjar.weixin.open.builder.kefu;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;

/**
 * 卡券消息builder
 * <pre>
 * 用法: WxOpenKefuMessage m = WxOpenKefuMessage.WXCARD().cardId(...).toUser(...).build();
 * </pre>
 *
 * @author mgcnrx11
 */
public final class WxCardBuilder extends BaseBuilder<WxCardBuilder> {
    private String cardId;

    public WxCardBuilder() {
        this.msgType = WxConsts.CUSTOM_MSG_WXCARD;
    }

    public WxCardBuilder cardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    @Override
    public WxOpenKefuMessage build() {
        WxOpenKefuMessage m = super.build();
        m.setCardId(this.cardId);
        return m;
    }
}
