package me.chanjar.weixin.open.api;

import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;

/**
 * 消息匹配器，用在消息路由的时候
 */
public interface WxOpenMessageMatcher {

    /**
     * 消息是否匹配某种模式
     *
     * @param message
     */
    boolean match(WxOpenXmlMessage message);

}
