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

package com.guoanjia.weixin.core.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信配置参数加载类
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@ConfigurationProperties(prefix = "wechat.open")
public class WechatOpenProperties {

    /**
     * 设置微信公众号的appid
     */
    private String appId;

    /**
     * 微信支付partner id
     */
    private String partnerId;

    /**
     * 微信支付partner key
     */
    private String partnerKey;

    /**
     * 开放平台APPID
     */
    private String componentAppId;

    /**
     * 开放平台Secret
     */
    private String componentAppSecret;

    /**
     * 开放平台EncodingAesKey
     */
    private String componentEncodingAesKey;

    /**
     * 开放平台Token
     */
    private String componentToken;

    /**
     * Redis服务器P地址
     */
    private String redisHost;

    /**
     * Redis服务器端口
     */
    private int redisPort;

    /**
     * redis密码
     */
    private String redisAdmin;

    private String parentKeyPath;


    public String getAppId() {
        return appId;
    }

    public WechatOpenProperties setAppId(String appId) {
        this.appId = appId == null ? "" : appId;
        return this;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public WechatOpenProperties setPartnerId(String partnerId) {
        this.partnerId = partnerId == null ? "" : partnerId;
        return this;
    }

    public String getPartnerKey() {
        return partnerKey;
    }

    public WechatOpenProperties setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey == null ? "" : partnerKey;
        return this;
    }

    public String getComponentAppId() {
        return componentAppId;
    }

    public WechatOpenProperties setComponentAppId(String componentAppId) {
        this.componentAppId = componentAppId == null ? "" : componentAppId;
        return this;
    }

    public String getComponentAppSecret() {
        return componentAppSecret;
    }

    public WechatOpenProperties setComponentAppSecret(String componentAppSecret) {
        this.componentAppSecret = componentAppSecret == null ? "" : componentAppSecret;
        return this;
    }

    public String getComponentEncodingAesKey() {
        return componentEncodingAesKey;
    }

    public WechatOpenProperties setComponentEncodingAesKey(String componentEncodingAesKey) {
        this.componentEncodingAesKey = componentEncodingAesKey == null ? "" : componentEncodingAesKey;
        return this;
    }

    public String getComponentToken() {
        return componentToken;
    }

    public WechatOpenProperties setComponentToken(String componentToken) {
        this.componentToken = componentToken == null ? "" : componentToken;
        return this;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public WechatOpenProperties setRedisHost(String redisHost) {
        this.redisHost = redisHost;
        return this;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public WechatOpenProperties setRedisPort(int redisPort) {
        this.redisPort = redisPort;
        return this;
    }

    public String getRedisAdmin() {
        return redisAdmin;
    }

    public WechatOpenProperties setRedisAdmin(String redisAdmin) {
        this.redisAdmin = redisAdmin;
        return this;
    }

    public String getParentKeyPath() {
        return parentKeyPath;
    }

    public WechatOpenProperties setParentKeyPath(String parentKeyPath) {
        this.parentKeyPath = parentKeyPath;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
