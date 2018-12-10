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
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.ObjectUtil;
import com.github.asherli0103.utils.RandomUtil;
import com.guoanjia.business.poster.controller.parameters.CheckVCodeParameters;
import com.guoanjia.business.poster.controller.parameters.SendVCodeParameters;
import com.guoanjia.business.base.entity.WxUser;
import com.guoanjia.business.base.entity.jpa.WxUserRepository;
import com.guoanjia.business.base.service.WxUserService;
import com.guoanjia.business.utils.ContextHolderUtils;
import com.guoanjia.business.utils.MessageUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxUserServiceImpl extends BaseServiceImpl<WxUser, String> implements WxUserService {

    private final WxUserRepository wxUserRepository;

    public WxUserServiceImpl(BaseRepository<WxUser, String> baseRepository, WxUserRepository wxUserRepository) {
        super(baseRepository);
        this.wxUserRepository = wxUserRepository;
    }

    @Override
    public AjaxJson sendVCode(SendVCodeParameters params) {

        AjaxJson ajaxJson = new AjaxJson();

        String code = RandomUtil.randomNumbers(6);

        String msg = "您的短信验证码为: " + code;

        String result = MessageUtils.messagePostRequest(msg, params.getPhoneNumber());
        if (result.contains("00")) {
            HttpSession session = ContextHolderUtils.getSession();
            session.setAttribute(params.getOpenid(), code);
           // MessageUtils.deleteSessionCode(session, params.getOpenid());
            ajaxJson.setMsg("验证短信发送成功,请注意查收");
            ajaxJson.setSuccess(true);
            return ajaxJson;
        }

        if (result.contains("40009")) {
            ajaxJson.setMsg("请输入手机号码");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }

        if (result.contains("40010")) {
            ajaxJson.setMsg("请输入正确的手机号码");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        ajaxJson.setMsg("短信发送失败请重试");
        ajaxJson.setSuccess(false);
        return ajaxJson;

    }

    @Override
    public AjaxJson checkVCode(CheckVCodeParameters params) {
        HttpSession session = ContextHolderUtils.getSession();
        String code = (String) session.getAttribute(params.getOpenid());
        AjaxJson ajaxJson = new AjaxJson();
        if (Objects.equals(code, params.getvCode())) {
            WxUser wxNUserSubscribe = new WxUser();
            wxNUserSubscribe.setuOpenid(params.getOpenid());
            Example<WxUser> example = Example.of(wxNUserSubscribe);
            wxNUserSubscribe = wxUserRepository.findOne(example);
            if (ObjectUtil.isNotEmpty(wxNUserSubscribe)) {
                wxNUserSubscribe.setuUserphone(params.getPhoneNumber());
                wxUserRepository.save(wxNUserSubscribe);
                ajaxJson.setMsg("设置成功");
                ajaxJson.setSuccess(true);
                return ajaxJson;
            }
            //写入用户
            wxNUserSubscribe = new WxUser();
            wxNUserSubscribe.setuOpenid(params.getOpenid());
            wxNUserSubscribe.setuCity(params.getCity());
            wxNUserSubscribe.setuCountry(params.getCoountry());
            wxNUserSubscribe.setuNickname(params.getNickname());
            wxNUserSubscribe.setuProvince(params.getProvince());
            wxNUserSubscribe.setuCreateTime(new Date());
            wxNUserSubscribe.setuUserphone(params.getPhoneNumber());
            Object obj = wxUserRepository.save(wxNUserSubscribe);
            if (ObjectUtil.isNotEmpty(obj)) {
                ajaxJson.setMsg("设置成功");
                ajaxJson.setSuccess(true);
                return ajaxJson;
            } else {
                ajaxJson.setMsg("设置失败");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }
        }
        ajaxJson.setMsg("验证码错误");
        ajaxJson.setSuccess(false);
        return ajaxJson;
    }

    @Override
    @Transactional(readOnly = true)
    public AjaxJson queryUserInfo(WxUser params) {
        AjaxJson ajaxJson = new AjaxJson();
        Example<WxUser> wxUserExample = Example.of(params);
        params = findOne(wxUserExample);

        // LOGGER.info(JacksonUtils.toJSONString(params));
        if (ObjectUtil.isEmpty(params)) {
            ajaxJson.setMsg("用户不存在");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        ajaxJson.setData(params);
        ajaxJson.setMsg("用户信息已获取");
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    @Override
    public Long getActivityUserCount(String actId) {
        Criteria<WxUser> wxUserCriteria = new Criteria<>();
        wxUserCriteria.add(Restrictions.eq("uType", actId, true));
        return wxUserRepository.count(wxUserCriteria);
    }

}
