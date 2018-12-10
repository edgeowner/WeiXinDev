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

package com.guoanjia.weixin.core.handler;

import com.guoanjia.business.base.entity.WxUser;
import com.guoanjia.business.base.entity.WxUserSubscribe;
import com.guoanjia.business.base.service.WxUserService;
import com.guoanjia.business.base.service.WxUserSubscribeService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxOpenUserService;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import me.chanjar.weixin.open.bean.result.WxOpenUser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SubscribeHandler extends AbstractHandler {

    private final WxUserService wxUserService;
    private final WxUserSubscribeService wxUserSubscribeService;

    public SubscribeHandler(WxUserService wxUserService, WxUserSubscribeService wxUserSubscribeService) {
        this.wxUserService = wxUserService;
        this.wxUserSubscribeService = wxUserSubscribeService;
    }

    /**
     * 用户关注处理事件
     *
     * @param wxMessage      微信消息体
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param wxOpenService  微信底层服务
     * @param sessionManager 微信Session服务
     * @return 微信欢迎信息
     * @throws WxErrorException
     */
    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService, WxSessionManager sessionManager) throws WxErrorException {
        String openId = wxMessage.getFromUser();
        this.logger.info("新关注用户 OPENID: " + openId);
        WxOpenUserService wxOpenUserService = wxOpenService.getUserService();
        WxOpenUser wxOpenUser = wxOpenUserService.userInfo(wxMessage.getFromUser());

        if (!wxUserService.exists(Example.of(new WxUser().setuOpenid(openId)))) {
            wxUserService.save(new WxUser().
                    setuCreateTime(new Date())
                    .setuOpenid(wxOpenUser.getOpenId())
                    .setuNickname(wxOpenUser.getNickname())
                    .setuProvince(wxOpenUser.getProvince())
                    .setuCity(wxOpenUser.getCity())
                    .setuCountry(wxOpenUser.getCountry())
                    .setuHeadimg(wxOpenUser.getHeadImgUrl())
                    .setuSex(wxOpenUser.getSex())
                    .setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                    .setuUnionid(wxOpenUser.getUnionId()));
        }

        if (!wxUserSubscribeService.exists(Example.of(new WxUserSubscribe().setuOpenid(openId)))) {
            wxUserSubscribeService.save(new WxUserSubscribe().
                    setuCreateTime(new Date())
                    .setuOpenid(wxOpenUser.getOpenId())
                    .setuNickname(wxOpenUser.getNickname())
                    .setuProvince(wxOpenUser.getProvince())
                    .setuCity(wxOpenUser.getCity())
                    .setuCountry(wxOpenUser.getCountry())
                    .setuHeadimg(wxOpenUser.getHeadImgUrl())
                    .setuSex(wxOpenUser.getSex())
                    .setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                    .setuUnionid(wxOpenUser.getUnionId()));
        }


        return null;


    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private String handleSpecial(WxOpenXmlMessage wxMessage) throws Exception {
        return "";
    }


}
