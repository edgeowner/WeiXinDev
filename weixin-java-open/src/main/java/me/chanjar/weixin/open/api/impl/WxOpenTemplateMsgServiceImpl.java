package me.chanjar.weixin.open.api.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxOpenTemplateMsgService;
import me.chanjar.weixin.open.bean.template.WxOpenTemplate;
import me.chanjar.weixin.open.bean.template.WxOpenTemplateIndustry;
import me.chanjar.weixin.open.bean.template.WxOpenTemplateMessage;

import java.util.List;

/**
 * <pre>
 * Created by Binary Wang on 2016-10-14.
 * @author <a href="https://github.com/binarywang">binarywang(Binary Wang)</a>
 * </pre>
 */
public class WxOpenTemplateMsgServiceImpl implements WxOpenTemplateMsgService {
    public static final String API_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/template";
    private static final JsonParser JSON_PARSER = new JsonParser();

    private WxOpenService wxOpenService;

    public WxOpenTemplateMsgServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public String sendTemplateMsg(WxOpenTemplateMessage templateMessage) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send";
        String responseContent = this.wxOpenService.post(url, templateMessage.toJson());
        final JsonObject jsonObject = JSON_PARSER.parse(responseContent).getAsJsonObject();
        if (jsonObject.get("errcode").getAsInt() == 0) {
            return jsonObject.get("msgid").getAsString();
        }
        throw new WxErrorException(WxError.fromJson(responseContent));
    }

    @Override
    public boolean setIndustry(WxOpenTemplateIndustry wxOpenIndustry) throws WxErrorException {
        if (null == wxOpenIndustry.getPrimaryIndustry() || null == wxOpenIndustry.getPrimaryIndustry().getId()
                || null == wxOpenIndustry.getSecondIndustry() || null == wxOpenIndustry.getSecondIndustry().getId()) {
            throw new IllegalArgumentException("行业Id不能为空，请核实");
        }

        String url = API_URL_PREFIX + "/api_set_industry";
        this.wxOpenService.post(url, wxOpenIndustry.toJson());
        return true;
    }

    @Override
    public WxOpenTemplateIndustry getIndustry() throws WxErrorException {
        String url = API_URL_PREFIX + "/get_industry";
        String responseContent = this.wxOpenService.get(url, null);
        return WxOpenTemplateIndustry.fromJson(responseContent);
    }

    @Override
    public String addTemplate(String shortTemplateId) throws WxErrorException {
        String url = API_URL_PREFIX + "/api_add_template";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("template_id_short", shortTemplateId);
        String responseContent = this.wxOpenService.post(url, jsonObject.toString());
        final JsonObject result = JSON_PARSER.parse(responseContent).getAsJsonObject();
        if (result.get("errcode").getAsInt() == 0) {
            return result.get("template_id").getAsString();
        }

        throw new WxErrorException(WxError.fromJson(responseContent));
    }

    @Override
    public List<WxOpenTemplate> getAllPrivateTemplate() throws WxErrorException {
        String url = API_URL_PREFIX + "/get_all_private_template";
        return WxOpenTemplate.fromJson(this.wxOpenService.get(url, null));
    }

    @Override
    public boolean delPrivateTemplate(String templateId) throws WxErrorException {
        String url = API_URL_PREFIX + "/del_private_template";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("template_id", templateId);
        String responseContent = this.wxOpenService.post(url, jsonObject.toString());
        WxError error = WxError.fromJson(responseContent);
        if (error.getErrorCode() == 0) {
            return true;
        }

        throw new WxErrorException(error);
    }

}
