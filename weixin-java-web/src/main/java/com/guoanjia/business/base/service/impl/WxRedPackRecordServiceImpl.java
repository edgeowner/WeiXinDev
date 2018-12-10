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

package com.guoanjia.business.base.service.impl;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Functions;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.DateUtil;
import com.github.asherli0103.utils.ObjectUtil;
import com.github.asherli0103.utils.RandomUtil;
import com.github.asherli0103.utils.StringUtil;
import com.guoanjia.business.base.entity.*;
import com.guoanjia.business.base.entity.jpa.*;
import com.guoanjia.business.base.service.WxRedPackRecordService;
import com.guoanjia.business.poster.controller.parameters.PaymentParameters;
import com.guoanjia.business.utils.ContextHolderUtils;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.pay.request.WxPaySendRedpackRequest;
import me.chanjar.weixin.open.bean.pay.result.WxPaySendRedpackResult;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

import static com.github.asherli0103.core.jpa.criteria.Function.FunctionName.SUM;


/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxRedPackRecordServiceImpl extends BaseServiceImpl<WxRedPackRecord, String> implements WxRedPackRecordService {


    private final WxRedPackRecordRepository wxRedPackRecordRepository;

    private final WxUserRepository wxUserRepository;

    private final WxActivityRepository wxActivityRepository;

    private final WxVFundsRepository wxVFundsRepository;

    private final WxOpenService wxOpenService;

    private final WxUserExtractRepository wxUserExtractRepository;

    private WxOpenConfigStorage wxOpenConfigStorage;

    public WxRedPackRecordServiceImpl(BaseRepository<WxRedPackRecord, String> baseRepository, WxRedPackRecordRepository wxRedPackRecordRepository, WxUserRepository wxUserRepository, WxActivityRepository wxActivityRepository, WxVFundsRepository wxVFundsRepository, WxOpenService wxOpenService, WxUserExtractRepository wxUserExtractRepository) {
        super(baseRepository);
        this.wxRedPackRecordRepository = wxRedPackRecordRepository;
        this.wxUserRepository = wxUserRepository;
        this.wxActivityRepository = wxActivityRepository;
        this.wxVFundsRepository = wxVFundsRepository;
        this.wxOpenService = wxOpenService;
        this.wxUserExtractRepository = wxUserExtractRepository;
        this.wxOpenConfigStorage = this.wxOpenService.getWxOpenConfigStorage();
    }

    public static String getFixLenthString(int Length) {
        String result = "";
        Random random = new Random();
        for (int i = 0; i < Length; i++) {
            result += random.nextInt(10) + "";
        }
        return result;
    }

    @Transactional(readOnly = true)
    public double queryPersonalFunds(String openid, String fundsType, String actId) {
        if (StringUtil.isBlank(openid)) {
            openid = "0";
        }
        String[] fundsTypeArray = fundsType.split(",");
        List<String> fundsTypes = Arrays.asList(fundsTypeArray);

        Criteria<WxVFunds> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("vfOpenid", openid, true));
        criteria.add(Restrictions.eq("actName", actId, true));
        criteria.add(Restrictions.in("vfType", fundsTypes, true));

        List<Functions> selections = new ArrayList<>();
        selections.add(new Functions("vfMoney", SUM));

        Double dd = wxVFundsRepository.functionQuery(criteria, Double.class, null, selections);
        if (null == dd) {
            return 0;
        }
        return dd;
    }

    @Override
    public synchronized AjaxJson payment(PaymentParameters parameters) {
        String openid = parameters.getOpenid();
        Double money = parameters.getMoney();
        String clientIp = parameters.getClientIp();
        HttpSession session = ContextHolderUtils.getSession();
        String code = (String) session.getAttribute(parameters.getOpenid());
        AjaxJson ajaxJson = new AjaxJson();

//        ajaxJson.setMsg("支付正在努力修复中,请稍后");
//        ajaxJson.setSuccess(false);
//        return ajaxJson;
//
        if (Objects.equals(code, parameters.getvCode())) {
            WxUser wxNUserSubscribe = new WxUser();
            wxNUserSubscribe.setuUserphone(parameters.getUserphone());
            Example<WxUser> example = Example.of(wxNUserSubscribe);
            boolean exist = wxUserRepository.exists(example);
            if (!exist) {
                wxNUserSubscribe.setuUserphone(null);
                wxNUserSubscribe.setuOpenid(parameters.getOpenid());
                example = Example.of(wxNUserSubscribe);
                wxNUserSubscribe = wxUserRepository.findOne(example);
                wxNUserSubscribe.setuUserphone(parameters.getUserphone());
                Object obj = wxUserRepository.save(wxNUserSubscribe);
                if (ObjectUtil.isEmpty(obj)) {
                    ajaxJson.setMsg("红包领取失败");
                    ajaxJson.setSuccess(false);
                    return ajaxJson;
                }
            }
        } else {
            ajaxJson.setMsg("验证码错误");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }


        Double balance = queryPersonalFunds(openid, "00,01,02", parameters.getActId());
        WxActivity wxActivity = wxActivityRepository.findOne(parameters.getActId());
        Double total = queryPersonalFunds(openid, "00,01", parameters.getActId());
        Double min = wxActivity.getActMinAmount();
        Double max = wxActivity.getActMaxAmount();


        if (money < min) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("最小提现金额为" + min + "元");
            return ajaxJson;
        }

        if (money > max) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("最大提现金额为" + min + "元");
            return ajaxJson;
        }

        if (money > balance) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("超出您的可提现金额");
            return ajaxJson;
        }


