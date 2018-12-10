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
public class WxOpenAuthorizationAccessToken implements Serializable {


    @SerializedName("authorizer_access_token")
    private String authorizerAccessToken;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("authorizer_refresh_token")
    private String authorizerRefreshToken;


    public static WxOpenAuthorizationAccessToken fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxOpenAuthorizationAccessToken.class);
    }

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public WxOpenAuthorizationAccessToken setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken == null ? "" : authorizerAccessToken;
        return this;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public WxOpenAuthorizationAccessToken setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    public WxOpenAuthorizationAccessToken setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken == null ? "" : authorizerRefreshToken;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
