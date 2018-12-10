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
public class WxOpenAuthorizerInfo implements Serializable {

    //账户信息
    @SerializedName("authorizer_info")
    private AuthorizerInfo authorizerInfo;

    //授权信息
    @SerializedName("authorization_info")
    private AuthorizationInfo authorizationInfo;

    public static WxOpenAuthorizerInfo fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxOpenAuthorizerInfo.class);
    }

    public AuthorizerInfo getAuthorizerInfo() {
        return authorizerInfo;
    }

    public WxOpenAuthorizerInfo setAuthorizerInfo(AuthorizerInfo authorizerInfo) {
        this.authorizerInfo = authorizerInfo;
        return this;
    }

    public AuthorizationInfo getAuthorizationInfo() {
        return authorizationInfo;
    }

    public WxOpenAuthorizerInfo setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
        this.authorizationInfo = authorizationInfo;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static class AuthorizerInfo implements Serializable {

        //授权方昵称
        @SerializedName("nick_name")
        private String nickName;

        //授权方头像
        @SerializedName("head_img")
        private String headImg;

        //授权方公众号类型
        // 0代表订阅号，
        // 1代表由历史老帐号升级后的订阅号，
        // 2代表服务号
        @SerializedName("service_type_info")
        private ServiceTypeInfo serviceTypeInfo;

        //授权方认证类型，
        // -1代表未认证，
        // 0代表微信认证，
        // 1代表新浪微博认证，
        // 2代表腾讯微博认证，
        // 3代表已资质认证通过但还未通过名称认证，
        // 4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，
        // 5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
        @SerializedName("verify_type_info")
        private VerifyTypeInfo verifyTypeInfo;

        //授权方公众号的原始ID
        @SerializedName("user_name")
        private String userName;

        //公众号的主体名称
        @SerializedName("principal_name")
        private String principalName;

        //用以了解以下功能的开通状况（0代表未开通，1代表已开通）：
        //open_store:是否开通微信门店功能
        //open_scan:是否开通微信扫商品功能
        //open_pay:是否开通微信支付功能
        //open_card:是否开通微信卡券功能
        //open_shake:是否开通微信摇一摇功能
        @SerializedName("business_info")
        private BusinessInfo businessInfo;

        //授权方公众号所设置的微信号，可能为空
        @SerializedName("alias")
        private String alias;

        //二维码图片的URL
        @SerializedName("qrcode_url")
        private String qrcodeUrl;

        public static AuthorizerInfo fromJson(String json) {
            return WxGsonBuilder.create().fromJson(json, AuthorizerInfo.class);
        }

        public String getNickName() {
            return nickName;
        }

        public AuthorizerInfo setNickName(String nickName) {
            this.nickName = nickName == null ? "" : nickName;
            return this;
        }

        public String getHeadImg() {
            return headImg;
        }

        public AuthorizerInfo setHeadImg(String headImg) {
            this.headImg = headImg == null ? "" : headImg;
            return this;
        }

        public ServiceTypeInfo getServiceTypeInfo() {
            return serviceTypeInfo;
        }

        public AuthorizerInfo setServiceTypeInfo(ServiceTypeInfo serviceTypeInfo) {
            this.serviceTypeInfo = serviceTypeInfo;
            return this;
        }

        public VerifyTypeInfo getVerifyTypeInfo() {
            return verifyTypeInfo;
        }

        public AuthorizerInfo setVerifyTypeInfo(VerifyTypeInfo verifyTypeInfo) {
            this.verifyTypeInfo = verifyTypeInfo;
            return this;
        }

        public String getUserName() {
            return userName;
        }

        public AuthorizerInfo setUserName(String userName) {
            this.userName = userName == null ? "" : userName;
            return this;
        }

        public String getPrincipalName() {
            return principalName;
        }

        public AuthorizerInfo setPrincipalName(String principalName) {
            this.principalName = principalName == null ? "" : principalName;
            return this;
        }

        public BusinessInfo getBusinessInfo() {
            return businessInfo;
        }

        public AuthorizerInfo setBusinessInfo(BusinessInfo businessInfo) {
            this.businessInfo = businessInfo;
            return this;
        }

        public String getAlias() {
            return alias;
        }

        public AuthorizerInfo setAlias(String alias) {
            this.alias = alias == null ? "" : alias;
            return this;
        }

        public String getQrcodeUrl() {
            return qrcodeUrl;
        }

        public AuthorizerInfo setQrcodeUrl(String qrcodeUrl) {
            this.qrcodeUrl = qrcodeUrl == null ? "" : qrcodeUrl;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }

        public static class ServiceTypeInfo implements Serializable {
            @SerializedName("id")
            private int id;

            public static ServiceTypeInfo fromJson(String json) {
                return WxGsonBuilder.create().fromJson(json, ServiceTypeInfo.class);
            }

            public int getId() {
                return id;
            }

            public ServiceTypeInfo setId(int id) {
                this.id = id;
                return this;
            }

            @Override
            public String toString() {
                return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
            }
        }

        public static class VerifyTypeInfo implements Serializable {
            @SerializedName("id")
            private int id;

            public static VerifyTypeInfo fromJson(String json) {
                return WxGsonBuilder.create().fromJson(json, VerifyTypeInfo.class);
            }

            public int getId() {
                return id;
            }

            public VerifyTypeInfo setId(int id) {
                this.id = id;
                return this;
            }

            @Override
            public String toString() {
                return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
            }
        }

        public static class BusinessInfo implements Serializable {

            @SerializedName("open_store")
            private int openStore;

            @SerializedName("open_scan")
            private int openScan;

            @SerializedName("openPay")
            private int openPay;

            @SerializedName("openCard")
            private int openCard;

            @SerializedName("openShake")
            private int openShake;

            public static BusinessInfo fromJson(String json) {
                return WxGsonBuilder.create().fromJson(json, BusinessInfo.class);
            }

            public int getOpenStore() {
                return openStore;
            }

            public BusinessInfo setOpenStore(int openStore) {
                this.openStore = openStore;
                return this;
            }

            public int getOpenScan() {
                return openScan;
            }

            public BusinessInfo setOpenScan(int openScan) {
                this.openScan = openScan;
                return this;
            }

            public int getOpenPay() {
                return openPay;
            }

            public BusinessInfo setOpenPay(int openPay) {
                this.openPay = openPay;
                return this;
            }

            public int getOpenCard() {
                return openCard;
            }

            public BusinessInfo setOpenCard(int openCard) {
                this.openCard = openCard;
                return this;
            }

            public int getOpenShake() {
                return openShake;
            }

            public BusinessInfo setOpenShake(int openShake) {
                this.openShake = openShake;
                return this;
            }

            @Override
            public String toString() {
                return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
            }
        }
    }

    public static class AuthorizationInfo implements Serializable {

        //授权方appid
        @SerializedName("appid")
        private String appid;

        //公众号授权给开发者的权限集列表，ID为1到15时分别代表：
        //消息管理权限
        //用户管理权限
        //帐号服务权限
        //网页服务权限
        //微信小店权限
        //微信多客服权限
        //群发与通知权限
        //微信卡券权限
        //微信扫一扫权限
        //微信连WIFI权限
        //素材管理权限
        //微信摇周边权限
        //微信门店权限
        //微信支付权限
        //自定义菜单权限
        @SerializedName("func_info")
        private List<FuncInfo> funcInfo;

        public static AuthorizationInfo fromJson(String json) {
            return WxGsonBuilder.create().fromJson(json, AuthorizationInfo.class);
        }

        public String getAppid() {
            return appid;
        }

        public AuthorizationInfo setAppid(String appid) {
            this.appid = appid == null ? "" : appid;
            return this;
        }

        public List<FuncInfo> getFuncInfo() {
            return funcInfo;
        }

        public AuthorizationInfo setFuncInfo(List<FuncInfo> funcInfo) {
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
