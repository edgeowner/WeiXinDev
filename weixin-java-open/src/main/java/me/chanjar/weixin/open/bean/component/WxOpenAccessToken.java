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

import java.io.Serializable;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class WxOpenAccessToken implements Serializable {

    @SerializedName("component_access_token")
    private String componentAccessToken;

    @SerializedName("expires_in")
    private int expiresIn = -1;

    public static WxOpenAccessToken fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxOpenAccessToken.class);
    }

    public String getComponentAccessToken() {
        return componentAccessToken;
    }

    public WxOpenAccessToken setComponentAccessToken(String componentAccessToken) {
        this.componentAccessToken = componentAccessToken == null ? "" : componentAccessToken;
        return this;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public WxOpenAccessToken setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
