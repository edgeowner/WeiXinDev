package me.chanjar.weixin.open.bean;

import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.io.Serializable;

/**
 * 群发时用到的视频素材
 *
 * @author chanjarster
 */
public class WxOpenMassVideo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9153925016061915637L;
    private String mediaId;
    private String title;
    private String description;

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

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String toJson() {
        return WxOpenGsonBuilder.INSTANCE.create().toJson(this);
    }
}
