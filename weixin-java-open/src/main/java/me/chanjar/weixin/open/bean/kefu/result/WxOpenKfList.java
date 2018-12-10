package me.chanjar.weixin.open.bean.kefu.result;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.util.List;

/**
 * @author Binary Wang
 */
public class WxOpenKfList {
    @SerializedName("kf_list")
    private List<WxOpenKfInfo> kfList;

    public static WxOpenKfList fromJson(String json) {
        return WxOpenGsonBuilder.INSTANCE.create().fromJson(json, WxOpenKfList.class);
    }

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public List<WxOpenKfInfo> getKfList() {
        return this.kfList;
    }

    public void setKfList(List<WxOpenKfInfo> kfList) {
        this.kfList = kfList;
    }
}
