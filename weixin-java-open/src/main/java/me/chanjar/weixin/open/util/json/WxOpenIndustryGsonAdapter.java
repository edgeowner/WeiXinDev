package me.chanjar.weixin.open.util.json;

import com.google.gson.*;
import me.chanjar.weixin.common.util.json.GsonHelper;
import me.chanjar.weixin.open.bean.template.WxOpenTemplateIndustry;

import java.lang.reflect.Type;

/**
 * @author miller
 */
public class WxOpenIndustryGsonAdapter
        implements JsonSerializer<WxOpenTemplateIndustry>, JsonDeserializer<WxOpenTemplateIndustry> {
    private static WxOpenTemplateIndustry.Industry convertFromJson(JsonObject json) {
        WxOpenTemplateIndustry.Industry industry = new WxOpenTemplateIndustry.Industry();
        industry.setFirstClass(GsonHelper.getString(json, "first_class"));
        industry.setSecondClass(GsonHelper.getString(json, "second_class"));
        return industry;
    }

    @Override
    public JsonElement serialize(WxOpenTemplateIndustry wxOpenIndustry, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject json = new JsonObject();
        json.addProperty("industry_id1", wxOpenIndustry.getPrimaryIndustry().getId());
        json.addProperty("industry_id2", wxOpenIndustry.getSecondIndustry().getId());
        return json;
    }

    @Override
    public WxOpenTemplateIndustry deserialize(JsonElement jsonElement, Type type,
                                              JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        WxOpenTemplateIndustry wxOpenIndustry = new WxOpenTemplateIndustry();
        JsonObject primaryIndustry = jsonElement.getAsJsonObject()
                .get("primary_industry").getAsJsonObject();
        wxOpenIndustry.setPrimaryIndustry(convertFromJson(primaryIndustry));
        JsonObject secondaryIndustry = jsonElement.getAsJsonObject()
                .get("secondary_industry").getAsJsonObject();
        wxOpenIndustry.setSecondIndustry(convertFromJson(secondaryIndustry));
        return wxOpenIndustry;
    }
}
