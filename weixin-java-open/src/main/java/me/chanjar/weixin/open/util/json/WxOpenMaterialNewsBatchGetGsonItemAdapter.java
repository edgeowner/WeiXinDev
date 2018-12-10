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
import me.chanjar.weixin.open.bean.material.WxOpenMaterialNews;
import me.chanjar.weixin.open.bean.material.WxOpenMaterialNewsBatchGetResult;

import java.lang.reflect.Type;
import java.util.Date;

public class WxOpenMaterialNewsBatchGetGsonItemAdapter implements JsonDeserializer<WxOpenMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem> {

    @Override
    public WxOpenMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        WxOpenMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem wxMaterialNewsBatchGetNewsItem = new WxOpenMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem();
        JsonObject json = jsonElement.getAsJsonObject();
        if (json.get("media_id") != null && !json.get("media_id").isJsonNull()) {
            wxMaterialNewsBatchGetNewsItem.setMediaId(GsonHelper.getAsString(json.get("media_id")));
        }
        if (json.get("update_time") != null && !json.get("update_time").isJsonNull()) {
            wxMaterialNewsBatchGetNewsItem.setUpdateTime(new Date(1000 * GsonHelper.getAsLong(json.get("update_time"))));
        }
        if (json.get("content") != null && !json.get("content").isJsonNull()) {
            JsonObject newsItem = json.getAsJsonObject("content");
            wxMaterialNewsBatchGetNewsItem.setContent(WxOpenGsonBuilder.create().fromJson(newsItem, WxOpenMaterialNews.class));
        }
        return wxMaterialNewsBatchGetNewsItem;
    }
}
