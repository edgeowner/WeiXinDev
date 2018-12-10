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
import me.chanjar.weixin.open.bean.material.WxOpenMaterialNews;

import java.lang.reflect.Type;

public class WxOpenMaterialNewsGsonAdapter implements JsonSerializer<WxOpenMaterialNews>, JsonDeserializer<WxOpenMaterialNews> {

    @Override
    public JsonElement serialize(WxOpenMaterialNews wxOpenMaterialNews, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject newsJson = new JsonObject();

        JsonArray articleJsonArray = new JsonArray();
        for (WxOpenMaterialNews.WxOpenMaterialNewsArticle article : wxOpenMaterialNews.getArticles()) {
            JsonObject articleJson = WxOpenGsonBuilder.create().toJsonTree(article, WxOpenMaterialNews.WxOpenMaterialNewsArticle.class).getAsJsonObject();
            articleJsonArray.add(articleJson);
        }
        newsJson.add("articles", articleJsonArray);

        return newsJson;
    }

    @Override
    public WxOpenMaterialNews deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        WxOpenMaterialNews wxOpenMaterialNews = new WxOpenMaterialNews();
        JsonObject json = jsonElement.getAsJsonObject();
        if (json.get("news_item") != null && !json.get("news_item").isJsonNull()) {
            JsonArray articles = json.getAsJsonArray("news_item");
            for (JsonElement article1 : articles) {
                JsonObject articleInfo = article1.getAsJsonObject();
                WxOpenMaterialNews.WxOpenMaterialNewsArticle article = WxOpenGsonBuilder.create().fromJson(articleInfo, WxOpenMaterialNews.WxOpenMaterialNewsArticle.class);
                wxOpenMaterialNews.addArticle(article);
            }
        }
        return wxOpenMaterialNews;
    }
}
