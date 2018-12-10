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

package me.chanjar.weixin.open.api.impl;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenIntelligentService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.WxOpenSemanticQuery;
import me.chanjar.weixin.open.bean.result.WxOpenSemanticQueryResult;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class WxOpenIntelligentServiceImpl implements WxOpenIntelligentService {

    private final WxOpenService wxOpenService;

    public WxOpenIntelligentServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public WxOpenSemanticQueryResult semanticQuery(WxOpenSemanticQuery semanticQuery) throws WxErrorException {
        String url = "https://api.weixin.qq.com/semantic/semproxy/search";
        String responseContent = this.wxOpenService.post(url, semanticQuery.toJson());
        return WxOpenSemanticQueryResult.fromJson(responseContent);
    }

}
