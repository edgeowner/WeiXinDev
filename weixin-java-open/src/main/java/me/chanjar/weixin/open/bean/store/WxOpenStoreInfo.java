package me.chanjar.weixin.open.bean.store;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;

public class WxOpenStoreInfo {
    @SerializedName("base_info")
    private WxOpenStoreBaseInfo baseInfo;

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public WxOpenStoreBaseInfo getBaseInfo() {
        return this.baseInfo;
    }

    public void setBaseInfo(WxOpenStoreBaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

}
