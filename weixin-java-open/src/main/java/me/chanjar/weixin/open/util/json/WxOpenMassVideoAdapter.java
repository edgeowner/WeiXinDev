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
import me.chanjar.weixin.open.bean.WxOpenMassVideo;

import java.lang.reflect.Type;

/**
 * @author Daniel Qian
 */
public class WxOpenMassVideoAdapter implements JsonSerializer<WxOpenMassVideo> {

    @Override
    public JsonElement serialize(WxOpenMassVideo message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("media_id", message.getMediaId());
        messageJson.addProperty("description", message.getDescription());
        messageJson.addProperty("title", message.getTitle());
        return messageJson;
    }

}