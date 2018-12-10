package me.chanjar.weixin.open.bean.kefu.request;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.io.Serializable;

public class WxOpenKfSessionRequest implements Serializable {
    private static final long serialVersionUID = -5451863610674856927L;

    /**
     * kf_account 完整客服账号，格式为：账号前缀@公众号微信号
     */
    @SerializedName("kf_account")
    private String kfAccount;

    /**
     * openid 客户openid
     */
    @SerializedName("openid")
    private String openid;

    public WxOpenKfSessionRequest(String kfAccount, String openid) {
        this.kfAccount = kfAccount;
        this.openid = openid;
    }

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public String toJson() {
        return WxOpenGsonBuilder.INSTANCE.create().toJson(this);
    }

    public String getKfAccount() {
        return this.kfAccount;
    }

    public void setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
    }

}
