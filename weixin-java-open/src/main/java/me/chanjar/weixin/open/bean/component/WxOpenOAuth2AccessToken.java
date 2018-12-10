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

import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class WxOpenOAuth2AccessToken implements Serializable {

    private static final long serialVersionUID = -1345910558078620805L;

    private String accessToken;

    private int expiresIn = -1;

    private String refreshToken;

    private String openId;

    private String scope;

    private String unionId;

    public static WxOpenOAuth2AccessToken fromJson(String json) {
        return WxOpenGsonBuilder.create().fromJson(json, WxOpenOAuth2AccessToken.class);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public WxOpenOAuth2AccessToken setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? "" : accessToken;
        return this;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public WxOpenOAuth2AccessToken setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public WxOpenOAuth2AccessToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? "" : refreshToken;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public WxOpenOAuth2AccessToken setOpenId(String openId) {
        this.openId = openId == null ? "" : openId;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public WxOpenOAuth2AccessToken setScope(String scope) {
        this.scope = scope == null ? "" : scope;
        return this;
    }

    public String getUnionId() {
        return unionId;
    }

    public WxOpenOAuth2AccessToken setUnionId(String unionId) {
        this.unionId = unionId == null ? "" : unionId;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
