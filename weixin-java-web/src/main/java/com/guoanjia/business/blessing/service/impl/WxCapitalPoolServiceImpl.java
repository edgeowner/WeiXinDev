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

package com.guoanjia.business.blessing.service.impl;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.ObjectUtil;
import com.github.asherli0103.utils.RandomUtil;
import com.guoanjia.business.base.entity.WxActivity;
import com.guoanjia.business.base.entity.WxUserExtract;
import com.guoanjia.business.base.entity.jpa.*;
import com.guoanjia.business.blessing.entity.WxCapitalPool;
import com.guoanjia.business.blessing.entity.jpa.WxCapitalPoolRepository;
import com.guoanjia.business.blessing.service.WxCapitalPoolService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * //TODO  需增加日志记录
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxCapitalPoolServiceImpl extends BaseServiceImpl<WxCapitalPool, String> implements WxCapitalPoolService {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxCapitalPoolRepository wxCapitalPoolRepository;
    private final WxUserActivityRepository wxUserActivityRepository;
    private final WxActivityRepository wxActivityRepository;
    private final WxUserExtractRepository wxUserExtractRepository;
    private final WxRedPackRecordRepository wxRedPackRecordRepository;
    private final WxUserRepository wxUserRepository;


    public WxCapitalPoolServiceImpl(BaseRepository<WxCapitalPool, String> baseRepository, WxCapitalPoolRepository wxCapitalPoolRepository, WxUserActivityRepository wxUserActivityRepository, WxActivityRepository wxActivityRepository, WxUserExtractRepository wxUserExtractRepository, WxRedPackRecordRepository wxRedPackRecordRepository, WxUserRepository wxUserRepository) {
        super(baseRepository);
        this.wxCapitalPoolRepository = wxCapitalPoolRepository;
        this.wxUserActivityRepository = wxUserActivityRepository;
        this.wxActivityRepository = wxActivityRepository;
        this.wxUserExtractRepository = wxUserExtractRepository;
        this.wxRedPackRecordRepository = wxRedPackRecordRepository;
        this.wxUserRepository = wxUserRepository;
    }

    private static final Double[] AMOUNT_OF_RED_PACKETS = new Double[]{1.1, 1.68, 1.8, 1.86, 1.88, 1.89, 1.9, 1.96, 1.98, 1.99};


    @Override
    @Transactional(rollbackFor = {Exception.class, WxErrorException.class}, isolation = Isolation.SERIALIZABLE)
    public synchronized AjaxJson randomRedPack(String openid, String userphone) throws WxErrorException {
        AjaxJson ajaxJson = new AjaxJson();
        Date date = new Date();

        WxActivity wxActivity1 = this.wxActivityRepository.findOne("4028daf3593f3e4201593f418ad70002");

        if (date.before(wxActivity1.getActStartTime())) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("活动未开始，请稍等，感谢您的关注");
            return ajaxJson;
        }

        if (date.after(wxActivity1.getActEndTime())) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("活动已结束，感谢您的关注");
            return ajaxJson;
        }


        //1.判断活动
        Criteria<WxActivity> activityCriteria = new Criteria<>();
        activityCriteria.add(Restrictions.lte("actEndTime", date, true));
        activityCriteria.add(Restrictions.gte("actStartTime", date, true));
        activityCriteria.add(Restrictions.eq("actType", "01", true));
        WxActivity wxActivity = this.wxActivityRepository.findOne(activityCriteria);
        if (ObjectUtil.isNotNull(wxActivity)) {
            //2.查询余额
            Criteria<WxCapitalPool> capitalPoolCriteria = new Criteria<>();
//            capitalPoolCriteria.add(Restrictions.eq("cpDate", DateUtil.stringToDate(DateUtil.dateToString(date, "yyyy-MM-dd"), "yyyy-MM-dd"), true));
            capitalPoolCriteria.add(Restrictions.lte("cpEndTime", date, true));
            capitalPoolCriteria.add(Restrictions.gte("cpCreateTime", date, true));
            WxCapitalPool wxCapitalPool = this.wxCapitalPoolRepository.findOneWithLock(capitalPoolCriteria);

            if (ObjectUtil.isNotNull(wxCapitalPool)) {
                //3.判断用户是否参与
                Criteria<WxUserExtract> userActivityCriteria = new Criteria<>();
                userActivityCriteria.add(
                        Restrictions.or(
                                Restrictions.eq("ueOpenid", openid, true),
                                Restrictions.eq("ueUserphone", userphone, true)
                        )
                );
                WxUserExtract wxUserActivity = wxUserExtractRepository.findOne(userActivityCriteria);
                if (ObjectUtil.isNull(wxUserActivity)) {
                    BigDecimal bl = BigDecimal.ZERO;
                    if (Objects.equals("0", wxCapitalPool.getCpStatus()) && !Objects.equals("0", wxCapitalPool.getId())) {
                        String id = String.valueOf(Integer.valueOf(wxCapitalPool.getId()) - 1);
                        WxCapitalPool wxCapitalPool1 = this.wxCapitalPoolRepository.findOne(id);
                        bl = bl.add(new BigDecimal(String.valueOf(wxCapitalPool1.getCpBalance())));
                        wxCapitalPool.setCpStatus("1");
                    }

                    BigDecimal balance = new BigDecimal(wxCapitalPool.getCpBalance().toString());
                    balance = balance.add(bl);
                    logger.info("balance:  " + balance);
                    BigDecimal money = new BigDecimal(RandomUtil.randomEle(AMOUNT_OF_RED_PACKETS).toString());
                    logger.info("Money:  " + money);
                    BigDecimal balanceCurrent = balance.subtract(money);

                    logger.info("balanceCurrent:  " + balanceCurrent);
                    //4.修改余额
                    if (balanceCurrent.compareTo(BigDecimal.ZERO) >= 0) {
                        wxCapitalPool.setCpBalance(balanceCurrent.doubleValue());
                        this.wxCapitalPoolRepository.save(wxCapitalPool);
                        //5.保存用户资金记录
                        this.wxUserExtractRepository.save(new WxUserExtract()
                                .setActId(wxActivity.getId())
                                .setUeCreatetime(date)
                                .setUeMoney(money.doubleValue())
                                .setUeOpenid(openid)
                                .setUeType("02")
                                .setUeUserphone(userphone)
                        );


                        //7.记录支付结果
//                            WxRedPackRecord wxRedPackPaymentRecord = new WxRedPackRecord();
//                            wxRedPackPaymentRecord.setErrCode(wxPaySendRedpackResult.getErrCode());
//                            wxRedPackPaymentRecord.setErrCodeDes(wxPaySendRedpackResult.getErrCodeDes());
//                            wxRedPackPaymentRecord.setMchBillno(wxPaySendRedpackResult.getMchBillno());
//                            wxRedPackPaymentRecord.setMchId(wxPaySendRedpackResult.getMchId());
//                            wxRedPackPaymentRecord.setReOpenid(wxPaySendRedpackResult.getReOpenid());
//                            wxRedPackPaymentRecord.setResultCode(wxPaySendRedpackResult.getResultCode());
//                            wxRedPackPaymentRecord.setReturnCode(wxPaySendRedpackResult.getReturnCode());
//                            wxRedPackPaymentRecord.setReturnMsg(wxPaySendRedpackResult.getReturnMsg());
//                            wxRedPackPaymentRecord.setSendListid(wxPaySendRedpackResult.getSendListid());
//                            wxRedPackPaymentRecord.setSign(wxPaySendRedpackResult.getSign());
//                            wxRedPackPaymentRecord.setTotalAmount(wxPaySendRedpackResult.getTotalAmount());
//                            wxRedPackPaymentRecord.setWxappid(wxPaySendRedpackResult.getWxappid());
//                            this.wxRedPackRecordRepository.save(wxRedPackPaymentRecord);

                        //wxUserActivityRepository.save(new WxUserActivity().setUactActid(wxActivity.getId()).setUactCreateTime(date).setUactName(wxActivity.getActName()).setUactOpenid(openid).setUactUserphone(userphone));

                        ajaxJson.setData(money.doubleValue());
                        ajaxJson.setSuccess(true);
                        ajaxJson.setMsg("红包发送成功,请注意领取红包");
                        return ajaxJson;
//                            } else {
//                                //抛出异常回滚数据
//                                throw new WxErrorException(new WxError().setErrorMsg("红包发送失败"));
//                            }
//                        } catch (WxErrorException e) {
//                            //抛出异常回滚数据
//                            throw new WxErrorException(new WxError().setErrorMsg("红包发送失败"));
//                        }
                    }
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("当前时段红包已被抢光，敬请下个发放时段！");
                    return ajaxJson;
                }
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("您已领取过红包啦！");
                return ajaxJson;
            }
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("当前时段红包已被抢光，敬请下个发放时段！");
            return ajaxJson;
        }
        ajaxJson.setSuccess(false);
        ajaxJson.setMsg("活动已结束,感谢您的关注");
        return ajaxJson;
    }

}
