package me.chanjar.weixin.open.builder.kefu;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;

/**
 * 音乐消息builder
 * <pre>
 * 用法: WxOpenKefuMessage m = WxOpenKefuMessage.MUSIC()
 *                      .musicUrl(...)
 *                      .hqMusicUrl(...)
 *                      .title(...)
 *                      .thumbMediaId(..)
 *                      .description(..)
 *                      .toUser(...)
 *                      .build();
 * </pre>
 */
public final class MusicBuilder extends BaseBuilder<MusicBuilder> {
    private String title;
    private String description;
    private String thumbMediaId;
    private String musicUrl;
    private String hqMusicUrl;

    public MusicBuilder() {
        this.msgType = WxConsts.CUSTOM_MSG_MUSIC;
    }

    public MusicBuilder musicUrl(String musicurl) {
        this.musicUrl = musicurl;
        return this;
    }

    public MusicBuilder hqMusicUrl(String hqMusicurl) {
        this.hqMusicUrl = hqMusicurl;
        return this;
    }

    public MusicBuilder title(String title) {
        this.title = title;
        return this;
    }

    public MusicBuilder description(String description) {
        this.description = description;
        return this;
    }

    public MusicBuilder thumbMediaId(String thumb_media_id) {
        this.thumbMediaId = thumb_media_id;
        return this;
    }

    @Override
    public WxOpenKefuMessage build() {
        WxOpenKefuMessage m = super.build();
        m.setMusicUrl(this.musicUrl);
        m.setHqMusicUrl(this.hqMusicUrl);
        m.setTitle(this.title);
        m.setDescription(this.description);
        m.setThumbMediaId(this.thumbMediaId);
        return m;
    }
}
