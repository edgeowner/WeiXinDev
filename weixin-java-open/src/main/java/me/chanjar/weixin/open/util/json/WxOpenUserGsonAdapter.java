/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package me.chanjar.weixin.open.util.json;

import com.google.gson.*;
import me.chanjar.weixin.common.util.json.GsonHelper;
import me.chanjar.weixin.open.bean.result.WxOpenUser;

import java.lang.reflect.Type;

public class WxOpenUserGsonAdapter implements JsonDeserializer<WxOpenUser> {

    @Override
    public WxOpenUser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject o = json.getAsJsonObject();
        WxOpenUser wxOpenUser = new WxOpenUser();
        Integer subscribe = GsonHelper.getInteger(o, "subscribe");
        if (subscribe != null) {
            wxOpenUser.setSubscribe(!new Integer(0).equals(subscribe));
        }
        wxOpenUser.setCity(GsonHelper.getString(o, "city"));
        wxOpenUser.setCountry(GsonHelper.getString(o, "country"));
        wxOpenUser.setHeadImgUrl(GsonHelper.getString(o, "headimgurl"));
        wxOpenUser.setLanguage(GsonHelper.getString(o, "language"));
        wxOpenUser.setNickname(GsonHelper.getString(o, "nickname"));
        wxOpenUser.setOpenId(GsonHelper.getString(o, "openid"));
        wxOpenUser.setProvince(GsonHelper.getString(o, "province"));
        wxOpenUser.setSubscribeTime(GsonHelper.getLong(o, "subscribe_time"));
        wxOpenUser.setUnionId(GsonHelper.getString(o, "unionid"));
        Integer sexId = GsonHelper.getInteger(o, "sex");
        wxOpenUser.setRemark(GsonHelper.getString(o, "remark"));
        wxOpenUser.setGroupId(GsonHelper.getInteger(o, "groupid"));
        wxOpenUser.setTagIds(GsonHelper.getIntArray(o, "tagid_list"));
        wxOpenUser.setPrivilege(GsonHelper.getStringArray(o, "privilege"));
        wxOpenUser.setSexId(sexId);
        if (new Integer(1).equals(sexId)) {
            wxOpenUser.setSex("男");
        } else if (new Integer(2).equals(sexId)) {
            wxOpenUser.setSex("女");
        } else {
            wxOpenUser.setSex("未知");
        }
        return wxOpenUser;
    }

}
