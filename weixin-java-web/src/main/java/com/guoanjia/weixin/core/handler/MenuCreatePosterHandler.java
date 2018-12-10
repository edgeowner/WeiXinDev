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
import com.github.asherli0103.utils.JacksonUtil;
import com.github.asherli0103.utils.ObjectUtil;
import com.github.asherli0103.utils.StringUtil;
import com.github.asherli0103.utils.tools.UUID;
import com.guoanjia.business.base.entity.WxActivity;
import com.guoanjia.business.base.entity.WxUser;
import com.guoanjia.business.base.entity.WxUserFunds;
import com.guoanjia.business.base.service.WxActivityService;
import com.guoanjia.business.base.service.WxUserFundsService;
import com.guoanjia.business.base.service.WxUserService;
import com.guoanjia.business.poster.entity.WxPrizeDetail;
import com.guoanjia.business.poster.entity.WxUserClick;
import com.guoanjia.business.poster.service.WxPrizeDetailService;
import com.guoanjia.business.poster.service.WxUserClickService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import me.chanjar.weixin.open.bean.result.WxOpenQrCodeTicket;
import me.chanjar.weixin.open.bean.result.WxOpenUser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MenuCreatePosterHandler extends AbstractHandler {

    private final WxPrizeDetailService wxPrizeDetailService;

    private final WxUserClickService wxUserClickService;

    private final WxUserService wxUserService;

    private final WxActivityService wxActivityService;

    private final WxUserFundsService wxUserFundsService;

    public MenuCreatePosterHandler(WxPrizeDetailService wxPrizeDetailService, WxUserClickService wxUserClickService, WxUserService wxUserService, WxActivityService wxActivityService, WxUserFundsService wxUserFundsService) {
        this.wxPrizeDetailService = wxPrizeDetailService;
        this.wxUserClickService = wxUserClickService;
        this.wxUserService = wxUserService;
        this.wxActivityService = wxActivityService;
        this.wxUserFundsService = wxUserFundsService;
    }

    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService, WxSessionManager sessionManager) {

        String msg = String.format("type:%s, event:%s, key:%s", wxMessage.getMsgType(), wxMessage.getEvent(),
                wxMessage.getEventKey());
        this.logger.info("用户点击海报生成按钮: " + wxMessage.getFromUser() + " " + msg + "生成海报");
        // TODO 活动结束条件需明确
        AjaxJson activityJSON = wxActivityService.checkActivityByCondition(new WxActivity().setActStartTime(new Date()));
        if (activityJSON.isSuccess()) {
            Criteria<WxUserClick> criteria = new Criteria<>();
            criteria.add(Restrictions.eq("ucOpenid", wxMessage.getFromUser(), true));
            WxUserClick wxUserClick = wxUserClickService.findOne(criteria);
            Integer clickCount = 0;
            if (ObjectUtil.isEmpty(wxUserClick)) {
                wxUserClick = new WxUserClick();
                wxUserClick.setId(UUID.generate());
                wxUserClick.setUcCount(1);
                wxUserClick.setUcClickTime(new Date());
                wxUserClick.setUcClickType("00");
                wxUserClick.setUcOpenid(wxMessage.getFromUser());
                wxUserClick.setUcClickCount(1);
                Object obj = wxUserClickService.save(wxUserClick);
                logger.info(JacksonUtil.toJSONString(obj));
            } else {
                if (wxUserClick.getUcClickType().equals("01")) {
                    int count = wxUserClick.getUcCount() + 1;
                    if (count <= 5) {
                        wxUserClick.setUcCount(count);
                        wxUserClick.setUcClickTime(new Date());
                        wxUserClick.setUcClickType("00");
                    }
                }
                clickCount = wxUserClick.getUcClickCount() + 1;
                wxUserClick.setUcClickCount(clickCount);
                wxUserClickService.save(wxUserClick);
                if (wxUserClick.getUcClickType().equals("00") && clickCount > 1) {
                    return null;
                }
            }
            try {

                Criteria<WxUserClick> criteria1 = new Criteria<>();
                criteria1.add(Restrictions.eq("ucOpenid", wxMessage.getFromUser(), true));

                WxUserClick wxUserClick1 = wxUserClickService.findOne(criteria1);
                int count = wxUserClick1.getUcCount() + 1;
                if (count <= 5) {
                    wxUserClick1.setUcClickType("01");
                    wxUserClick1.setUcClickCount(0);
                    wxUserClickService.save(wxUserClick1);
                }

                WxOpenUser wxOpenUser = wxOpenService.getUserService().userInfo(wxMessage.getFromUser());

                //如不存在则新增
                Criteria<WxUser> wxUserCriteria = new Criteria<>();
                wxUserCriteria.add(Restrictions.eq("uOpenid", wxMessage.getFromUser(), true));
                WxUser wxUser = wxUserService.findOne(wxUserCriteria);
                if (null == wxUser) {
                    wxUser = wxUserService.save(new WxUser().
                            setuCreateTime(new Date())
                            .setuOpenid(wxOpenUser.getOpenId())
                            .setuNickname(wxOpenUser.getNickname()
                            ).setuProvince(wxOpenUser.getProvince())
                            .setuCity(wxOpenUser.getCity())
                            .setuCountry(wxOpenUser.getCountry())
                            .setuHeadimg(wxOpenUser.getHeadImgUrl())
                            .setuSex(wxOpenUser.getSex()).setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                            .setuUnionid(wxOpenUser.getUnionId())
                            );

                    Criteria<WxPrizeDetail> wxPrizeDetailCriteria = new Criteria<>();
                    wxPrizeDetailCriteria.add(Restrictions.eq("actId", ((WxActivity) activityJSON.getData()).getId(), true));
                    wxPrizeDetailCriteria.add(Restrictions.eq("pdCondition", "first", true));
                    wxPrizeDetailCriteria.add(Restrictions.eq("pdStatus", "01", true));

                    WxPrizeDetail firstWxPrizeDeetail = wxPrizeDetailService.findOne(wxPrizeDetailCriteria);

                    wxUserFundsService.save(new WxUserFunds().setUfCreatetime(new Date()).setUfMoney(firstWxPrizeDeetail.getPdItem()).setUfOpenid(wxOpenUser.getOpenId()).setUfType("00").setActId(((WxActivity) activityJSON.getData()).getId()));
                }


                // 获取永久二维码Ticket
                WxOpenQrCodeTicket wxOpenQrCodeTicket = wxOpenService.getQrCodeService().qrCodeCreateLastTicket(wxMessage.getFromUser());

                // 获取二维码图片
                File qrcodeFile = wxOpenService.getQrCodeService().qrCodePicture(wxOpenQrCodeTicket);
                AjaxJson ajaxJson = null;
                if (StringUtil.isNotBlank(wxUser.getuHeadimgLocal())) {
                    ajaxJson = wxActivityService.createPoster(wxUser.getuHeadimgLocal(), qrcodeFile, wxOpenUser.getNickname(), true, wxUser.getId());
                } else {
                    ajaxJson = wxActivityService.createPoster(wxOpenUser.getHeadImgUrl(), qrcodeFile, wxOpenUser.getNickname(), false, wxUser.getId());
                }

                // 上传微信素材
                WxMediaUploadResult wxMediaUploadResult = wxOpenService.getMaterialService().mediaUpload("image", (File) ajaxJson.getData());

                String mediaId = wxMediaUploadResult.getMediaId();
                WxOpenKefuMessage wxOpenKefuMessage1 = new WxOpenKefuMessage();
                wxOpenKefuMessage1.setToUser(wxMessage.getFromUser());
                wxOpenKefuMessage1.setMsgType(WxConsts.CUSTOM_MSG_IMAGE);
                wxOpenKefuMessage1.setMediaId(mediaId);
                wxOpenService.getKefuService().sendKefuMessage(wxOpenKefuMessage1);

                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, 3);
                String endTime = time.format(c.getTime());

                WxOpenKefuMessage wxOpenKefuMessage2 = new WxOpenKefuMessage();
                wxOpenKefuMessage2.setToUser(wxMessage.getFromUser());
                wxOpenKefuMessage2.setMsgType(WxConsts.CUSTOM_MSG_TEXT);

                wxOpenKefuMessage2.setContent("海报生成完毕,将于" + endTime + "失效");
                wxOpenService.getKefuService().sendKefuMessage(wxOpenKefuMessage2);


            } catch (Exception ignored) {
                System.out.println(ignored);
            }
        }
        return null;


    }

}