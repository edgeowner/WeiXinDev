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

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.utils.StringUtil;
import com.guoanjia.business.base.service.WxActivityService;
import com.guoanjia.business.base.entity.WxActivity;
import com.guoanjia.business.base.service.WxUserFundsService;
import com.guoanjia.business.base.service.WxUserService;
import com.guoanjia.business.poster.entity.WxPrizeDetail;
import com.guoanjia.business.base.entity.WxUser;
import com.guoanjia.business.base.entity.WxUserFunds;
import com.guoanjia.business.poster.service.WxPrizeDetailService;
import com.guoanjia.business.poster.service.WxUserClickService;
import com.guoanjia.business.poster.service.WxUserRelationService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import me.chanjar.weixin.open.bean.result.WxOpenUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SubscribePosterHandler extends AbstractHandler {


    private final WxUserClickService wxUserClickService;

    private final WxActivityService wxActivityService;

    private final WxPrizeDetailService wxPrizeDetailService;

    private final WxUserRelationService wxUserRelationService;

    private final WxUserFundsService wxUserFundsService;

    private final WxUserService wxUserService;
    private WxOpenUser wxOpenUser = null;
    private WxActivity wxActivity = null;

    @Autowired
    public SubscribePosterHandler(WxUserClickService wxUserClickService, WxActivityService wxActivityService, WxPrizeDetailService wxPrizeDetailService, WxUserRelationService wxUserRelationService, WxUserFundsService wxUserFundsService, WxUserService wxUserService) {
        this.wxUserClickService = wxUserClickService;
        this.wxActivityService = wxActivityService;
        this.wxPrizeDetailService = wxPrizeDetailService;
        this.wxUserRelationService = wxUserRelationService;
        this.wxUserFundsService = wxUserFundsService;
        this.wxUserService = wxUserService;
    }

    /**
     * 用户关注处理事件
     *
     * @param wxMessage      微信消息体
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param wxOpenService  微信底层服务
     * @param sessionManager 微信Session服务
     * @return 微信欢迎信息
     * @throws WxErrorException 微信异常
     */
    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService, WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        //1.判断用户是否在数据库存在
        Criteria<WxUser> wxUserCriteria = new Criteria<>();
        wxUserCriteria.add(Restrictions.eq("uOpenid", wxMessage.getFromUser(), true));
        WxUser wxUser = wxUserService.findOne(wxUserCriteria);

        //2.判断用户是否存在
        if (null != wxUser) {
            return null;
        }

        //获取微信用户信息
        wxOpenUser = wxOpenService.getUserService().userInfo(wxMessage.getFromUser());

        //3.根据条件检索活动是否正在展开,当前根据当前时间检测规定时间内是否存在活动信息
        AjaxJson activity = wxActivityService.checkActivityByCondition(new WxActivity().setActStartTime(new Date()));

        //判断活动是否存在
        if (activity.isSuccess()) {
            wxActivity = (WxActivity) activity.getData();
            String actEndCondition = wxActivity.getActEndCondition();
            String actId = wxActivity.getId();
            if (Objects.equals("time", actEndCondition)) {
                try {
                    saveUser(wxMessage, wxOpenService);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Long count = wxUserService.getActivityUserCount(actId);
                if (count < Integer.valueOf(actEndCondition)) {
                    try {
                        saveUser(wxMessage, wxOpenService);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {

            wxUserService.save(new WxUser().
                    setuCreateTime(new Date())
                    .setuOpenid(wxOpenUser.getOpenId())
                    .setuNickname(wxOpenUser.getNickname()
                    ).setuProvince(wxOpenUser.getProvince())
                    .setuCity(wxOpenUser.getCity())
                    .setuCountry(wxOpenUser.getCountry())
                    .setuHeadimg(wxOpenUser.getHeadImgUrl())
                    .setuSex(wxOpenUser.getSex()).setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                    .setuUnionid(wxOpenUser.getUnionId()));
        }
        return null;
    }

    private void saveUser(WxOpenXmlMessage wxMessage, WxOpenService wxOpenService) throws Exception {
        String actId = wxActivity.getId();
        String openid = wxOpenUser.getOpenId();
        //获取分享者Openid
        String qrscenes = handleSpecial(wxMessage);
        wxUserService.save(new WxUser().
                setuCreateTime(new Date())
                .setuOpenid(wxOpenUser.getOpenId())
                .setuNickname(wxOpenUser.getNickname()
                ).setuProvince(wxOpenUser.getProvince())
                .setuCity(wxOpenUser.getCity())
                .setuCountry(wxOpenUser.getCountry())
                .setuHeadimg(wxOpenUser.getHeadImgUrl())
                .setuSex(wxOpenUser.getSex()).setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                .setuUnionid(wxOpenUser.getUnionId()));
        //查询首次关注奖励
        Criteria<WxPrizeDetail> firstWxPrizeDetailCriteria = new Criteria<>();
        firstWxPrizeDetailCriteria.add(Restrictions.eq("actId", actId, true));
        firstWxPrizeDetailCriteria.add(Restrictions.eq("pdCondition", "first", true));
        firstWxPrizeDetailCriteria.add(Restrictions.eq("pdStatus", "01", true));
        WxPrizeDetail firstWxPrizeDeetail = wxPrizeDetailService.findOne(firstWxPrizeDetailCriteria);
        if (firstWxPrizeDeetail != null) {
            wxUserFundsService.save(
                    new WxUserFunds()
                            .setUfCreatetime(new Date())
                            .setUfMoney(firstWxPrizeDeetail.getPdItem())
                            .setUfOpenid(openid)
                            .setUfType("00")
                            .setActId(actId)
            );
        }

        //如为分享者二维码扫码关注则创建用户关联关系
        if (StringUtil.isNotBlank(qrscenes)) {
            Criteria<WxPrizeDetail> seconWwxPrizeDetailCriteria = new Criteria<>();
            seconWwxPrizeDetailCriteria.add(Restrictions.eq("actId", actId, true));
            seconWwxPrizeDetailCriteria.add(Restrictions.eq("pdCondition", "second", true));
            seconWwxPrizeDetailCriteria.add(Restrictions.eq("pdStatus", "01", true));
            WxPrizeDetail secondWxPrizeDeetail = wxPrizeDetailService.findOne(seconWwxPrizeDetailCriteria);

            if (secondWxPrizeDeetail != null) {
                Double item = secondWxPrizeDeetail.getPdItem();
                AjaxJson relation = wxUserRelationService.saveUserRelation(openid, qrscenes, item, actId);
                if (relation.isSuccess()) {
                    WxOpenUser sendWxOpenUser = wxOpenService.getUserService().userInfo(qrscenes);
                    WxOpenKefuMessage wxOpenKefuMessage1 = new WxOpenKefuMessage();
                    wxOpenKefuMessage1.setToUser(qrscenes);
                    wxOpenKefuMessage1.setMsgType(WxConsts.CUSTOM_MSG_TEXT);

                    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String startTime = time.format(new Date());

                    wxOpenKefuMessage1.setContent(
                            "亲爱的" + sendWxOpenUser.getNickname() + ",恭喜你获得一个代言人!\n" +
                                    "代言人:" + wxOpenUser.getNickname() + "\n" +
                                    "关注时间:" + startTime + "\n" +
                                    "代言费:" + item + "\n" +
                                    // "当前余额:" + String.valueOf(balanceMoney + wxPrizeDetail.getItem()) + "\n" + //TODO 余额建议使用缓存方式防止数据误差
                                    "代言费满" + wxActivity.getActMinAmount() + "元即可提现,最高提现" + wxActivity.getActMaxAmount() + "元\n" +
                                    "<a href='#'>【查看我的分享】</a>"
                    );
                    wxOpenService.getKefuService().sendKefuMessage(wxOpenKefuMessage1);
                }
            }
        }

        WxOpenKefuMessage wxOpenKefuMessage1 = new WxOpenKefuMessage();
        wxOpenKefuMessage1.setToUser(openid);
        wxOpenKefuMessage1.setMsgType(WxConsts.CUSTOM_MSG_TEXT);

        wxOpenKefuMessage1.setContent("活动....");
        wxOpenService.getKefuService().sendKefuMessage(wxOpenKefuMessage1);
    }


    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private String handleSpecial(WxOpenXmlMessage wxMessage) throws Exception {
        String eventKey = wxMessage.getEventKey();
        if (StringUtil.isNotBlank(eventKey) && eventKey.startsWith("qrscene_")) {
            return eventKey.substring(8, eventKey.length());
        }
        return "";
    }


}
