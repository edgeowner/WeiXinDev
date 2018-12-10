package me.chanjar.weixin.open.util.json;

import com.google.gson.*;
import me.chanjar.weixin.common.util.json.GsonHelper;
import me.chanjar.weixin.open.bean.material.WxOpenMaterialVideoInfoResult;

import java.lang.reflect.Type;

/**
 * @author codepiano
 */
public class WxOpenMaterialVideoInfoResultAdapter implements JsonDeserializer<WxOpenMaterialVideoInfoResult> {

    @Override
    public WxOpenMaterialVideoInfoResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxOpenMaterialVideoInfoResult uploadResult = new WxOpenMaterialVideoInfoResult();
        JsonObject uploadResultJsonObject = json.getAsJsonObject();

        if (uploadResultJsonObject.get("title") != null && !uploadResultJsonObject.get("title").isJsonNull()) {
            uploadResult.setTitle(GsonHelper.getAsString(uploadResultJsonObject.get("title")));
        }
        if (uploadResultJsonObject.get("description") != null && !uploadResultJsonObject.get("description").isJsonNull()) {
            uploadResult.setDescription(GsonHelper.getAsString(uploadResultJsonObject.get("description")));
        }
        if (uploadResultJsonObject.get("down_url") != null && !uploadResultJsonObject.get("down_url").isJsonNull()) {
            uploadResult.setDownUrl(GsonHelper.getAsString(uploadResultJsonObject.get("down_url")));
        }
        return uploadResult;
    }

}
