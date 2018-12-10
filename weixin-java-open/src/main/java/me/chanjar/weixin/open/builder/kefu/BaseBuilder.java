package me.chanjar.weixin.open.builder.kefu;

import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;

public class BaseBuilder<T> {
    protected String msgType;
    protected String toUser;

    public T toUser(String toUser) {
        this.toUser = toUser;
        return (T) this;
    }

    public WxOpenKefuMessage build() {
        WxOpenKefuMessage m = new WxOpenKefuMessage();
        m.setMsgType(this.msgType);
        m.setToUser(this.toUser);
        return m;
    }
}
