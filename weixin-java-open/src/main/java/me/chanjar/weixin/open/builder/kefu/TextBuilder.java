package me.chanjar.weixin.open.builder.kefu;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;

/**
 * 文本消息builder
 * <pre>
 * 用法: WxOpenKefuMessage m = WxOpenKefuMessage.TEXT().content(...).toUser(...).build();
 * </pre>
 *
 * @author chanjarster
 */
public final class TextBuilder extends BaseBuilder<TextBuilder> {
    private String content;

    public TextBuilder() {
        this.msgType = WxConsts.CUSTOM_MSG_TEXT;
    }

    public TextBuilder content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public WxOpenKefuMessage build() {
        WxOpenKefuMessage m = super.build();
        m.setContent(this.content);
        return m;
    }
}
