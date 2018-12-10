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
import me.chanjar.weixin.open.bean.material.WxOpenMaterialNewsBatchGetResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WxOpenMaterialNewsBatchGetGsonAdapter implements JsonDeserializer<WxOpenMaterialNewsBatchGetResult> {

    @Override
    public WxOpenMaterialNewsBatchGetResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        WxOpenMaterialNewsBatchGetResult wxOpenMaterialNewsBatchGetResult = new WxOpenMaterialNewsBatchGetResult();
        JsonObject json = jsonElement.getAsJsonObject();
        if (json.get("total_count") != null && !json.get("total_count").isJsonNull()) {
            wxOpenMaterialNewsBatchGetResult.setTotalCount(GsonHelper.getAsInteger(json.get("total_count")));
        }
        if (json.get("item_count") != null && !json.get("item_count").isJsonNull()) {
            wxOpenMaterialNewsBatchGetResult.setItemCount(GsonHelper.getAsInteger(json.get("item_count")));
        }
        if (json.get("item") != null && !json.get("item").isJsonNull()) {
            JsonArray item = json.getAsJsonArray("item");
            List<WxOpenMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem> items = new ArrayList<>();
            for (JsonElement anItem : item) {
                JsonObject articleInfo = anItem.getAsJsonObject();
                items.add(WxOpenGsonBuilder.create().fromJson(articleInfo, WxOpenMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem.class));
            }
            wxOpenMaterialNewsBatchGetResult.setItems(items);
        }
        return wxOpenMaterialNewsBatchGetResult;
    }
}
