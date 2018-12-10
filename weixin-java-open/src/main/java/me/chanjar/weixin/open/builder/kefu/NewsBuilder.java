package me.chanjar.weixin.open.builder.kefu;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息builder
 * <pre>
 * 用法:
 * WxOpenKefuMessage m = WxOpenKefuMessage.NEWS().addArticle(article).toUser(...).build();
 * </pre>
 *
 * @author chanjarster
 */
public final class NewsBuilder extends BaseBuilder<NewsBuilder> {

    private List<WxOpenKefuMessage.WxArticle> articles = new ArrayList<>();

    public NewsBuilder() {
        this.msgType = WxConsts.CUSTOM_MSG_NEWS;
    }

    public NewsBuilder addArticle(WxOpenKefuMessage.WxArticle article) {
        this.articles.add(article);
        return this;
    }

    @Override
    public WxOpenKefuMessage build() {
        WxOpenKefuMessage m = super.build();
        m.setArticles(this.articles);
        return m;
    }
}
