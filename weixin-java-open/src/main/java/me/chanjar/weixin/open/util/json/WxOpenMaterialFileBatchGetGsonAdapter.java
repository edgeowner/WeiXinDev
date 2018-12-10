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
import me.chanjar.weixin.open.bean.material.WxOpenMaterialFileBatchGetResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WxOpenMaterialFileBatchGetGsonAdapter implements JsonDeserializer<WxOpenMaterialFileBatchGetResult> {

    @Override
    public WxOpenMaterialFileBatchGetResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        WxOpenMaterialFileBatchGetResult wxOpenMaterialFileBatchGetResult = new WxOpenMaterialFileBatchGetResult();
        JsonObject json = jsonElement.getAsJsonObject();
        if (json.get("total_count") != null && !json.get("total_count").isJsonNull()) {
            wxOpenMaterialFileBatchGetResult.setTotalCount(GsonHelper.getAsInteger(json.get("total_count")));
        }
        if (json.get("item_count") != null && !json.get("item_count").isJsonNull()) {
            wxOpenMaterialFileBatchGetResult.setItemCount(GsonHelper.getAsInteger(json.get("item_count")));
        }
        if (json.get("item") != null && !json.get("item").isJsonNull()) {
            JsonArray item = json.getAsJsonArray("item");
            List<WxOpenMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem> items = new ArrayList<>();
            for (JsonElement anItem : item) {
                JsonObject articleInfo = anItem.getAsJsonObject();
                items.add(WxOpenGsonBuilder.create().fromJson(articleInfo, WxOpenMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem.class));
            }
            wxOpenMaterialFileBatchGetResult.setItems(items);
        }
        return wxOpenMaterialFileBatchGetResult;
    }
}
