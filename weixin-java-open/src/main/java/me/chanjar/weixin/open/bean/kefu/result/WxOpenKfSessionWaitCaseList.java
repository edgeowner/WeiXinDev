package me.chanjar.weixin.open.bean.kefu.result;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.util.List;

/**
 * @author Binary Wang
 */
public class WxOpenKfSessionWaitCaseList {
    /**
     * count 未接入会话数量
     */
    @SerializedName("count")
    private Long count;

    /**
     * waitcaselist 未接入会话列表，最多返回100条数据
     */
    @SerializedName("waitcaselist")
    private List<WxOpenKfSession> kfSessionWaitCaseList;

    public static WxOpenKfSessionWaitCaseList fromJson(String json) {
        return WxOpenGsonBuilder.INSTANCE.create().fromJson(json,
                WxOpenKfSessionWaitCaseList.class);
    }

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public List<WxOpenKfSession> getKfSessionWaitCaseList() {
        return this.kfSessionWaitCaseList;
    }

    public void setKfSessionWaitCaseList(List<WxOpenKfSession> kfSessionWaitCaseList) {
        this.kfSessionWaitCaseList = kfSessionWaitCaseList;
    }

}
