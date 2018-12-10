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

package me.chanjar.weixin.open.bean.mass;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.json.WxGsonBuilder;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class WxOpenMassMessageStatus implements Serializable {

    @SerializedName(value = "msg_id")
    private int msgId;

    @SerializedName(value = "msg_status")
    private String msgStatus;

    public static WxOpenMassMessageStatus fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxOpenMassMessageStatus.class);
    }

    public int getMsgId() {
        return msgId;
    }

    public WxOpenMassMessageStatus setMsgId(int msgId) {
        this.msgId = msgId;
        return this;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public WxOpenMassMessageStatus setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus == null ? "" : msgStatus;
        return this;
    }

    public String toJson() {
        return WxOpenGsonBuilder.INSTANCE.create().toJson(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
