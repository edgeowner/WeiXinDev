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
import me.chanjar.weixin.open.bean.result.WxOpenSemanticQueryResult;

import java.lang.reflect.Type;

/**
 * @author Daniel Qian
 */
public class WxOpenSemanticQueryResultAdapter implements JsonDeserializer<WxOpenSemanticQueryResult> {

    @Override
    public WxOpenSemanticQueryResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxOpenSemanticQueryResult result = new WxOpenSemanticQueryResult();
        JsonObject resultJsonObject = json.getAsJsonObject();

        if (GsonHelper.getString(resultJsonObject, "query") != null) {
            result.setQuery(GsonHelper.getString(resultJsonObject, "query"));
        }
        if (GsonHelper.getString(resultJsonObject, "type") != null) {
            result.setType(GsonHelper.getString(resultJsonObject, "type"));
        }
        if (resultJsonObject.get("semantic") != null) {
            result.setSemantic(resultJsonObject.get("semantic").toString());
        }
        if (resultJsonObject.get("result") != null) {
            result.setResult(resultJsonObject.get("result").toString());
        }
        if (GsonHelper.getString(resultJsonObject, "answer") != null) {
            result.setAnswer(GsonHelper.getString(resultJsonObject, "answer"));
        }
        if (GsonHelper.getString(resultJsonObject, "text") != null) {
            result.setText(GsonHelper.getString(resultJsonObject, "text"));
        }
        return result;
    }

}
