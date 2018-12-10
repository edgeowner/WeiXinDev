package me.chanjar.weixin.open.bean.template;

import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 参考${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN }发送模板消息接口部分
 */
public class WxOpenTemplateMessage implements Serializable {
    private static final long serialVersionUID = 5063374783759519418L;

    private String toUser;
    private String templateId;
    private String url;
    private List<WxOpenTemplateData> data = new ArrayList<>();

    public static WxOpenTemplateMessageBuilder builder() {
        return new WxOpenTemplateMessageBuilder();
    }

    public String getToUser() {
        return this.toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WxOpenTemplateData> getData() {
        return this.data;
    }

    public void setData(List<WxOpenTemplateData> data) {
        this.data = data;
    }

    public void addWxOpenTemplateData(WxOpenTemplateData datum) {
        this.data.add(datum);
    }

    public String toJson() {
        return WxOpenGsonBuilder.INSTANCE.create().toJson(this);
    }

    public static class WxOpenTemplateMessageBuilder {
        private String toUser;
        private String templateId;
        private String url;
        private List<WxOpenTemplateData> data = new ArrayList<>();

        public WxOpenTemplateMessageBuilder toUser(String toUser) {
            this.toUser = toUser;
            return this;
        }

        public WxOpenTemplateMessageBuilder templateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public WxOpenTemplateMessageBuilder url(String url) {
            this.url = url;
            return this;
        }

        public WxOpenTemplateMessageBuilder data(List<WxOpenTemplateData> data) {
            this.data = data;
            return this;
        }

        public WxOpenTemplateMessageBuilder from(WxOpenTemplateMessage origin) {
            this.toUser(origin.toUser);
            this.templateId(origin.templateId);
            this.url(origin.url);
            this.data(origin.data);
            return this;
        }

        public WxOpenTemplateMessage build() {
            WxOpenTemplateMessage m = new WxOpenTemplateMessage();
            m.toUser = this.toUser;
            m.templateId = this.templateId;
            m.url = this.url;
            m.data = this.data;
            return m;
        }
    }

}
