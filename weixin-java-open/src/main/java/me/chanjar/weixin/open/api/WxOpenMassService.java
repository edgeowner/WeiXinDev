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

package me.chanjar.weixin.open.api;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.bean.*;
import me.chanjar.weixin.open.bean.mass.WxOpenMassMessageStatus;
import me.chanjar.weixin.open.bean.result.WxOpenMassSendResult;
import me.chanjar.weixin.open.bean.result.WxOpenMassUploadResult;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public interface WxOpenMassService {

    /**
     * <pre>
     * 上传群发用的图文消息，上传后才能群发图文消息
     *
     * 详情请见:${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140549&token=&lang=zh_CN}
     * </pre>
     *
     * @see #massGroupMessageSend(me.chanjar.weixin.open.bean.WxOpenMassTagMessage)
     * @see #massOpenIdsMessageSend(me.chanjar.weixin.open.bean.WxOpenMassOpenIdsMessage)
     */
    WxOpenMassUploadResult massNewsUpload(WxOpenMassNews news) throws WxErrorException;

    /**
     * <pre>
     * 上传群发用的视频，上传后才能群发视频消息
     * 详情请见:${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140549&token=&lang=zh_CN}
     * </pre>
     *
     * @see #massGroupMessageSend(me.chanjar.weixin.open.bean.WxOpenMassTagMessage)
     * @see #massOpenIdsMessageSend(me.chanjar.weixin.open.bean.WxOpenMassOpenIdsMessage)
     */
    WxOpenMassUploadResult massVideoUpload(WxOpenMassVideo video) throws WxErrorException;

    /**
     * <pre>
     * 分组群发消息
     * 如果发送图文消息，必须先使用 {@link #massNewsUpload(me.chanjar.weixin.open.bean.WxOpenMassNews)} 获得media_id，然后再发送
     * 如果发送视频消息，必须先使用 {@link #massVideoUpload(me.chanjar.weixin.open.bean.WxOpenMassVideo)} 获得media_id，然后再发送
     * 详情请见:${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140549&token=&lang=zh_CN}
     * </pre>
     */
    WxOpenMassSendResult massGroupMessageSend(WxOpenMassTagMessage message) throws WxErrorException;

    /**
     * <pre>
     * 按openId列表群发消息
     * 如果发送图文消息，必须先使用 {@link #massNewsUpload(me.chanjar.weixin.open.bean.WxOpenMassNews)} 获得media_id，然后再发送
     * 如果发送视频消息，必须先使用 {@link #massVideoUpload(me.chanjar.weixin.open.bean.WxOpenMassVideo)} 获得media_id，然后再发送
     * 详情请见:${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140549&token=&lang=zh_CN}
     * </pre>
     */
    WxOpenMassSendResult massOpenIdsMessageSend(WxOpenMassOpenIdsMessage message) throws WxErrorException;

    /**
     * <pre>
     * 群发消息预览接口
     * 开发者可通过该接口发送消息给指定用户，在手机端查看消息的样式和排版。为了满足第三方平台开发者的需求，在保留对openID预览能力的同时，增加了对指定微信号发送预览的能力，但该能力每日调用次数有限制（100次），请勿滥用。
     * 接口调用请求说明
     *  http请求方式: POST
     *  https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN
     * 详情请见：${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140549&token=&lang=zh_CN}
     * </pre>
     *
     * @return wxOpenMassSendResult
     */
    WxOpenMassSendResult massMessagePreview(WxOpenMassPreviewMessage wxOpenMassPreviewMessage) throws Exception;

    boolean massMessageDelete(String msgId) throws Exception;

    WxOpenMassMessageStatus massMessageStatus(String msgId) throws Exception;
}
