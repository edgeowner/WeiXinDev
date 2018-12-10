package me.chanjar.weixin.open.bean.menu;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;
import me.chanjar.weixin.common.util.json.WxGsonBuilder;

/**
 * <pre>
 * Created by Binary Wang on 2016-11-25.
 * @author <a href="https://github.com/binarywang">binarywang(Binary Wang)</a>
 * </pre>
 */
public class WxOpenGetSelfMenuInfoResult {
    @SerializedName("selfmenu_info")
    private WxOpenSelfMenuInfo selfMenuInfo;

    @SerializedName("is_menu_open")
    private Integer isMenuOpen;

    public static WxOpenGetSelfMenuInfoResult fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxOpenGetSelfMenuInfoResult.class);
    }

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public WxOpenSelfMenuInfo getSelfMenuInfo() {
        return selfMenuInfo;
    }

    public void setSelfMenuInfo(WxOpenSelfMenuInfo selfMenuInfo) {
        this.selfMenuInfo = selfMenuInfo;
    }

    public Integer getIsMenuOpen() {
        return isMenuOpen;
    }

    public void setIsMenuOpen(Integer isMenuOpen) {
        this.isMenuOpen = isMenuOpen;
    }
}
