package me.chanjar.weixin.open.api.impl;

import com.google.gson.JsonObject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxOpenUserService;
import me.chanjar.weixin.open.bean.WxOpenUserQuery;
import me.chanjar.weixin.open.bean.result.WxOpenUser;
import me.chanjar.weixin.open.bean.result.WxOpenUserList;

import java.util.List;

/**
 * Created by Binary Wang on 2016/7/21.
 */
public class WxOpenUserServiceImpl implements WxOpenUserService {
    private static final String API_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/user";
    private WxOpenService wxOpenService;

    public WxOpenUserServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public void userUpdateRemark(String openid, String remark) throws WxErrorException {
        String url = API_URL_PREFIX + "/info/updateremark";
        JsonObject json = new JsonObject();
        json.addProperty("openid", openid);
        json.addProperty("remark", remark);
        this.wxOpenService.post(url, json.toString());
    }

    @Override
    public WxOpenUser userInfo(String openid) throws WxErrorException {
        return this.userInfo(openid, null);
    }

    @Override
    public WxOpenUser userInfo(String openid, String lang) throws WxErrorException {
        String url = API_URL_PREFIX + "/info";
        lang = lang == null ? "zh_CN" : lang;
        String responseContent = this.wxOpenService.get(url,
                "openid=" + openid + "&lang=" + lang);
        return WxOpenUser.fromJson(responseContent);
    }

    @Override
    public WxOpenUserList userList(String next_openid) throws WxErrorException {
        String url = API_URL_PREFIX + "/get";
        String responseContent = this.wxOpenService.get(url,
                next_openid == null ? null : "next_openid=" + next_openid);
        return WxOpenUserList.fromJson(responseContent);
    }

    @Override
    public List<WxOpenUser> userInfoList(List<String> openids)
            throws WxErrorException {
        return this.userInfoList(new WxOpenUserQuery(openids));
    }

    @Override
    public List<WxOpenUser> userInfoList(WxOpenUserQuery userQuery) throws WxErrorException {
        String url = API_URL_PREFIX + "/info/batchget";
        String responseContent = this.wxOpenService.post(url,
                userQuery.toJsonString());
        return WxOpenUser.fromJsonList(responseContent);
    }

}
