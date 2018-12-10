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

package com.guoanjia.business.poster.controller;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.utils.NetWorkUtil;
import com.guoanjia.business.base.controller.BaseController;
import com.guoanjia.business.base.entity.WxUser;
import com.guoanjia.business.base.entity.WxVActivity;
import com.guoanjia.business.base.entity.WxVFunds;
import com.guoanjia.business.base.service.*;
import com.guoanjia.business.blessing.service.*;
import com.guoanjia.business.poster.controller.parameters.*;
import com.guoanjia.business.poster.entity.WxAgentFunds;
import com.guoanjia.business.poster.service.WxAgentFundsService;
import me.chanjar.weixin.open.api.WxOpenService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */

@RestController
@RequestMapping(
        path = {"/activity"},
        method = {RequestMethod.POST},
        consumes = {"application/json", "application/json;charset=UTF-8"},
        produces = {"application/json;charset=UTF-8"}
)
public class RestActivityController extends BaseController {

    private final WxOpenService wxOpenService;
    private final WxActivityService wxActivityService;

    private final WxVFundsService wxVFundsService;

    private final WxAgentFundsService wxAgentFundsService;

    private final WxVActivityService wxVActivityService;

    private final WxUserService wxUserService;

    private final WxCapitalPoolService wxCapitalPoolService;

    private final WxRedPackRecordService wxRedPackRecordService;

    private final WxUserActivityService wxUserActivityService;

    private final WxBlessingMessageService wxBlessingMessageService;

    private final WxBlessingInfoService wxBlessingInfoService;


    public RestActivityController(WxOpenService wxOpenService, WxActivityService wxActivityService, WxVFundsService wxVFundsService, WxAgentFundsService wxAgentFundsService, WxVActivityService wxVActivityService, WxUserService wxUserService, WxCapitalPoolService wxCapitalPoolService, WxRedPackRecordService wxRedPackRecordService, WxUserActivityService wxUserActivityService, WxBlessingMessageService wxBlessingMessageService, WxBlessingInfoService wxBlessingInfoService) {
        super(wxOpenService);
        this.wxOpenService = wxOpenService;
        this.wxActivityService = wxActivityService;
        this.wxVFundsService = wxVFundsService;
        this.wxAgentFundsService = wxAgentFundsService;
        this.wxVActivityService = wxVActivityService;
        this.wxUserService = wxUserService;
        this.wxCapitalPoolService = wxCapitalPoolService;
        this.wxRedPackRecordService = wxRedPackRecordService;
        this.wxUserActivityService = wxUserActivityService;
        this.wxBlessingMessageService = wxBlessingMessageService;
        this.wxBlessingInfoService = wxBlessingInfoService;
    }

    /**
     * 获取用户信息
     *
     * @param params 用户信息请求参数
     * @return 请求结果
     */
    @RequestMapping(path = {"queryUserInfo"})
    public AjaxJson queryUserInfo(@RequestBody WxUser params) {
        return wxUserService.queryUserInfo(params);
    }

    /**
     * 获取当前活动列表
     *
     * @param wxVActivity 活动信息请求参数
     * @return 活动列表
     */
    @RequestMapping(path = {"queryAllActivity"})
    public AjaxJson queryAllActivity(@RequestBody WxVActivity wxVActivity) {
        return wxVActivityService.findActivityByCondition(wxVActivity);
    }

    /**
     * 获取 经纪人等微信奖金信息
     *
     * @param wxAgentFunds 用户奖金信息获取参数
     * @return 奖金信息
     */
    @RequestMapping(path = {"queryAllAgentFunds"})
    public AjaxJson queryAllAgentFunds(@RequestBody WxAgentFunds wxAgentFunds) {
        return wxAgentFundsService.queryAllAgentFunds(wxAgentFunds);
    }

    /**
     * 用户 经纪人等奖金信息新增
     *
     * @param wxAgentFunds 用户奖金新增参数
     * @return 新增结果
     */
    @RequestMapping(path = {"saveAgentFunds"})
    public AjaxJson saveAgentFunds(@RequestBody WxAgentFunds wxAgentFunds) {
        AjaxJson ajaxJson = new AjaxJson();
        WxAgentFunds wxAgentFunds1 = wxAgentFundsService.save(wxAgentFunds);
        if (null != wxAgentFunds1) {
            ajaxJson.setSuccess(true);
            ajaxJson.setData(wxAgentFunds1);
            ajaxJson.setMsg("数据插入成功");
            return ajaxJson;
        }
        ajaxJson.setSuccess(false);
        ajaxJson.setData(new ArrayList<>());
        ajaxJson.setMsg("数据插入成功");
        return ajaxJson;
    }

    /**
     * 获取活动详细列表
     *
     * @param capitalListParameters 列表请求参数
     * @return 数据列表
     */
    @RequestMapping(path = {"queryUserVFunds"})
    public AjaxJson queryUserVFunds(@RequestBody CapitalListParameters capitalListParameters) {
        AjaxJson ajaxJson = new AjaxJson();
        Page<WxVFunds> wxVFundss = wxVFundsService.queryUserVFunds(capitalListParameters);
        ajaxJson.setSuccess(true);
        ajaxJson.setData(wxVFundss);
        ajaxJson.setMsg("资金数据获取成功");
        return ajaxJson;
    }

    /**
     * 获取奖励余额
     *
     * @param activityAmountParameters 余额获取 参数
     * @return 资金余额
     */
    @RequestMapping(path = {"queryAmountBalance"})
    public AjaxJson queryAmountBalance(@RequestBody ActivityAmountParameters activityAmountParameters) {
        AjaxJson ajaxJson = new AjaxJson();
        double balance = wxVFundsService.queryPersonalFunds(activityAmountParameters);
        double balance1 = wxAgentFundsService.queryAgentFunds(activityAmountParameters.getOpenid());
        ajaxJson.setSuccess(true);
        ajaxJson.setData(new BigDecimal(balance).add(new BigDecimal(balance1)));
        ajaxJson.setMsg("资金数据获取成功");
        return ajaxJson;
    }

    /**
     * 用户资金提取结果
     *
     * @param request 请求流
     * @param params  提取参数
     * @return 提取结果
     */
    @RequestMapping(path = {"payment"})
    public AjaxJson payment(HttpServletRequest request, @RequestBody PaymentParameters params) throws IOException {
        String clientIp = NetWorkUtil.getIpAddress(request);
        params.setClientIp(clientIp);
        return wxRedPackRecordService.payment(params);
    }

    /**
     * 发送短信验证码
     *
     * @param request 请求流
     * @param params  请求参数
     * @return 短信发送结果
     */
    @RequestMapping(path = {"sendVCode"})
    public AjaxJson sendVCode(HttpServletRequest request, @RequestBody SendVCodeParameters params) {
        return wxUserService.sendVCode(params);
    }

    /**
     * 校验短信验证码
     *
     * @param request 请求流
     * @param params  校验参数
     * @return 校验结果
     */
    @RequestMapping(path = {"checkVCode"})
    public AjaxJson checkVCode(HttpServletRequest request, @RequestBody CheckVCodeParameters params) {
        return wxUserService.checkVCode(params);
    }
}
