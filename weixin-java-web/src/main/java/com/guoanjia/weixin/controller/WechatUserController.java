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

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.utils.StringUtil;
import com.google.gson.JsonObject;
import com.guoanjia.business.base.entity.WxUser;
import com.guoanjia.business.base.entity.WxUserSubscribe;
import com.guoanjia.business.base.service.WxUserService;
import com.guoanjia.business.base.service.WxUserSubscribeService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.WxOpenKefuService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxOpenUserService;
import me.chanjar.weixin.open.bean.component.WxOpenOAuth2AccessToken;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;
import me.chanjar.weixin.open.bean.result.WxOpenUser;
import me.chanjar.weixin.open.bean.result.WxOpenUserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@RestController
@RequestMapping(path = {"/user"})
public class WechatUserController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final WxOpenService wxOpenService;
    private final WxUserService wxUserService;
    private WxOpenConfigStorage wxOpenConfigStorage;
    private WxOpenUserService wxOpenUserService;
    private final WxUserSubscribeService wxUserSubscribeService;
    private JedisPool jedisPool;

    public WechatUserController(WxOpenService wxOpenService, WxUserService wxUserService, WxUserSubscribeService wxUserSubscribeService) {
        this.wxOpenService = wxOpenService;
        this.wxUserService = wxUserService;
        this.wxOpenConfigStorage = wxOpenService.getWxOpenConfigStorage();
        this.wxUserSubscribeService = wxUserSubscribeService;
        this.jedisPool = this.wxOpenConfigStorage.getJedisPool();
        this.wxOpenUserService = this.wxOpenService.getUserService();
    }

    @PostMapping(path = {"currentVisitCount"})
    public Boolean currentVisitCount() {
        int key = 0;
        try (Jedis jedis = jedisPool.getResource()) {
            key = Integer.valueOf(jedis.get("CURRENT_VISIT_COUNT"));
        }

        int staticKey = 0;
        try (Jedis jedis = jedisPool.getResource()) {
            staticKey = Integer.valueOf(jedis.get("CURRENT_VISIT_COUNT_STATIC"));
        }

        if (key >= staticKey) {
            return false;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set("CURRENT_VISIT_COUNT", String.valueOf(key + 1));
        }
        return true;
    }

    /**
     * 获取微信用户网页授权码
     *
     * @param request     请求流
     * @param response    返回流
     * @param redirectUrl 回调地址
     * @param scope       授权种类
     * @param state       识别码
     * @throws IOException 重定向异常时抛出此异常
     */
    @GetMapping(path = "/getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "redirect_url") String redirectUrl, @RequestParam(name = "scope", required = false) String scope, @RequestParam(name = "state", required = false) String state) throws IOException {
        String redirect_uri = request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/user/getUserInfo?redirect_url=" + redirectUrl;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n用户授权码请求信息：[redirect_url:{},scope:{},state:{}]", redirect_uri, scope, state);
        }
        if (StringUtil.isBlank(scope)) {
            scope = WxConsts.OAUTH2_SCOPE_USER_INFO;
        } else if (Objects.equals(scope, WxConsts.OAUTH2_SCOPE_BASE)) {
            redirect_uri = request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/user/getUserOpenId?redirect_url=" + redirectUrl;
        }

        if (StringUtil.isBlank(state)) {
            state = String.valueOf(new Random().nextInt(10000));
        }
        String authorizationUrl = this.wxOpenService.oauth2buildAuthorizationUrl(redirect_uri, scope, state);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("\n用户授权码请求地址：[authorizationUrl:{}]", authorizationUrl);
        }
        response.sendRedirect(authorizationUrl);
    }


    /**
     * 获取微信用户信息
     *
     * @param code 授权码
     * @return 用户信息
     */
    @RequestMapping(path = "getUserInfo")
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "redirect_url") String redirectUrl, @RequestParam(name = "code") String code) throws IOException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n接收用户信息获取请求:[redirect_url:{},code:{}]", redirectUrl, code);
        }
        try {
            WxOpenOAuth2AccessToken wxOpenOAuth2AccessToken = this.wxOpenService.getOAuth2AccessToken(code);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\n获取用户AccessToken：\n{} ", wxOpenOAuth2AccessToken.toString());
            }
            WxOpenUser wxOpenUser = new WxOpenUser();
            Example<WxUser> wxUserExample = Example.of(new WxUser().setuOpenid(wxOpenOAuth2AccessToken.getOpenId()));
            Boolean b = this.wxUserService.exists(wxUserExample);
            if (b) {
                WxUser wxUser = this.wxUserService.findOne(wxUserExample);
                wxOpenUser = new WxOpenUser().setOpenId(wxUser.getuOpenid()).setCity(wxUser.getuCity()).setCountry(wxUser.getuCountry()).setHeadImgUrl(wxUser.getuHeadimg()).setNickname(wxUser.getuNickname()).setProvince(wxUser.getuProvince()).setSex(wxUser.getuSex());
            } else {
                wxOpenUser = this.wxOpenService.oauth2getUserInfo(wxOpenOAuth2AccessToken, null);
                this.wxUserService.save(new WxUser()
                        .setuOpenid(wxOpenUser.getOpenId())
                        .setuHeadimg(wxOpenUser.getHeadImgUrl())
                        .setuCity(wxOpenUser.getCity())
                        .setuCountry(wxOpenUser.getCountry())
                        .setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                        .setuProvince(wxOpenUser.getProvince())
                        .setuNickname(wxOpenUser.getNickname())
                        .setuSex(wxOpenUser.getSex())
                        .setuUnionid(wxOpenUser.getUnionId())
                );
            }
