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
import me.chanjar.weixin.open.bean.WxOpenMassNews;

import java.lang.reflect.Type;

public class WxOpenMassNewsGsonAdapter implements JsonSerializer<WxOpenMassNews>, JsonDeserializer<WxOpenMassNews> {

    @Override
    public JsonElement serialize(WxOpenMassNews message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject newsJson = new JsonObject();

        JsonArray articleJsonArray = new JsonArray();
        for (WxOpenMassNews.WxOpenMassNewsArticle article : message.getArticles()) {
            JsonObject articleJson = WxOpenGsonBuilder.create().toJsonTree(article, WxOpenMassNews.WxOpenMassNewsArticle.class).getAsJsonObject();
            articleJsonArray.add(articleJson);
        }
        newsJson.add("articles", articleJsonArray);

        return newsJson;
    }

    @Override
    public WxOpenMassNews deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        WxOpenMassNews wxOpenMassNews = new WxOpenMassNews();
        JsonObject json = jsonElement.getAsJsonObject();
        if (json.get("media_id") != null && !json.get("media_id").isJsonNull()) {
            JsonArray articles = json.getAsJsonArray("articles");
            for (JsonElement article1 : articles) {
                JsonObject articleInfo = article1.getAsJsonObject();
                WxOpenMassNews.WxOpenMassNewsArticle article = WxOpenGsonBuilder.create().fromJson(articleInfo, WxOpenMassNews.WxOpenMassNewsArticle.class);
                wxOpenMassNews.addArticle(article);
            }
        }
        return wxOpenMassNews;
    }
}
