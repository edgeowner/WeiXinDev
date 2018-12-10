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
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UnsubscribeHandler extends AbstractHandler {

    private final WxUserService wxUserService;
    private final WxUserSubscribeService wxUserSubscribeService;

    public UnsubscribeHandler(WxUserService wxUserService, WxUserSubscribeService wxUserSubscribeService) {
        this.wxUserService = wxUserService;
        this.wxUserSubscribeService = wxUserSubscribeService;
    }


    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage,
                                      Map<String, Object> context, WxOpenService wxOpenService,
                                      WxSessionManager sessionManager) throws WxErrorException {
        String openId = wxMessage.getFromUser();
        this.logger.info("取消关注用户 OPENID: " + openId);

        WxUserSubscribe wxUser = wxUserSubscribeService.findOne(Example.of(new WxUserSubscribe().setuOpenid(openId)));
        if (Objects.nonNull(wxUser)) {
            wxUserSubscribeService.delete(wxUser);
        }

        // TODO 可以更新本地数据库为取消关注状态
        return null;
    }

}