//            AjaxJson userAjax = this.wxUserService.queryUserInfo(new WxUser().setuOpenid(wxOpenUser.getOpenId()));
//            if (!userAjax.isSuccess()) {
//                this.wxUserService.save(new WxUser()
//                        .setuOpenid(wxOpenUser.getOpenId())
//                        .setuHeadimg(wxOpenUser.getHeadImgUrl())
//                        .setuCity(wxOpenUser.getCity())
//                        .setuCountry(wxOpenUser.getCountry())
//                        .setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
//                        .setuProvince(wxOpenUser.getProvince())
//                        .setuNickname(wxOpenUser.getNickname())
//                        .setuSex(wxOpenUser.getSex())
//                        .setuUnionid(wxOpenUser.getUnionId())
//                );
//            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\n获取用户信息：\n{} ", wxOpenUser.toString());
            }


            response.sendRedirect(redirectUrl + "?subscribe=" + wxOpenUser.getSubscribe() + "&openid=" + wxOpenUser.getOpenId() +
                    "&headimgurl=" + wxOpenUser.getHeadImgUrl() +
                    "&nickname=" + URLEncoder.encode(wxOpenUser.getNickname(), "UTF-8") +
                    "&city=" + URLEncoder.encode(wxOpenUser.getCity(), "UTF-8") +
                    "&province=" + URLEncoder.encode(wxOpenUser.getProvince(), "UTF-8") +
                    "&country=" + URLEncoder.encode(wxOpenUser.getCountry(), "UTF-8") +
                    "&sex=" + URLEncoder.encode(wxOpenUser.getSex(), "UTF-8") +
                    "&error=false"
            );

        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            LOGGER.error("获取网页授权用户信息失败,{},\n{}", wxError.toString(), e.getMessage());

            response.sendRedirect(redirectUrl + "?error=true");
        }
    }

    /**
     * 获取用户OpenId
     *
     * @param code 授权码
     * @return 用户OpenId
     */
    @RequestMapping(path = "getUserOpenId")
    public void getUserOpenId(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "redirect_url") String redirectUrl, @RequestParam(name = "code") String code) throws IOException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n接收用户OpenId获取请求:[code:{}]", code);
        }
        AjaxJson ajaxJson = new AjaxJson();
        if (StringUtil.isNotBlank(code)) {
            try {
                WxOpenOAuth2AccessToken wxOpenOAuth2AccessToken = this.wxOpenService.getOAuth2AccessToken(code);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("\n获取用户AccessToken：\n{} ", wxOpenOAuth2AccessToken.toString());
                }

                int key = 0;
                try (Jedis jedis = jedisPool.getResource()) {
                    key = Integer.valueOf(jedis.get("CURRENT_VISIT_COUNT"));
                }
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.set("CURRENT_VISIT_COUNT", String.valueOf(key - 1));
                }
                LOGGER.info("当前key:  ---------------------------------------------------------          " + key);

                Example<WxUserSubscribe> wxUserExample = Example.of(new WxUserSubscribe().setuOpenid(wxOpenOAuth2AccessToken.getOpenId()));
                Boolean b = this.wxUserSubscribeService.exists(wxUserExample);
                if (b) {
                    response.sendRedirect(redirectUrl + "?openid=" + wxOpenOAuth2AccessToken.getOpenId() + "&subscribe=true&error=false");
                } else {
                    response.sendRedirect(redirectUrl + "?openid=" + wxOpenOAuth2AccessToken.getOpenId() + "&subscribe=false&error=false");
                }
            } catch (WxErrorException e) {
                WxError wxError = e.getError();
                LOGGER.error("获取网页授权用户OpenId失败,{},\n{}", wxError.toString(), e.getMessage());
                if(wxError.getErrorCode()!=40029) {
                    int key = 0;
                    try (Jedis jedis = jedisPool.getResource()) {
                        key = Integer.valueOf(jedis.get("CURRENT_VISIT_COUNT"));
                    }

                    if (key > 0) {
                        try (Jedis jedis = jedisPool.getResource()) {
                            jedis.set("CURRENT_VISIT_COUNT", String.valueOf(key - 1));
                        }
                    }
                }
                response.sendRedirect(redirectUrl + "?error=true");
            }
        } else {
            LOGGER.error("获取网页授权用户OpenId失败,缺少必要参数");
            int key = 0;
            try (Jedis jedis = jedisPool.getResource()) {
                key = Integer.valueOf(jedis.get("CURRENT_VISIT_COUNT"));
            }

            if (key > 0) {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.set("CURRENT_VISIT_COUNT", String.valueOf(key - 1));
                }
            }
            response.sendRedirect(redirectUrl + "?error=true");
        }
    }

    /**
     * 获取微信用户信息,For Wex5JS专用
     *
     * @param code 授权码
     * @return 用户信息
     */
    @GetMapping(path = "userinfo")
    public JsonObject userinfo(@RequestParam(name = "code") String code) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n接收用户信息获取请求:[code:{}]", code);
        }
        if (StringUtil.isBlank(code)) {
            return new JsonObject();
        }
        JsonObject object = new JsonObject();
        try {
            WxOpenOAuth2AccessToken wxOpenOAuth2AccessToken = this.wxOpenService.getOAuth2AccessToken(code);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\n获取用户AccessToken：\n{} ", wxOpenOAuth2AccessToken.toString());
            }
            WxOpenUser wxOpenUser = this.wxOpenService.oauth2getUserInfo(wxOpenOAuth2AccessToken, null);
            AjaxJson userAjax = this.wxUserService.queryUserInfo(new WxUser().setuOpenid(wxOpenUser.getOpenId()));
            if (!userAjax.isSuccess()) {
                this.wxUserService.save(new WxUser()
                        .setuOpenid(wxOpenUser.getOpenId())
                        .setuHeadimg(wxOpenUser.getHeadImgUrl())
                        .setuCity(wxOpenUser.getCity())
                        .setuCountry(wxOpenUser.getCountry())
                        .setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                        .setuProvince(wxOpenUser.getProvince())
                        .setuNickname(wxOpenUser.getNickname())
                        .setuSex(wxOpenUser.getSex())
                        .setuUnionid(wxOpenUser.getUnionId())
                );
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\n获取用户信息：\n{} ", wxOpenUser.toString());
            }
            object.addProperty("openid", wxOpenUser.getOpenId());
            object.addProperty("nickname", wxOpenUser.getNickname());
            object.addProperty("country", wxOpenUser.getCountry());
            object.addProperty("province", wxOpenUser.getProvince());
            object.addProperty("city", wxOpenUser.getCity());
            object.addProperty("headimgurl", wxOpenUser.getHeadImgUrl());
            return object;
        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            LOGGER.error("获取网页授权用户信息失败,{},\n{}", wxError.toString(), e.getMessage());
            return new JsonObject();
        }
    }

    @PostMapping(params = {"initUser"})
    public String initUser( ) {
        try {
            return getUserList(null);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String getUserList(String nextOpenid) throws WxErrorException {
        WxOpenUserList wxOpenUserList = wxOpenUserService.userList(nextOpenid);
        List<String> openIds =  wxOpenUserList.getOpenIds();
        List<String> openidList = new ArrayList<>();
        List<WxOpenUser> wxOpenUsers = new ArrayList<>();


        for (int i=1;i<openIds.size();i++){
            openidList.add(openIds.get(i-1));
            if(i%100==0|| (openIds.size()-i<100)){
                wxOpenUsers = wxOpenUserService.userInfoList(openidList);
                List<WxUser> wxUsers = new ArrayList<>();
                List<WxUserSubscribe> wxUserSubscribes = new ArrayList<>();
                for (WxOpenUser wxOpenUser:wxOpenUsers){
                    WxUser wxUser = new WxUser().setuCity(wxOpenUser.getCity()).setuCountry(wxOpenUser.getCountry())
                            .setuCreateTime(new Date()).setuHeadimg(wxOpenUser.getHeadImgUrl())
                            .setuNickname(wxOpenUser.getNickname())
                            .setuOpenid(wxOpenUser.getOpenId())
                            .setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                            .setuProvince(wxOpenUser.getProvince()).setuSex(wxOpenUser.getSex())
                            .setuUnionid(wxOpenUser.getUnionId());
                    WxUserSubscribe wxUserSubscribe = new WxUserSubscribe().setuCity(wxOpenUser.getCity()).setuCountry(wxOpenUser.getCountry())
                            .setuCreateTime(new Date()).setuHeadimg(wxOpenUser.getHeadImgUrl())
                            .setuNickname(wxOpenUser.getNickname())
                            .setuOpenid(wxOpenUser.getOpenId())
                            .setuPrivilege(Arrays.toString(wxOpenUser.getPrivilege()))
                            .setuProvince(wxOpenUser.getProvince()).setuSex(wxOpenUser.getSex())
                            .setuUnionid(wxOpenUser.getUnionId());
                    wxUsers.add(wxUser);
                    wxUserSubscribes.add(wxUserSubscribe);
                }

                wxUserSubscribeService.batchSave(wxUserSubscribes);

                wxUserService.batchSave(wxUsers);
                openidList.clear();
            }
        }



        String next_openid = wxOpenUserList.getNextOpenId();
        if (StringUtil.isNotBlank(next_openid)){
            getUserList(next_openid);
        }
        return "FINISH";
    }

    @PostMapping(params = {"test"})
    public Boolean send() throws WxErrorException {
       WxOpenKefuService wxOpenKefuService =  wxOpenService.getKefuService();

        WxOpenKefuMessage wxOpenKefuMessage= new WxOpenKefuMessage();
        wxOpenKefuMessage.setContent("ceshi");
        wxOpenKefuMessage.setToUser("o8awnwilwxhF4EeocRDN-1w2EZOc");
        wxOpenKefuMessage.setMsgType(WxConsts.CUSTOM_MSG_TEXT);

       return wxOpenKefuService.sendKefuMessage(wxOpenKefuMessage);
    }


}
