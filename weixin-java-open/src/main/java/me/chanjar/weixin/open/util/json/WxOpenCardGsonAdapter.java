package me.chanjar.weixin.open.util.json;

import com.google.gson.*;
import me.chanjar.weixin.common.util.json.GsonHelper;
import me.chanjar.weixin.open.bean.WxOpenCard;

import java.lang.reflect.Type;

/**
 * Created by YuJian on 15/11/11.
 *
 * @author YuJian
 * @version 15/11/11
 */
public class WxOpenCardGsonAdapter implements JsonDeserializer<WxOpenCard> {

    @Override
    public WxOpenCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
            jsonDeserializationContext) throws JsonParseException {
        WxOpenCard card = new WxOpenCard();
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        card.setCardId(GsonHelper.getString(jsonObject, "card_id"));
        card.setBeginTime(GsonHelper.getLong(jsonObject, "begin_time"));
        card.setEndTime(GsonHelper.getLong(jsonObject, "end_time"));

        return card;
    }

}
