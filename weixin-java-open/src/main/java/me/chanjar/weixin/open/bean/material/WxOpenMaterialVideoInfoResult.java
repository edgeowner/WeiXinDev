package me.chanjar.weixin.open.bean.material;

import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.io.Serializable;

public class WxOpenMaterialVideoInfoResult implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1269131745333792202L;
    private String title;
    private String description;
    private String downUrl;

    public static WxOpenMaterialVideoInfoResult fromJson(String json) {
        return WxOpenGsonBuilder.create().fromJson(json, WxOpenMaterialVideoInfoResult.class);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownUrl() {
        return this.downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    @Override
    public String toString() {
        return "WxOpenMaterialVideoInfoResult [title=" + this.title + ", description=" + this.description + ", downUrl=" + this.downUrl + "]";
    }

}
