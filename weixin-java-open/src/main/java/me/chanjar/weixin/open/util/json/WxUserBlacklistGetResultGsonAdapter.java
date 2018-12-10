package me.chanjar.weixin.open.util.json;

import com.google.gson.*;
import me.chanjar.weixin.common.util.json.GsonHelper;
import me.chanjar.weixin.open.bean.result.WxOpenUserBlacklistGetResult;

import java.lang.reflect.Type;

/**
 * @author miller
 */
public class WxUserBlacklistGetResultGsonAdapter implements JsonDeserializer<WxOpenUserBlacklistGetResult> {
    @Override
    public WxOpenUserBlacklistGetResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject o = json.getAsJsonObject();
        WxOpenUserBlacklistGetResult wxOpenUserBlacklistGetResult = new WxOpenUserBlacklistGetResult();
        wxOpenUserBlacklistGetResult.setTotal(GsonHelper.getInteger(o, "total"));
        wxOpenUserBlacklistGetResult.setCount(GsonHelper.getInteger(o, "count"));
        wxOpenUserBlacklistGetResult.setNextOpenid(GsonHelper.getString(o, "next_openid"));
        if (o.get("data") != null && !o.get("data").isJsonNull() && !o.get("data").getAsJsonObject().get("openid").isJsonNull()) {
            JsonArray data = o.get("data").getAsJsonObject().get("openid").getAsJsonArray();
            for (int i = 0; i < data.size(); i++) {
                wxOpenUserBlacklistGetResult.getOpenidList().add(GsonHelper.getAsString(data.get(i)));
            }
        }
        return wxOpenUserBlacklistGetResult;
    }
}
