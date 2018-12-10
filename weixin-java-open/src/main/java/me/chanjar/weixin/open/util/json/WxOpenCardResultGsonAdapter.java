package me.chanjar.weixin.open.util.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.chanjar.weixin.common.util.json.GsonHelper;
import me.chanjar.weixin.open.bean.WxOpenCard;
import me.chanjar.weixin.open.bean.result.WxOpenCardResult;

import java.lang.reflect.Type;

/**
 * Created by YuJian on 15/11/11.
 *
 * @author YuJian
 * @version 15/11/11
 */
public class WxOpenCardResultGsonAdapter implements JsonDeserializer<WxOpenCardResult> {
    @Override
    public WxOpenCardResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        WxOpenCardResult cardResult = new WxOpenCardResult();
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        cardResult.setOpenId(GsonHelper.getString(jsonObject, "openid"));
        cardResult.setErrorCode(GsonHelper.getString(jsonObject, "errcode"));
        cardResult.setErrorMsg(GsonHelper.getString(jsonObject, "errmsg"));
        cardResult.setCanConsume(GsonHelper.getBoolean(jsonObject, "can_consume"));
        cardResult.setUserCardStatus(GsonHelper.getString(jsonObject, "user_card_status"));

        WxOpenCard card = WxOpenGsonBuilder.INSTANCE.create().fromJson(jsonObject.get("card"),
                new TypeToken<WxOpenCard>() {
                }.getType());

        cardResult.setCard(card);

        return cardResult;
    }
}
