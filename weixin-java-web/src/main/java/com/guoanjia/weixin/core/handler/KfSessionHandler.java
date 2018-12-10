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

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Component
public class KfSessionHandler extends AbstractHandler {

    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage,
                                      Map<String, Object> context, WxOpenService wxOpenService,
                                      WxSessionManager sessionManager) {
        //TODO 对会话做处理
        return null;
    }

}
