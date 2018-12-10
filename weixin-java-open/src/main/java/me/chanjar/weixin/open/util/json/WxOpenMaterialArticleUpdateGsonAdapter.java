/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package me.chanjar.weixin.open.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.chanjar.weixin.open.bean.material.WxOpenMaterialArticleUpdate;
import me.chanjar.weixin.open.bean.material.WxOpenMaterialNews;

import java.lang.reflect.Type;

public class WxOpenMaterialArticleUpdateGsonAdapter implements JsonSerializer<WxOpenMaterialArticleUpdate> {

    @Override
    public JsonElement serialize(WxOpenMaterialArticleUpdate wxOpenMaterialArticleUpdate, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject articleUpdateJson = new JsonObject();
        articleUpdateJson.addProperty("media_id", wxOpenMaterialArticleUpdate.getMediaId());
        articleUpdateJson.addProperty("index", wxOpenMaterialArticleUpdate.getIndex());
        articleUpdateJson.add("articles", WxOpenGsonBuilder.create().toJsonTree(wxOpenMaterialArticleUpdate.getArticles(), WxOpenMaterialNews.WxOpenMaterialNewsArticle.class));
        return articleUpdateJson;
    }

}
