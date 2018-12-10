package me.chanjar.weixin.open.builder.outxml;

import me.chanjar.weixin.open.bean.message.WxOpenXmlOutImageMessage;

/**
 * 图片消息builder
 *
 * @author chanjarster
 */
public final class ImageBuilder extends BaseBuilder<ImageBuilder, WxOpenXmlOutImageMessage> {

    private String mediaId;

    public ImageBuilder mediaId(String media_id) {
        this.mediaId = media_id;
        return this;
    }

    @Override
    public WxOpenXmlOutImageMessage build() {
        WxOpenXmlOutImageMessage m = new WxOpenXmlOutImageMessage();
        setCommon(m);
        m.setMediaId(this.mediaId);
        return m;
    }

}
