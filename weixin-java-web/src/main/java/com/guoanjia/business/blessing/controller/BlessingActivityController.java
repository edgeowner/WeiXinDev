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

package com.guoanjia.business.blessing.controller;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.utils.*;
import com.github.asherli0103.utils.enums.ErrorCode;
import com.guoanjia.business.base.controller.BaseController;
import com.guoanjia.business.base.entity.WxUserExtract;
import com.guoanjia.business.base.service.WxUserActivityService;
import com.guoanjia.business.base.service.WxUserExtractService;
import com.guoanjia.business.blessing.entity.WxBlessingInfo;
import com.guoanjia.business.blessing.entity.WxBlessingMessage;
import com.guoanjia.business.blessing.entity.WxMessageCode;
import com.guoanjia.business.blessing.entity.WxTemp;
import com.guoanjia.business.blessing.service.*;
import com.guoanjia.business.utils.MessageUtils;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.pay.request.WxPaySendRedpackRequest;
import me.chanjar.weixin.open.bean.pay.result.WxPaySendRedpackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@RestController
@RequestMapping(path = {"blessing"})
public class BlessingActivityController extends BaseController {

    private final WxCapitalPoolService wxCapitalPoolService;

    private final WxBlessingInfoService wxBlessingInfoService;

    private final WxBlessingMessageService wxBlessingMessageService;

    private final WxUserActivityService wxUserActivityService;

    private final WxUserExtractService wxUserExtractService;


    private final WxMessageCodeService wxMessageCodeService;

    private final WxOpenService wxOpenService;
    private WxOpenConfigStorage wxOpenConfigStorage;

    private final WxTempService wxTempService;

    private JedisPool jedisPool;

    public BlessingActivityController(WxCapitalPoolService wxCapitalPoolService
            , WxBlessingInfoService wxBlessingInfoService, WxBlessingMessageService wxBlessingMessageService
            , WxUserActivityService wxUserActivityService, WxUserExtractService wxUserExtractService
            , WxMessageCodeService wxMessageCodeService, WxOpenService wxOpenService, WxTempService wxTempService) {
        super(wxOpenService);
        this.wxCapitalPoolService = wxCapitalPoolService;
        this.wxBlessingInfoService = wxBlessingInfoService;
        this.wxBlessingMessageService = wxBlessingMessageService;
        this.wxUserActivityService = wxUserActivityService;
        this.wxUserExtractService = wxUserExtractService;
        this.wxMessageCodeService = wxMessageCodeService;
        this.wxOpenService = wxOpenService;
        this.wxTempService = wxTempService;
        this.wxOpenConfigStorage = this.wxOpenService.getWxOpenConfigStorage();
       this.jedisPool = this.wxOpenConfigStorage.getJedisPool();
    }

    /**
     * 随机发放红包
     *
     * @param request    请求流
     * @param response   返回流
     * @param parameters 请求参数
     * @return 发放结果
     */
    @PostMapping(path = {"randomRedPack"})
    public AjaxJson randomRedPack(HttpServletRequest request, HttpServletResponse response, @RequestBody BaseParameters parameters) {
        return randomRedPack(request, parameters);
    }


    /**
     * 获取祝福信息模板信息
     *
     * @return 信息列表
     */
    @PostMapping(path = {"getBlessingMessges"})
    public AjaxJson getBlessingMessges() {
        AjaxJson ajaxJson = new AjaxJson();
        List<WxBlessingMessage> wxBlessingMessages = this.wxBlessingMessageService.findAll();
        if (wxBlessingMessages.size() > 0) {
            ajaxJson.setData(wxBlessingMessages);
            return ajaxJson;
        }
        ajaxJson.setData(new ArrayList<>());
        ajaxJson.setMsgAndCode(ErrorCode.FAILED_LOAD_DATA);
        ajaxJson.setSuccess(false);
        return ajaxJson;
    }

