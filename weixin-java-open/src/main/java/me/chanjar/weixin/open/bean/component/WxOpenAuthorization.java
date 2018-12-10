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
import java.util.List;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class WxOpenAuthorization implements Serializable {

    @SerializedName("authorization_info")
    private WxOpenAuthorizationInfo authorizationInfo;

    public static WxOpenAuthorization fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxOpenAuthorization.class);
    }

    public WxOpenAuthorizationInfo getAuthorizationInfo() {
        return authorizationInfo;
    }

    public WxOpenAuthorization setAuthorizationInfo(WxOpenAuthorizationInfo authorizationInfo) {
        this.authorizationInfo = authorizationInfo;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static class WxOpenAuthorizationInfo implements Serializable {

        @SerializedName("authorizer_appid")
        private String authorizerAppid;
        @SerializedName("authorizer_access_token")
        private String authorizerAccessToken;
        @SerializedName("expires_in")
        private int expiresIn;
        @SerializedName("authorizer_refresh_token")
        private String authorizerRefreshToken;
        @SerializedName("func_info")
        private List<FuncInfo> funcInfo;

        public static WxOpenAuthorizationInfo fromJson(String json) {
            return WxGsonBuilder.create().fromJson(json, WxOpenAuthorizationInfo.class);
        }

        public String getAuthorizerAppid() {
            return authorizerAppid;
        }

        public WxOpenAuthorizationInfo setAuthorizerAppid(String authorizerAppid) {
            this.authorizerAppid = authorizerAppid == null ? "" : authorizerAppid;
            return this;
        }

        public String getAuthorizerAccessToken() {
            return authorizerAccessToken;
        }

        public WxOpenAuthorizationInfo setAuthorizerAccessToken(String authorizerAccessToken) {
            this.authorizerAccessToken = authorizerAccessToken == null ? "" : authorizerAccessToken;
            return this;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public WxOpenAuthorizationInfo setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public String getAuthorizerRefreshToken() {
            return authorizerRefreshToken;
        }

        public WxOpenAuthorizationInfo setAuthorizerRefreshToken(String authorizerRefreshToken) {
            this.authorizerRefreshToken = authorizerRefreshToken == null ? "" : authorizerRefreshToken;
            return this;
        }

        public List<FuncInfo> getFuncInfo() {
            return funcInfo;
        }

        public WxOpenAuthorizationInfo setFuncInfo(List<FuncInfo> funcInfo) {
            this.funcInfo = funcInfo;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }

        public static class FuncInfo implements Serializable {
            @SerializedName("funcscope_category")
            private FuncscopeCategory funcscopeCategory;


            public FuncscopeCategory getFuncscopeCategory() {
                return funcscopeCategory;
            }

            public FuncInfo setFuncscopeCategory(FuncscopeCategory funcscopeCategory) {
                this.funcscopeCategory = funcscopeCategory;
                return this;
            }

            @Override
            public String toString() {
                return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
            }

            public static class FuncscopeCategory {
                @SerializedName("id")
                private int id;

                public int getId() {
                    return id;
                }

                public FuncscopeCategory setId(int id) {
                    this.id = id;
                    return this;
                }
            }
        }
    }
}
