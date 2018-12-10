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

package com.guoanjia.business.base.controller;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.utils.NetWorkUtil;
import com.github.asherli0103.utils.RandomUtil;
import com.github.asherli0103.utils.StringUtil;
import com.github.asherli0103.utils.VallidateUtil;
import com.github.asherli0103.utils.enums.ErrorCode;
import com.guoanjia.business.base.service.WxRedPackRecordService;
import com.guoanjia.business.utils.MessageUtils;
import me.chanjar.weixin.open.api.WxOpenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@RestController
@RequestMapping(path = {"redpack"})
public class SendRedPackController extends BaseController {

    private final WxOpenService wxOpenService;
    private final WxRedPackRecordService wxRedPackRecordService;

    public SendRedPackController(WxOpenService wxOpenService, WxRedPackRecordService wxRedPackRecordService) {
        super(wxOpenService);
        this.wxOpenService = wxOpenService;
        this.wxRedPackRecordService = wxRedPackRecordService;
    }


    private AjaxJson sendRedPack(HttpServletRequest request, @RequestBody CheckValidateCodeParameters parameters) {
        String ip = "";
        try {
            ip = NetWorkUtil.getIpAddress(request);
            if (logger.isInfoEnabled()) {
                logger.info("获得客户端IP地址: {},用户手机号: {},用户OpenId: {}", ip, parameters.getUserphone(), parameters.getOpenid());
            }
        } catch (IOException e) {
            //非必要异常记录日志后忽略
            if (logger.isDebugEnabled()) {
                logger.debug("未能获得客户端IP地址");
            }
            ip = "127.0.0.1";
        }

        return wxRedPackRecordService.sendRedPack(parameters.getOpenid(), ip, parameters.getUserphone(), jedisPool);
    }

    @PostMapping(path = {"sendValidateCode"})
    public AjaxJson sendValidateCode(@RequestBody BaseParameters parameters) {
        AjaxJson ajaxJson = new AjaxJson();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sf.format(date);
        if (!"2017-02-11".equals(currentDate) && !"2017-02-12".equals(currentDate)) {
            ajaxJson.setMsg("活动开始时间为2月11日0点整至2月12日24点整");
            ajaxJson.setSuccess(false);
            ajaxJson.setCode(203);
            return ajaxJson;
        }

        String userphone = parameters.getUserphone();
        if (StringUtil.isBlank(userphone)) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_MSG_PHONE_EMPTY);
            return ajaxJson;
        }
        if (!VallidateUtil.isMobile(userphone)) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_MSG_PHONE_ERROR);
            return ajaxJson;
        }

        String openid = parameters.getOpenid();
        if (StringUtil.isBlank(openid)) {
            ajaxJson.setMsgAndCode(ErrorCode.UNAUTHORIZED_ACCESS);
            return ajaxJson;
        }

        String money = getString(openid);
        if (money == null) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_SEND_REDPACK_ERROR);
            return ajaxJson;
        }

        if (Double.valueOf(money) == 0) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_SEND_REDPACK_SUCCESS);
            return ajaxJson;
        }

        String validateCode = RandomUtil.randomNumbers(6);
        set(userphone, validateCode);
        expire(userphone, 1800);
        String result = MessageUtils.messagePostRequest("您的验证码为:" + validateCode, userphone);
        if (!Objects.equals("00", result)) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_MSG_SEND_ERROR);
            return ajaxJson;
        }
        ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_MSG_SEND_SUCCESS);
        return ajaxJson;
    }

    @PostMapping(path = {"checkValidateCode"})
    public AjaxJson checkValidateCode(HttpServletRequest request, HttpServletResponse response, @RequestBody CheckValidateCodeParameters parameters) {
        AjaxJson ajaxJson = new AjaxJson();
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sf.format(date);
        if (!"2017-02-11".equals(currentDate) && !"2017-02-12".equals(currentDate)) {
            ajaxJson.setMsg("活动开始时间为2月11日0点整至2月12日24点整");
            ajaxJson.setSuccess(false);
            ajaxJson.setCode(203);
            return ajaxJson;
        }
        String userphone = parameters.getUserphone();
        if (StringUtil.isBlank(userphone)) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_MSG_PHONE_EMPTY);
            return ajaxJson;
        }
        if (!VallidateUtil.isMobile(userphone)) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_MSG_PHONE_ERROR);
            return ajaxJson;
        }
        String openid = parameters.getOpenid();
        if (StringUtil.isBlank(openid)) {
            ajaxJson.setMsgAndCode(ErrorCode.MISS_REQUIRED_PARAMETER);
            return ajaxJson;
        }

        String vcode = parameters.getValidateCode();
        if (StringUtil.isBlank(vcode)) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_VALIDATE_EMPTY);
            return ajaxJson;
        }
        String vcode1 = String.valueOf(getString(userphone));

        if (!Objects.equals(vcode1, vcode)) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_VALIDATE_ERROR);
            return ajaxJson;
        }
        return sendRedPack(request, parameters);
    }


}
