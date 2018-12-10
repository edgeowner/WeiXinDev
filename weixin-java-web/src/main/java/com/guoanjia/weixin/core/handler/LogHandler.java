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

import com.guoanjia.weixin.core.utils.JsonUtils;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 微信信息日志处理器
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@Component
public class LogHandler extends AbstractHandler {

    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage,
                                      Map<String, Object> context, WxOpenService wxOpenService,
                                      WxSessionManager sessionManager) {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("\n接收到请求消息，内容：{}", JsonUtils.toJson(wxMessage));
        }

        return null;
    }

}