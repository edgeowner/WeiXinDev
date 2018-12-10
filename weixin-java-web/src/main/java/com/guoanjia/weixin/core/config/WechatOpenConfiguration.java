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

import com.guoanjia.weixin.core.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.WxOpenJedisConfigStorage;
import me.chanjar.weixin.open.api.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 开放平台初始化配置类
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@Configuration
@ConditionalOnClass(WxOpenService.class)
@EnableConfigurationProperties(WechatOpenProperties.class)
public class WechatOpenConfiguration {
    /**
     * 开放平台参数加载
     */
    private final WechatOpenProperties properties;

    ///////////////////////////////////////////////////////////////
    ////////////////      消息处理器列表注入      /////////////////
    ///////////////////////////////////////////////////////////////
    /**
     * 菜单处理器
     */
    private final MenuHandler menuHandler;

    /**
     * 生成海报按钮异步执行处理器
     */
    private final MenuCreatePosterHandler menuCreatePosterHandler;

    /**
     * 生成海报按钮事件处理器
     */
    private final MenuPosterHandler menuPosterHandler;

    /**
     * 地理位置事件处理器
     */
    private final LocationHandler locationHandler;

    /**
     * 默认消息拦截处理器
     */
    private final MsgHandler msgHandler;

    /**
     * 日志处理器
     */
    private final LogHandler logHandler;
    private final NullHandler nullHandler;

    /**
     * 客服处理器
     */
    private final KfSessionHandler kfSessionHandler;

    /**
     * 小店处理器
     */
    private final StoreCheckNotifyHandler storeCheckNotifyHandler;

    /**
     * 取消关注处理器
     */
    private final UnsubscribeHandler unsubscribeHandler;

    /**
     * 关注异步事件处理器
     */
    private final SubscribePosterHandler subscribePosterHandler;

    /**
     * 关注处理器
     */
    private final SubscribeHandler subscribeHandler;

    /**
     * 扫描二维码事件处理器
     */
    private final ScanHandler scanHandler;

    public WechatOpenConfiguration(WechatOpenProperties properties, MenuCreatePosterHandler menuCreatePosterHandler,
                                   MenuPosterHandler menuPosterHandler, LocationHandler locationHandler, MsgHandler msgHandler,
                                   MenuHandler menuHandler, LogHandler logHandler, NullHandler nullHandler,
                                   KfSessionHandler kfSessionHandler, StoreCheckNotifyHandler storeCheckNotifyHandler,
                                   UnsubscribeHandler unsubscribeHandler, SubscribePosterHandler subscribePosterHandler,
                                   SubscribeHandler subscribeHandler, ScanHandler scanHandler) {
        this.properties = properties;
        this.menuCreatePosterHandler = menuCreatePosterHandler;
        this.menuPosterHandler = menuPosterHandler;
        this.locationHandler = locationHandler;
        this.msgHandler = msgHandler;
        this.menuHandler = menuHandler;
        this.logHandler = logHandler;
        this.nullHandler = nullHandler;
        this.kfSessionHandler = kfSessionHandler;
        this.storeCheckNotifyHandler = storeCheckNotifyHandler;
        this.unsubscribeHandler = unsubscribeHandler;
        this.subscribePosterHandler = subscribePosterHandler;
        this.subscribeHandler = subscribeHandler;
        this.scanHandler = scanHandler;
    }

    /**
     * 加载必要配置参数
     *
     * @return 配置参数初始化
     */
    @Bean
    @ConditionalOnMissingBean
    public WxOpenConfigStorage configStorage() {
        WxOpenJedisConfigStorage configStorage = new WxOpenJedisConfigStorage(this.properties.getRedisHost(), this.properties.getRedisPort(), this.properties.getRedisAdmin());
        configStorage.setAppId(this.properties.getAppId());
        configStorage.setPartnerId(this.properties.getPartnerId());
        configStorage.setPartnerKey(this.properties.getPartnerKey());
        configStorage.setComponentAppId(this.properties.getComponentAppId());
        configStorage.setComponentAppSecret(this.properties.getComponentAppSecret());
        configStorage.setComponentEncodingAesKey(this.properties.getComponentEncodingAesKey());
        configStorage.setComponentToken(this.properties.getComponentToken());
        configStorage.setParentKeyPath(this.properties.getParentKeyPath());
        return configStorage;
    }

    /**
     * 微信服务设置配置参数
     *
     * @param configStorage 配置参数
     * @return 微信服务
     */
    @Bean
    @ConditionalOnMissingBean
    public WxOpenService wxOpenService(WxOpenConfigStorage configStorage) {
        WxOpenService wxOpenService = new WxOpenServiceImpl();
        wxOpenService.setWxOpenConfigStorage(configStorage);
        return wxOpenService;
    }

    /**
     * 微信消息路由规则表
     *
     * @param wxOpenService 微信服务
     * @return 微信路由
     */
    @Bean
    public WxOpenMessageRouter router(WxOpenService wxOpenService) {
        final WxOpenMessageRouter newRouter = new WxOpenMessageRouter(wxOpenService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE)
                .handler(this.subscribeHandler).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_UNSUBSCRIBE)
                .handler(this.unsubscribeHandler).end();

        newRouter.rule().async(false).handler(this.nullHandler).end();

//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_TEXT).content("buildMenu")
//                .handler(this.menuHandler).end();
//
//
//        // 接收客服会话管理事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_KF_CREATE_SESSION)
//                .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_KF_CLOSE_SESSION).handler(this.kfSessionHandler)
//                .end();
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_KF_SWITCH_SESSION)
//                .handler(this.kfSessionHandler).end();
//
//        // 门店审核事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_POI_CHECK_NOTIFY)
//                .handler(this.storeCheckNotifyHandler).end();
//
//        newRouter.rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE)
//                .handler(this.subscribePosterHandler)
//                .next();
//
//        // 关注事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE)
//                .handler(this.subscribeHandler)
//                .end();
//
//        // 取消关注事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_UNSUBSCRIBE)
//                .handler(this.unsubscribeHandler).end();
//
//        // 上报地理位置事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_LOCATION).handler(this.locationHandler)
//                .end();
//
//        // 接收地理位置消息
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_LOCATION)
//                .handler(this.locationHandler).end();
//
//        // 扫码事件
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.EVT_SCAN).handler(this.scanHandler).end();
//
//
//        //默认消息拦截器
//        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_TEXT).handler(this.menuHandler).end();
//
//
//        //-------------------------------------生成海报
//
//
//        // 海报生成按钮
//        newRouter.rule().async(true).msgType(WxConsts.XML_MSG_EVENT)
//                .event(WxConsts.BUTTON_CLICK).eventKey("show").handler(this.menuCreatePosterHandler).next();
//
//        // 海报生成事件
//        newRouter.rule().async(false).handler(this.menuPosterHandler).end();

        return newRouter;
    }


}