    /**
     * 获取用户祝福短信
     *
     * @param parameters 用户Openid
     * @return 祝福信息
     */
    @PostMapping(path = {"getBlessingInfo"})
    public AjaxJson getBlessingInfo(@RequestBody BaseParameters parameters) {
        return wxBlessingInfoService.getBlessingInfo(parameters.getOpenid());
    }

    /**
     * 保存用户祝福信息,
     *
     * @param wxBlessingInfo 祝福信息
     * @return 是否保存成功
     */
    @PostMapping(path = {"setBlessingInfo"})
    public AjaxJson setBlessingInfo(@RequestBody WxBlessingInfo wxBlessingInfo) {
        return wxBlessingInfoService.saveBlessingInfo(wxBlessingInfo);
    }

    @PostMapping(path = {"checkRandomRedPack"})
    public AjaxJson checkRandomRedPack(@RequestBody BaseParameters parameters) {
        AjaxJson ajaxJson = new AjaxJson();
        String openid = parameters.getOpenid();
        if (StringUtil.isBlank(openid)) {
            ajaxJson.setMsgAndCode(ErrorCode.MISS_REQUIRED_PARAMETER);
            return ajaxJson;
        }
        Criteria<WxUserExtract> criteria = new Criteria<>();
        String userphone = parameters.getUserphone();
        if (ObjectUtil.isNotEmpty(userphone)) {
            criteria.add(
                    Restrictions.or(
                            Restrictions.eq("ueOpenid", parameters.getOpenid(), true),
                            Restrictions.eq("ueUserphone", parameters.getUserphone(), true)
                    )
            );
        } else {
            criteria.add(
                    Restrictions.eq("ueOpenid", parameters.getOpenid(), true)
            );
        }
        boolean bl = wxUserExtractService.findAll(criteria).isEmpty();
        if (!bl) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_BLESSING_HAS_INVOLVED);
            return ajaxJson;
        }

        return ajaxJson;
    }

    @PostMapping(path = {"sendValidateCode"})
    public AjaxJson sendValidateCode(@RequestBody BaseParameters parameters) {
        AjaxJson ajaxJson = new AjaxJson();

        Date date = new Date();
        Criteria<WxTemp> tempCriteria = new Criteria<>();
        tempCriteria.add(Restrictions.lte("cpEndTime", date, true));
        tempCriteria.add(Restrictions.gte("cpCreateTime", date, true));
        tempCriteria.add(Restrictions.eq("cpStatus", "0", true));
        WxTemp wxTemp = wxTempService.findOne(tempCriteria);
        if (ObjectUtil.isEmpty(wxTemp)){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("当前时段红包已被抢光，敬请下个发放时段！");
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
        Criteria<WxUserExtract> criteria = new Criteria<>();
        criteria.add(
                Restrictions.or(
                        Restrictions.eq("ueOpenid", parameters.getOpenid(), true),
                        Restrictions.eq("ueUserphone", parameters.getUserphone(), true)
                )
        );
        boolean bl = wxUserExtractService.findAll(criteria).isEmpty();
        if (!bl) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_BLESSING_HAS_INVOLVED);
            return ajaxJson;
        }

        String validateCode = RandomUtil.randomNumbers(6);
        wxMessageCodeService.saveCode(new WxMessageCode().setMcCode(validateCode).setMcCreateTime(new Date()).setMcUserphone(userphone));

        String result = MessageUtils.messagePostRequest("欢迎您的加入,您的验证码为:" + validateCode, userphone);
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
        ajaxJson = wxMessageCodeService.getCode(userphone, vcode);
        if (!ajaxJson.isSuccess()) {
            return ajaxJson;
        }
        try {
            return randomRedPack(request, parameters);
        } catch (Exception e) {
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_BLESSING_SEND_ERROR);
            return ajaxJson;
        }
    }


    private AjaxJson randomRedPack(HttpServletRequest request, BaseParameters parameters) {


        Date date = new Date();

        String ip = "";
        AjaxJson ajaxJson = new AjaxJson();

        Criteria<WxTemp> tempCriteria = new Criteria<>();

        tempCriteria.add(Restrictions.lte("cpEndTime", date, true));
        tempCriteria.add(Restrictions.gte("cpCreateTime", date, true));
        tempCriteria.add(Restrictions.eq("cpStatus", "0", true));

        WxTemp wxTemp = wxTempService.findOne(tempCriteria);
        if (ObjectUtil.isNotEmpty(wxTemp)) {
            if (Objects.equals("0", wxTemp.getCpStatus())) {
                int key = 0;
                try (Jedis jedis = jedisPool.getResource()) {
                    key = Integer.valueOf(jedis.get("COUNT_CLICK_KEY"));
                }

                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.set("COUNT_CLICK_KEY", String.valueOf(key + 1));
                }

                if (key >=2000) {
                    wxTempService.save(wxTemp.setCpStatus("1"));
                    try (Jedis jedis = jedisPool.getResource()) {
                        jedis.set("COUNT_CLICK_KEY", String.valueOf(0));
                    }
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("当前时段红包已被抢光，敬请下个发放时段！");
                    return ajaxJson;
                }
            }
        } else {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("当前时段红包已被抢光，敬请下个发放时段！");
            return ajaxJson;
        }

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
        try {
            ajaxJson = this.wxCapitalPoolService.randomRedPack(parameters.getOpenid(), parameters.getUserphone());
            if (!ajaxJson.isSuccess()) {
                return ajaxJson;
            }

            DecimalFormat df = new DecimalFormat("########0");
            WxPaySendRedpackRequest wxPaySendRedpackRequest = new WxPaySendRedpackRequest();
            wxPaySendRedpackRequest.setClientIp(ip);
            wxPaySendRedpackRequest.setMchBillNo(wxOpenService.getWxOpenConfigStorage().getPartnerId() + DateUtil.dateToString(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(10));
            wxPaySendRedpackRequest.setSendName("国安家");
            wxPaySendRedpackRequest.setReOpenid(parameters.getOpenid());
            wxPaySendRedpackRequest.setTotalAmount(Integer.valueOf(df.format((double) ajaxJson.getData() * 100)));
            wxPaySendRedpackRequest.setTotalNum(1);
            wxPaySendRedpackRequest.setWishing("国安家在这里祝您新年快乐!");
            wxPaySendRedpackRequest.setActName("国安家给您拜年啦！");
            wxPaySendRedpackRequest.setRemark("国安家新年祝福活动发放红包");

            WxPaySendRedpackResult wxPaySendRedpackResult = this.wxOpenService.getPayService().sendRedpack(wxPaySendRedpackRequest, new File(this.wxOpenConfigStorage.getParentKeyPath()));


            if (!"SUCCESS".equals(wxPaySendRedpackResult.getResultCode()) || !"SUCCESS".equals(wxPaySendRedpackResult.getErrCode())) {
                logger.error("随机红吧发送失败,用户手机号: {},用户OpenId: {},ResultCode: {},ErrCode: {}", parameters.getUserphone(), parameters.getOpenid(), wxPaySendRedpackResult.getResultCode(), wxPaySendRedpackResult.getErrCode());
                wxUserExtractService.deleteExtract(new WxUserExtract().setUeOpenid(parameters.getOpenid()));
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("红包发送失败,稍后重试");
                return ajaxJson;
            }


        } catch (WxErrorException e) {
            wxUserExtractService.deleteExtract(new WxUserExtract().setUeOpenid(parameters.getOpenid()));
            logger.error("随机红吧发送失败,用户手机号: {},用户OpenId: {},失败原因: {},\n{}", parameters.getUserphone(), parameters.getOpenid(), e.getError().toString(), e.getMessage());
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("红包发送失败,稍后重试");
            return ajaxJson;
        }
        return ajaxJson;
    }

}
