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

package com.guoanjia.weixin.controller;

import me.chanjar.weixin.open.api.WxOpenPayService;
import me.chanjar.weixin.open.api.WxOpenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */

@RestController
@RequestMapping(path = {"/pay"})
public class WechatOpenPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final WxOpenService wxOpenService;

    private final WxOpenPayService wxOpenPayService;

    public WechatOpenPayController(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
        this.wxOpenPayService = wxOpenService.getPayService();
    }


}
