package me.chanjar.weixin.common.bean.menu;

import me.chanjar.weixin.common.util.ToStringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMenuButton implements Serializable {
    private static final long serialVersionUID = -1070939403109776555L;

    private String type;
    private String name;
    private String key;
    private String url;
    private String mediaId;

    private List<WxMenuButton> subButtons = new ArrayList<>();

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public String getType() {
        return type;
    }

    public WxMenuButton setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public WxMenuButton setName(String name) {
        this.name = name;
        return this;
    }

    public String getKey() {
        return key;
    }

    public WxMenuButton setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public WxMenuButton setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public WxMenuButton setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public List<WxMenuButton> getSubButtons() {
        return subButtons;
    }

    public WxMenuButton setSubButtons(List<WxMenuButton> subButtons) {
        this.subButtons = subButtons;
        return this;
    }
}