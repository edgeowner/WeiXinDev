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

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@RestController
@RequestMapping(path = {"/jsapi"})
public class WechatOpenJsApiController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final WxOpenService wxOpenService;

    public WechatOpenJsApiController(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }


    /**
     * 获取JSSDK 参数
     *
     * @param local_url 页面地址
     * @return JSSDK参数
     * @throws WxErrorException 微信获取异常时抛出此异常
     */
    @RequestMapping(path = {"/getJsapiSignature"})
    public WxJsapiSignature getJsapiSignature(@RequestParam String local_url) throws WxErrorException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n获取JSSDK 参数请求:[local_url:{}]", local_url);
        }
        try {
            return this.wxOpenService.createJsapiSignature(local_url);
        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            LOGGER.error("获取JSSDK 参数失败,{},\n{}", wxError.toString(), e.getMessage());
            throw new WxErrorException(e.getError());
        }
    }
}
