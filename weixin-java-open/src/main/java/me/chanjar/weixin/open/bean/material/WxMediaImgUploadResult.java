package me.chanjar.weixin.open.bean.material;

import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.io.Serializable;

/**
 * @author miller
 */
public class WxMediaImgUploadResult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1996392453428768829L;
    private String url;

    public static WxMediaImgUploadResult fromJson(String json) {
        return WxOpenGsonBuilder.create().fromJson(json, WxMediaImgUploadResult.class);
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
