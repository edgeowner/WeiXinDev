/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.chanjar.weixin.open.api.impl;

import com.google.gson.JsonObject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenMassService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.*;
import me.chanjar.weixin.open.bean.mass.WxOpenMassMessageStatus;
import me.chanjar.weixin.open.bean.result.WxOpenMassSendResult;
import me.chanjar.weixin.open.bean.result.WxOpenMassUploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class WxOpenMassServiceImpl implements WxOpenMassService {

    private static final String API_URL_PREFIX_WITH_CGI_BIN = "https://api.weixin.qq.com/cgi-bin/message/mass";
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxOpenService wxOpenService;

    public WxOpenMassServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public WxOpenMassUploadResult massNewsUpload(WxOpenMassNews news) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
        String responseContent = this.wxOpenService.post(url, news.toJson());
        return WxOpenMassUploadResult.fromJson(responseContent);
    }

    @Override
    public WxOpenMassUploadResult massVideoUpload(WxOpenMassVideo video) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/media/uploadvideo";
        String responseContent = this.wxOpenService.post(url, video.toJson());
        return WxOpenMassUploadResult.fromJson(responseContent);
    }

    @Override
    public WxOpenMassSendResult massGroupMessageSend(WxOpenMassTagMessage message) throws WxErrorException {
        String url = API_URL_PREFIX_WITH_CGI_BIN + "/sendall";
        String responseContent = this.wxOpenService.post(url, message.toJson());
        return WxOpenMassSendResult.fromJson(responseContent);
    }

    @Override
    public WxOpenMassSendResult massOpenIdsMessageSend(WxOpenMassOpenIdsMessage message) throws WxErrorException {
        String url = API_URL_PREFIX_WITH_CGI_BIN + "/send";
        String responseContent = this.wxOpenService.post(url, message.toJson());
        return WxOpenMassSendResult.fromJson(responseContent);
    }

    @Override
    public WxOpenMassSendResult massMessagePreview(WxOpenMassPreviewMessage wxOpenMassPreviewMessage) throws Exception {
        String url = API_URL_PREFIX_WITH_CGI_BIN + "/preview";
        String responseContent = this.wxOpenService.post(url, wxOpenMassPreviewMessage.toJson());
        return WxOpenMassSendResult.fromJson(responseContent);
    }

    @Override
    public boolean massMessageDelete(String msgId) throws Exception {
        String url = API_URL_PREFIX_WITH_CGI_BIN + "/delete";
        JsonObject o = new JsonObject();
        o.addProperty("msg_id", msgId);
        String responseContent = this.wxOpenService.post(url, o.toString());
        return responseContent != null;
    }

    @Override
    public WxOpenMassMessageStatus massMessageStatus(String msgId) throws Exception {
        String url = API_URL_PREFIX_WITH_CGI_BIN + "/get";
        JsonObject o = new JsonObject();
        o.addProperty("msg_id", msgId);
        String responseContent = this.wxOpenService.post(url, o.toString());
        return WxOpenMassMessageStatus.fromJson(responseContent);
    }


}
