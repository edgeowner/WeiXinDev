package me.chanjar.weixin.open.bean.material;

import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.io.Serializable;

public class WxOpenMaterialArticleUpdate implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7611963949517780270L;
    private String mediaId;
    private int index;
    private WxOpenMaterialNews.WxOpenMaterialNewsArticle articles;

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public WxOpenMaterialNews.WxOpenMaterialNewsArticle getArticles() {
        return this.articles;
    }

    public void setArticles(WxOpenMaterialNews.WxOpenMaterialNewsArticle articles) {
        this.articles = articles;
    }

    public String toJson() {
        return WxOpenGsonBuilder.create().toJson(this);
    }

    @Override
    public String toString() {
        return "WxOpenMaterialArticleUpdate [" + "mediaId=" + this.mediaId + ", index=" + this.index + ", articles=" + this.articles + "]";
    }
}
