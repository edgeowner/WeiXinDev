package me.chanjar.weixin.open.bean.material;

import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.io.Serializable;

public class WxOpenMaterialUploadResult implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -128818731449449537L;
    private String mediaId;
    private String url;

    public static WxOpenMaterialUploadResult fromJson(String json) {
        return WxOpenGsonBuilder.create().fromJson(json, WxOpenMaterialUploadResult.class);
    }

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WxOpenMaterialUploadResult [media_id=" + this.mediaId + ", url=" + this.url + "]";
    }

}

