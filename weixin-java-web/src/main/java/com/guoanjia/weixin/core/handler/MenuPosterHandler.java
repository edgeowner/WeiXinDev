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

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.utils.ObjectUtil;
import com.guoanjia.business.base.entity.WxActivity;
import com.guoanjia.business.poster.entity.WxUserClick;
import com.guoanjia.business.base.service.WxActivityService;
import com.guoanjia.business.poster.service.WxUserClickService;
import com.guoanjia.weixin.core.builder.TextBuilder;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MenuPosterHandler extends AbstractHandler {

    private final WxUserClickService wxUserClickService;

    private final WxActivityService wxActivityService;

    public MenuPosterHandler(WxUserClickService wxUserClickService, WxActivityService wxActivityService) {
        this.wxUserClickService = wxUserClickService;
        this.wxActivityService = wxActivityService;
    }


    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService,
                                      WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("用户点击海报生成按钮: " + wxMessage.getFromUser());

        // TODO 活动结束条件需明确
        AjaxJson activityJSON = wxActivityService.checkActivityByCondition(new WxActivity().setActStartTime(new Date()));
        if (activityJSON.isSuccess()) {
            Criteria<WxUserClick> criteria = new Criteria<>();
            criteria.add(Restrictions.eq("ucOpenid", wxMessage.getFromUser(), true));
            WxUserClick wxUserClick = wxUserClickService.findOne(criteria);
            if (ObjectUtil.isNotEmpty(wxUserClick)) {
                if (wxUserClick.getUcClickType().equals("00") && wxUserClick.getUcCount() >= 5) {
                    return new TextBuilder().build("今日生成次数已用尽", wxMessage, wxOpenService);
                } else {
                    if (wxUserClick.getUcClickCount() == 0) {
                        return new TextBuilder().build("亲,正在为您生成专属海报,大约需要5秒时间...", wxMessage, wxOpenService);
                    } else {
                        return new TextBuilder().build("不要频繁点击", wxMessage, wxOpenService);
                    }
                }
            } else {
                return new TextBuilder().build("亲,正在为您生成专属海报,大约需要5秒时间...", wxMessage, wxOpenService);
            }
        } else {
            return new TextBuilder().build("活动已结束", wxMessage, wxOpenService);
        }
    }

}
