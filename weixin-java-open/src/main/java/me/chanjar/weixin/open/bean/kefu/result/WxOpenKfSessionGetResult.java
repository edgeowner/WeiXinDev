package me.chanjar.weixin.open.bean.kefu.result;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

/**
 * @author Binary Wang
 */
public class WxOpenKfSessionGetResult {
    /**
     * kf_account 正在接待的客服，为空表示没有人在接待
     */
    @SerializedName("kf_account")
    private String kfAccount;

    /**
     * createtime 会话接入的时间 ，UNIX时间戳
     */
    @SerializedName("createtime")
    private long createTime;

    public static WxOpenKfSessionGetResult fromJson(String json) {
        return WxOpenGsonBuilder.INSTANCE.create().fromJson(json, WxOpenKfSessionGetResult.class);
    }

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public String getKfAccount() {
        return this.kfAccount;
    }

    public void setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}
