package me.chanjar.weixin.open.bean.store;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.ToStringUtils;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.util.List;

/**
 * 门店列表结果类
 *
 * @author <a href="https://github.com/binarywang">binarywang(Binary Wang)</a>
 *         Created by Binary Wang on 2016-09-27.
 */
public class WxOpenStoreListResult {
    /**
     * 错误码，0为正常
     */
    @SerializedName("errcode")
    private Integer errCode;
    /**
     * 错误信息
     */
    @SerializedName("errmsg")
    private String errMsg;
    /**
     * 门店信息列表
     */
    @SerializedName("business_list")
    private List<WxOpenStoreInfo> businessList;
    /**
     * 门店信息总数
     */
    @SerializedName("total_count")
    private Integer totalCount;

    public static WxOpenStoreListResult fromJson(String json) {
        return WxOpenGsonBuilder.create().fromJson(json, WxOpenStoreListResult.class);
    }

    @Override
    public String toString() {
        return ToStringUtils.toSimpleString(this);
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getErrCode() {
        return this.errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<WxOpenStoreInfo> getBusinessList() {
        return this.businessList;
    }

    public void setBusinessList(List<WxOpenStoreInfo> businessList) {
        this.businessList = businessList;
    }

}
