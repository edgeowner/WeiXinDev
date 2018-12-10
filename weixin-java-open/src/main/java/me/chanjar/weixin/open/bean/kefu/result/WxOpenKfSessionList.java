package me.chanjar.weixin.open.bean.kefu.result;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.util.List;

/**
 * @author Binary Wang
 */
public class WxOpenKfSessionList {
    /**
     * 会话列表
     */
    @SerializedName("sessionlist")
    private List<WxOpenKfSession> kfSessionList;

    public static WxOpenKfSessionList fromJson(String json) {
        return WxOpenGsonBuilder.INSTANCE.create().fromJson(json,
                WxOpenKfSessionList.class);
    }

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public List<WxOpenKfSession> getKfSessionList() {
        return this.kfSessionList;
    }

    public void setKfSessionList(List<WxOpenKfSession> kfSessionList) {
        this.kfSessionList = kfSessionList;
    }
}
