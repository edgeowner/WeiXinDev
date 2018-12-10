package me.chanjar.weixin.open.bean.kefu.result;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.util.List;

/**
 * @author Binary Wang
 */
public class WxOpenKfOnlineList {
    @SerializedName("kf_online_list")
    private List<WxOpenKfInfo> kfOnlineList;

    public static WxOpenKfOnlineList fromJson(String json) {
        return WxOpenGsonBuilder.INSTANCE.create().fromJson(json, WxOpenKfOnlineList.class);
    }

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public List<WxOpenKfInfo> getKfOnlineList() {
        return this.kfOnlineList;
    }

    public void setKfOnlineList(List<WxOpenKfInfo> kfOnlineList) {
        this.kfOnlineList = kfOnlineList;
    }
}
