package me.chanjar.weixin.open.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.bean.WxOpenMassPreviewMessage;

import java.lang.reflect.Type;

/**
 * @author miller
 */
public class WxOpenMassPreviewMessageGsonAdapter implements JsonSerializer<WxOpenMassPreviewMessage> {
    @Override
    public JsonElement serialize(WxOpenMassPreviewMessage wxOpenMassPreviewMessage, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("towxname", wxOpenMassPreviewMessage.getToWxUsername());
        if (WxConsts.MASS_MSG_NEWS.equals(wxOpenMassPreviewMessage.getMsgType())) {
            JsonObject news = new JsonObject();
            news.addProperty("media_id", wxOpenMassPreviewMessage.getMediaId());
            jsonObject.add(WxConsts.MASS_MSG_NEWS, news);
        }
        if (WxConsts.MASS_MSG_TEXT.equals(wxOpenMassPreviewMessage.getMsgType())) {
            JsonObject sub = new JsonObject();
            sub.addProperty("content", wxOpenMassPreviewMessage.getContent());
            jsonObject.add(WxConsts.MASS_MSG_TEXT, sub);
        }
        if (WxConsts.MASS_MSG_VOICE.equals(wxOpenMassPreviewMessage.getMsgType())) {
            JsonObject sub = new JsonObject();
            sub.addProperty("media_id", wxOpenMassPreviewMessage.getMediaId());
            jsonObject.add(WxConsts.MASS_MSG_VOICE, sub);
        }
        if (WxConsts.MASS_MSG_IMAGE.equals(wxOpenMassPreviewMessage.getMsgType())) {
            JsonObject sub = new JsonObject();
            sub.addProperty("media_id", wxOpenMassPreviewMessage.getMediaId());
            jsonObject.add(WxConsts.MASS_MSG_IMAGE, sub);
        }
        if (WxConsts.MASS_MSG_VIDEO.equals(wxOpenMassPreviewMessage.getMsgType())) {
            JsonObject sub = new JsonObject();
            sub.addProperty("media_id", wxOpenMassPreviewMessage.getMediaId());
            jsonObject.add(WxConsts.MASS_MSG_VIDEO, sub);
        }
        jsonObject.addProperty("msgtype", wxOpenMassPreviewMessage.getMsgType());
        return jsonObject;
    }
}
