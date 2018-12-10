package me.chanjar.weixin.open.builder.outxml;

import me.chanjar.weixin.open.bean.message.WxOpenXmlOutNewsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息builder
 *
 * @author chanjarster
 */
public final class NewsBuilder extends BaseBuilder<NewsBuilder, WxOpenXmlOutNewsMessage> {

    protected final List<WxOpenXmlOutNewsMessage.Item> articles = new ArrayList<>();

    public NewsBuilder addArticle(WxOpenXmlOutNewsMessage.Item item) {
        this.articles.add(item);
        return this;
    }

    @Override
    public WxOpenXmlOutNewsMessage build() {
        WxOpenXmlOutNewsMessage m = new WxOpenXmlOutNewsMessage();
        for (WxOpenXmlOutNewsMessage.Item item : this.articles) {
            m.addArticle(item);
        }
        setCommon(m);
        return m;
    }

}
