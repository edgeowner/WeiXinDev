package me.chanjar.weixin.open.builder.outxml;

import me.chanjar.weixin.open.bean.message.WxOpenXmlOutTextMessage;

/**
 * 文本消息builder
 *
 * @author chanjarster
 */
public final class TextBuilder extends BaseBuilder<TextBuilder, WxOpenXmlOutTextMessage> {
    private String content;

    public TextBuilder content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public WxOpenXmlOutTextMessage build() {
        WxOpenXmlOutTextMessage m = new WxOpenXmlOutTextMessage();
        setCommon(m);
        m.setContent(this.content);
        return m;
    }
}
