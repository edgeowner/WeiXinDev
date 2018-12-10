package me.chanjar.weixin.open.bean.result;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 微信用户信息
 *
 * @author chanjarster
 */
public class WxOpenUser implements Serializable {

    private static final long serialVersionUID = 5788154322646488738L;
    private Boolean subscribe;
    private String openId;
    private String nickname;
    private String sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headImgUrl;
    private Long subscribeTime;
    private String unionId;
    private Integer sexId;
    private String remark;
    private Integer groupId;
    private Integer[] tagIds;
    private String[] privilege;

    public static WxOpenUser fromJson(String json) {
        return WxOpenGsonBuilder.INSTANCE.create().fromJson(json, WxOpenUser.class);
    }

    public static List<WxOpenUser> fromJsonList(String json) {
        Type collectionType = new TypeToken<List<WxOpenUser>>() {
        }.getType();
        Gson gson = WxOpenGsonBuilder.INSTANCE.create();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return gson.fromJson(jsonObject.get("user_info_list"), collectionType);
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public WxOpenUser setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public WxOpenUser setOpenId(String openId) {
        this.openId = openId == null ? "" : openId;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public WxOpenUser setNickname(String nickname) {
        this.nickname = nickname == null ? "" : nickname;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public WxOpenUser setSex(String sex) {
        this.sex = sex == null ? "" : sex;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public WxOpenUser setLanguage(String language) {
        this.language = language == null ? "" : language;
        return this;
    }

    public String getCity() {
        return city;
    }

    public WxOpenUser setCity(String city) {
        this.city = city == null ? "" : city;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public WxOpenUser setProvince(String province) {
        this.province = province == null ? "" : province;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public WxOpenUser setCountry(String country) {
        this.country = country == null ? "" : country;
        return this;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public WxOpenUser setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl == null ? "" : headImgUrl;
        return this;
    }

    public Long getSubscribeTime() {
        return subscribeTime;
    }

    public WxOpenUser setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
        return this;
    }

    public String getUnionId() {
        return unionId;
    }

    public WxOpenUser setUnionId(String unionId) {
        this.unionId = unionId == null ? "" : unionId;
        return this;
    }

    public Integer getSexId() {
        return sexId;
    }

    public WxOpenUser setSexId(Integer sexId) {
        this.sexId = sexId == null ? 0 : sexId;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public WxOpenUser setRemark(String remark) {
        this.remark = remark == null ? "" : remark;
        return this;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public WxOpenUser setGroupId(Integer groupId) {
        this.groupId = groupId == null ? 0 : groupId;
        return this;
    }

    public Integer[] getTagIds() {
        return tagIds;
    }

    public WxOpenUser setTagIds(Integer[] tagIds) {
        this.tagIds = tagIds;
        return this;
    }

    public String[] getPrivilege() {
        return privilege;
    }

    public WxOpenUser setPrivilege(String[] privilege) {
        this.privilege = privilege;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