//        if (total < min) {
//            ajaxJson.setSuccess(false);
//            ajaxJson.setMsg("累计金额大于或等于" + min + "元,方可提现");
//            return ajaxJson;
//        }
//        if (money < 1) {
//            ajaxJson.setSuccess(false);
//            ajaxJson.setMsg("单笔提现最小1元,请重新输入");
//            return ajaxJson;
//        }
//
//        if (money > max) {
//            ajaxJson.setSuccess(false);
//            ajaxJson.setMsg("单笔提现最大" + max + "元,请重新输入");
//            return ajaxJson;
//        }
//
//        if (money > balance) {
//            ajaxJson.setSuccess(false);
//            ajaxJson.setMsg("超出您的可提现金额");
//            return ajaxJson;
//        }


        DecimalFormat df = new DecimalFormat("######0");
        WxPaySendRedpackRequest wxPaySendRedpackRequest = new WxPaySendRedpackRequest();
        wxPaySendRedpackRequest.setClientIp(clientIp);
        wxPaySendRedpackRequest.setMchBillNo("1382292802" + DateUtil.dateToString(new Date(), "yyyyMMdd") + getFixLenthString(10));
        wxPaySendRedpackRequest.setSendName("国安家");
        wxPaySendRedpackRequest.setReOpenid(openid);
        wxPaySendRedpackRequest.setTotalAmount(Integer.valueOf(df.format(money * 100)));
        wxPaySendRedpackRequest.setTotalNum(1);
        wxPaySendRedpackRequest.setWishing("感谢您参加国安家快码传播活动,祝您生活快乐！");
        wxPaySendRedpackRequest.setActName("快码传播");
        wxPaySendRedpackRequest.setRemark("快码传播资金提取");
        try {
            WxPaySendRedpackResult wxPaySendRedpackResult = wxOpenService.getPayService().sendRedpack(wxPaySendRedpackRequest, new File("/usr/local/kuaima/apiclient_cert.p12"));
            //对账历史记录
            WxRedPackRecord wxRedPackPaymentRecord = new WxRedPackRecord();
            wxRedPackPaymentRecord.setErrCode(wxPaySendRedpackResult.getErrCode());
            wxRedPackPaymentRecord.setErrCodeDes(wxPaySendRedpackResult.getErrCodeDes());
            wxRedPackPaymentRecord.setMchBillno(wxPaySendRedpackResult.getMchBillno());
            wxRedPackPaymentRecord.setMchId(wxPaySendRedpackResult.getMchId());
            wxRedPackPaymentRecord.setReOpenid(wxPaySendRedpackResult.getReOpenid());
            wxRedPackPaymentRecord.setResultCode(wxPaySendRedpackResult.getResultCode());
            wxRedPackPaymentRecord.setReturnCode(wxPaySendRedpackResult.getReturnCode());
            wxRedPackPaymentRecord.setReturnMsg(wxPaySendRedpackResult.getReturnMsg());
            wxRedPackPaymentRecord.setSendListid(wxPaySendRedpackResult.getSendListid());
            wxRedPackPaymentRecord.setSign(wxPaySendRedpackResult.getSign());
            wxRedPackPaymentRecord.setTotalAmount(wxPaySendRedpackResult.getTotalAmount());
            wxRedPackPaymentRecord.setWxappid(wxPaySendRedpackResult.getWxappid());
            wxRedPackRecordRepository.save(wxRedPackPaymentRecord);

            if ("SUCCESS".equals(wxPaySendRedpackResult.getResultCode()) && "SUCCESS".equals(wxPaySendRedpackResult.getErrCode())) {
                WxUserExtract wxUserExtract = new WxUserExtract();
                wxUserExtract.setActId(wxActivity.getId());
                wxUserExtract.setUeCreatetime(new Date());
                wxUserExtract.setUeMoney(money);
                wxUserExtract.setUeOpenid(openid);
                wxUserExtract.setUeType("02");
                wxUserExtract.setUeUserphone(parameters.getUserphone());
                wxUserExtractRepository.save(wxUserExtract);

                ajaxJson.setSuccess(true);
                ajaxJson.setMsg("资金提取成功,请注意领取公众号红包");
                return ajaxJson;
            } else {
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("资金提取失败");
                return ajaxJson;
            }

        } catch (WxErrorException e) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("资金提取异常");
            return ajaxJson;
        }

    }


    @Override
    public synchronized AjaxJson sendRedPack(String openid, String clientIp, String userphone, JedisPool jedisPool) {
        AjaxJson ajaxJson = new AjaxJson();

        Double money = 0D;
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(openid);
            if (value == null) {
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("非法请求");
                return ajaxJson;
            }
            money = Double.valueOf(value);
        }
        if (money == 0) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("您已领取或正在发放中,请不要频繁点击");
            return ajaxJson;
        }
        //置0防止快速重复点击
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(openid, "0");
        }
        DecimalFormat df = new DecimalFormat("########0");
        WxPaySendRedpackRequest wxPaySendRedpackRequest = new WxPaySendRedpackRequest();
        wxPaySendRedpackRequest.setClientIp(clientIp);
        wxPaySendRedpackRequest.setMchBillNo(wxOpenService.getWxOpenConfigStorage().getPartnerId() + DateUtil.dateToString(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(10));
        wxPaySendRedpackRequest.setSendName("国安家");
        wxPaySendRedpackRequest.setReOpenid(openid);
        wxPaySendRedpackRequest.setTotalAmount(Integer.valueOf(df.format(money * 100)));
        wxPaySendRedpackRequest.setTotalNum(1);
        wxPaySendRedpackRequest.setWishing("国安家祝你：元宵节快乐！");
            wxPaySendRedpackRequest.setActName("国安家陪你闹元宵");
        wxPaySendRedpackRequest.setRemark("国安家粉丝红利卷土重来");
        WxPaySendRedpackResult wxPaySendRedpackResult = null;
        try {
            wxPaySendRedpackResult = this.wxOpenService.getPayService().sendRedpack(wxPaySendRedpackRequest, new File(this.wxOpenConfigStorage.getParentKeyPath()));
            WxRedPackRecord wxRedPackPaymentRecord = new WxRedPackRecord();
            wxRedPackPaymentRecord.setErrCode(wxPaySendRedpackResult.getErrCode());
            wxRedPackPaymentRecord.setErrCodeDes(wxPaySendRedpackResult.getErrCodeDes());
            wxRedPackPaymentRecord.setMchBillno(wxPaySendRedpackResult.getMchBillno());
            wxRedPackPaymentRecord.setMchId(wxPaySendRedpackResult.getMchId());
            wxRedPackPaymentRecord.setReOpenid(wxPaySendRedpackResult.getReOpenid());
            wxRedPackPaymentRecord.setResultCode(wxPaySendRedpackResult.getResultCode());
            wxRedPackPaymentRecord.setReturnCode(wxPaySendRedpackResult.getReturnCode());
            wxRedPackPaymentRecord.setReturnMsg(wxPaySendRedpackResult.getReturnMsg());
            wxRedPackPaymentRecord.setSendListid(wxPaySendRedpackResult.getSendListid());
            wxRedPackPaymentRecord.setSign(wxPaySendRedpackResult.getSign());
            wxRedPackPaymentRecord.setTotalAmount(wxPaySendRedpackResult.getTotalAmount());
            wxRedPackPaymentRecord.setWxappid(wxPaySendRedpackResult.getWxappid());
            wxRedPackRecordRepository.save(wxRedPackPaymentRecord);
            if ("SUCCESS".equals(wxPaySendRedpackResult.getResultCode()) && "SUCCESS".equals(wxPaySendRedpackResult.getErrCode())) {
                WxUserExtract wxUserExtract = new WxUserExtract();
                wxUserExtract.setActId("2017_YX");
                wxUserExtract.setUeCreatetime(new Date());
                wxUserExtract.setUeMoney(money);
                wxUserExtract.setUeOpenid(openid);
                wxUserExtract.setUeType("02");
                wxUserExtract.setUeUserphone(userphone);
                wxUserExtractRepository.save(wxUserExtract);

                ajaxJson.setSuccess(true);
                ajaxJson.setCode(200);
                ajaxJson.setMsg("红包发送成功,请注意领取红包");
                return ajaxJson;
            }
            logger.error("红包发送失败,用户金额: {},用户OpenId: {},ResultCode: {},ErrCode: {}", money, openid, wxPaySendRedpackResult.getResultCode(), wxPaySendRedpackResult.getErrCode());
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.set(openid, String.valueOf(money));
            }
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("领取失败,请重试");
            return ajaxJson;
        } catch (WxErrorException e) {
            logger.error("红包发送失败,用户金额: {},用户OpenId: {},ERRORMSG:{},ERRORCODE:{}", money, openid, e.getError().getErrorMsg(), e.getError().getErrorCode());
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.set(openid, String.valueOf(money));
            }
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("领取失败,请重试");
            return ajaxJson;
        }
    }


}
