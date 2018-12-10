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

package me.chanjar.weixin.open.bean.component;

import com.google.gson.annotations.SerializedName;
import me.chanjar.weixin.common.util.json.WxGsonBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class WxOpenPreAuthCode implements Serializable {
    @SerializedName("pre_auth_code")
    private String preAuthCode;
    @SerializedName("expires_in")
    private int expiresIn = -1;

    public static WxOpenPreAuthCode fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxOpenPreAuthCode.class);
    }

    public String getPreAuthCode() {
        return preAuthCode;
    }

    public WxOpenPreAuthCode setPreAuthCode(String preAuthCode) {
        this.preAuthCode = preAuthCode == null ? "" : preAuthCode;
        return this;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public WxOpenPreAuthCode setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}