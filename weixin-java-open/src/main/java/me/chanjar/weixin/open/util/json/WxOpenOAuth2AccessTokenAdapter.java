package me.chanjar.weixin.open.util.json;

import com.google.gson.*;
import me.chanjar.weixin.common.util.json.GsonHelper;
import me.chanjar.weixin.open.bean.component.WxOpenOAuth2AccessToken;

import java.lang.reflect.Type;

public class WxOpenOAuth2AccessTokenAdapter implements JsonDeserializer<WxOpenOAuth2AccessToken> {

    @Override
    public WxOpenOAuth2AccessToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
        WxOpenOAuth2AccessToken accessToken = new WxOpenOAuth2AccessToken();
        JsonObject accessTokenJsonObject = json.getAsJsonObject();

        if (accessTokenJsonObject.get("access_token") != null && !accessTokenJsonObject.get("access_token").isJsonNull()) {
            accessToken.setAccessToken(GsonHelper.getAsString(accessTokenJsonObject.get("access_token")));
        }
        if (accessTokenJsonObject.get("expires_in") != null && !accessTokenJsonObject.get("expires_in").isJsonNull()) {
            accessToken.setExpiresIn(GsonHelper.getAsPrimitiveInt(accessTokenJsonObject.get("expires_in")));
        }
        if (accessTokenJsonObject.get("refresh_token") != null && !accessTokenJsonObject.get("refresh_token").isJsonNull()) {
            accessToken.setRefreshToken(GsonHelper.getAsString(accessTokenJsonObject.get("refresh_token")));
        }
        if (accessTokenJsonObject.get("openid") != null && !accessTokenJsonObject.get("openid").isJsonNull()) {
            accessToken.setOpenId(GsonHelper.getAsString(accessTokenJsonObject.get("openid")));
        }
        if (accessTokenJsonObject.get("scope") != null && !accessTokenJsonObject.get("scope").isJsonNull()) {
            accessToken.setScope(GsonHelper.getAsString(accessTokenJsonObject.get("scope")));
        }
        if (accessTokenJsonObject.get("unionid") != null && !accessTokenJsonObject.get("unionid").isJsonNull()) {
            accessToken.setUnionId(GsonHelper.getAsString(accessTokenJsonObject.get("unionid")));
        }
        return accessToken;
    }

}
