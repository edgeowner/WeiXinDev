package me.chanjar.weixin.open.api.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.SimplePostRequestExecutor;
import me.chanjar.weixin.open.api.WxOpenQrcodeService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.result.WxOpenQrCodeTicket;
import me.chanjar.weixin.open.util.http.QrCodeRequestExecutor;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by Binary Wang on 2016/7/21.
 */
public class WxOpenQrcodeServiceImpl implements WxOpenQrcodeService {

    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final String API_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/qrcode";
    private WxOpenService wxOpenService;

    public WxOpenQrcodeServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public WxOpenQrCodeTicket qrCodeCreateTmpTicket(int scene_id, Integer expire_seconds) throws WxErrorException {
        String url = API_URL_PREFIX + "/create";
        JsonObject json = new JsonObject();
        json.addProperty("action_name", "QR_SCENE");
        if (expire_seconds != null) {
            json.addProperty("expire_seconds", expire_seconds);
        }
        JsonObject actionInfo = new JsonObject();
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_id", scene_id);
        actionInfo.add("scene", scene);
        json.add("action_info", actionInfo);
        String responseContent = this.wxOpenService.execute(new SimplePostRequestExecutor(), url, json.toString());
        return WxOpenQrCodeTicket.fromJson(responseContent);
    }

    @Override
    public WxOpenQrCodeTicket qrCodeCreateLastTicket(int scene_id) throws WxErrorException {
        String url = API_URL_PREFIX + "/create";
        JsonObject json = new JsonObject();
        json.addProperty("action_name", "QR_LIMIT_SCENE");
        JsonObject actionInfo = new JsonObject();
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_id", scene_id);
        actionInfo.add("scene", scene);
        json.add("action_info", actionInfo);
        String responseContent = this.wxOpenService.execute(new SimplePostRequestExecutor(), url, json.toString());
        return WxOpenQrCodeTicket.fromJson(responseContent);
    }

    @Override
    public WxOpenQrCodeTicket qrCodeCreateLastTicket(String scene_str) throws WxErrorException {
        String url = API_URL_PREFIX + "/create";
        JsonObject json = new JsonObject();
        json.addProperty("action_name", "QR_LIMIT_STR_SCENE");
        JsonObject actionInfo = new JsonObject();
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_str", scene_str);
        actionInfo.add("scene", scene);
        json.add("action_info", actionInfo);
        String responseContent = this.wxOpenService.execute(new SimplePostRequestExecutor(), url, json.toString());
        return WxOpenQrCodeTicket.fromJson(responseContent);
    }

    @Override
    public File qrCodePicture(WxOpenQrCodeTicket ticket) throws WxErrorException {
        String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
        return this.wxOpenService.execute(new QrCodeRequestExecutor(), url, ticket);
    }

    @Override
    public String qrCodePictureUrl(String ticket, boolean needShortUrl) throws WxErrorException {
        String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
        try {
            String resultUrl = String.format(url,
                    URLEncoder.encode(ticket, StandardCharsets.UTF_8.name()));
            if (needShortUrl) {
                return this.shortUrl(resultUrl);
            }

            return resultUrl;
        } catch (UnsupportedEncodingException e) {
            WxError error = WxError.newBuilder().setErrorCode(-1)
                    .setErrorMsg(e.getMessage()).build();
            throw new WxErrorException(error);
        }
    }

    @Override
    public String qrCodePictureUrl(String ticket) throws WxErrorException {
        return qrCodePictureUrl(ticket, false);
    }

    //TODO 待改进
    @Override
    public String shortUrl(String long_url) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/shorturl";
        JsonObject o = new JsonObject();
        o.addProperty("action", "long2short");
        o.addProperty("long_url", long_url);
        String responseContent = this.wxOpenService.post(url, o.toString());
        JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
        return tmpJsonElement.getAsJsonObject().get("short_url").getAsString();
    }

}
