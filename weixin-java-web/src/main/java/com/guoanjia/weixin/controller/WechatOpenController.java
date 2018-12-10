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
import com.github.asherli0103.utils.DateUtil;
import com.github.asherli0103.utils.StringUtil;
import com.guoanjia.business.utils.qiniu.CdnUtil;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.component.WxOpenAuthorization;
import me.chanjar.weixin.open.bean.component.WxOpenPreAuthCode;
import me.chanjar.weixin.open.bean.datacube.WxDataCubeUserSummary;
import me.chanjar.weixin.open.bean.message.WxOpenXmlAuthorizeMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import me.chanjar.weixin.open.util.crypto.WxOpenCryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 微信请求控制层
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@RestController
@RequestMapping(value = {"service"})
public class WechatOpenController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final WxOpenService wxOpenService;


    private final WxOpenMessageRouter router;

    private WxOpenConfigStorage wxOpenConfigStorage;

    public WechatOpenController(WxOpenService wxOpenService, WxOpenMessageRouter router) {
        this.wxOpenService = wxOpenService;
        this.wxOpenConfigStorage = wxOpenService.getWxOpenConfigStorage();
        this.router = router;
    }

    @RequestMapping(path = "test")
    public List<WxDataCubeUserSummary> test() throws WxErrorException {
        return wxOpenService.getDataCubeService().getUserSummary(DateUtil.stringToDate("2017-01-18"),DateUtil.getCurrentDate());
    }

    /**
     * 微信开放平台 授权事件接收URL
     *
     * @param requestBody  授权加密消息
     * @param signature    签名
     * @param timestamp    时间戳
     * @param nonce        随机字符串
     * @param msgSignature 签名字符串
     * @return 成功返回微信{@code "success"}
     */
    @PostMapping(path = "/event/authorize", produces = "application/xml; charset=UTF-8")
    public String acceptAuthorizeEvent(@RequestBody String requestBody,
                                       @RequestParam("signature") String signature,
                                       @RequestParam("timestamp") String timestamp,
                                       @RequestParam("nonce") String nonce,
                                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n接收微信请求：[signature:{},msgSignature:{},timestamp:{},nonce:{}]\nrequestBody:\n{} ", signature, msgSignature, timestamp, nonce, requestBody);
        }
        if (!StringUtil.isNotBlank(msgSignature)) {
            return null;
        }

        boolean isValid = WxOpenCryptUtil.verifySignature(this.wxOpenConfigStorage.getComponentToken(), signature, timestamp, nonce);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("\n校验请求来源,{}", isValid);
        }
        if (isValid) {
            WxOpenXmlAuthorizeMessage wxOpenXmlAuthorizeMessage = WxOpenXmlAuthorizeMessage.fromEncryptedXml(requestBody, this.wxOpenConfigStorage, timestamp, nonce, msgSignature);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\n消息解密后内容为：\n{} ", wxOpenXmlAuthorizeMessage.toString());
            }
            //TODO ticket 是否有必要存在数据库中?

            //当前Ticket缓存至内存中
            this.wxOpenConfigStorage.updateComponentVerifyTicket(wxOpenXmlAuthorizeMessage.getComponentVerifyTicket());
            return "success";
        }
        return null;
    }

    /**
     * 微信管理员授权登陆页
     *
     * @param request  请求流
     * @param response 返回流
     * @throws WxErrorException 微信获取异常时抛出此异常
     * @throws IOException      重定向异常时抛出此异常
     */
    @GetMapping(path = "/componentLoginPage")
    public void componentLoginPage(HttpServletRequest request, HttpServletResponse response) throws IOException, WxErrorException {
        String redirect_uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/service/authorCallback";
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n授权页面回调地址：[redirect_uri:{}]", redirect_uri);
        }
        WxOpenPreAuthCode wxOpenPreAuthCode = null;
        try {
            wxOpenPreAuthCode = this.wxOpenService.getPreAuthCode();
        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            LOGGER.error("\n获取预授权码错误,{},\n{}", wxError.toString(), e.getMessage());
            throw new WxErrorException(e.getError());
        }
        String componentLoginPageUrl = this.wxOpenService.createComponentLoginPageUrl(wxOpenPreAuthCode.getPreAuthCode(), redirect_uri);
        response.sendRedirect(componentLoginPageUrl);
    }

    /**
     * 授权回调地址 TODO 待后期改为页面展示,优化体验效果
     *
     * @param auth_code  授权码
     * @param expires_in 授权码存活时间
     * @return 授权结果
     */
    @RequestMapping(path = "/authorCallback")
    public AjaxJson authorCallback(
            @RequestParam("auth_code") String auth_code,
            @RequestParam("expires_in") String expires_in) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n授权回调请求信息：[auth_code:{},expires_in:{}]", auth_code, expires_in);
        }
        AjaxJson ajaxJson = new AjaxJson();
        try {
            WxOpenAuthorization wxOpenAuthorization = this.wxOpenService.getAuthorization(auth_code);
            WxOpenAuthorization.WxOpenAuthorizationInfo wxOpenAuthorizerInfo = wxOpenAuthorization.getAuthorizationInfo();
            String access_token = wxOpenAuthorizerInfo.getAuthorizerAccessToken();
            String refresh_token = wxOpenAuthorizerInfo.getAuthorizerRefreshToken();
            int expires_time = wxOpenAuthorizerInfo.getExpiresIn();
            //授权成功强制更新access_token信息
            this.wxOpenConfigStorage.updateAccessToken(access_token, expires_time, refresh_token);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\n授权信息,{}", wxOpenAuthorization.toString());
                LOGGER.debug("\n授权信息必要参数,[access_token:{},expires_time:{},refresh_token:{}]", access_token, expires_time, refresh_token);
            }
            //TODO 该数据应保存至数据库防止重启数据丢失,Mysql?Redis?

        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            LOGGER.error("授权信息获取错误,{},\n{}", wxError.toString(), e.getMessage());
            ajaxJson.setSuccess(false).setCode(wxError.getErrorCode()).setMsg(wxError.getErrorMsg());
        }
        return ajaxJson;
    }


    /**
     * 微信信息回调接口
     *
     * @param requestBody  信息体
     * @param signature    签名
     * @param timestamp    时间戳
     * @param nonce        随机字符串
     * @param encryptType  加密类型
     * @param msgSignature 签名字符串
     * @return 回复微信信息
     */
    @PostMapping(path = "{appid}/callback", produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam(name = "encrypt_type", required = false) String encryptType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n接收微信请求：[{},{},{},{},{}]\n{} ", signature, encryptType, msgSignature, timestamp, nonce, requestBody);
        }
        if ("aes".equals(encryptType)) {
            WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedXml(requestBody, this.wxOpenConfigStorage, timestamp, nonce, msgSignature);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            }
            WxOpenXmlOutMessage outMessage = this.router.route(inMessage);
            if (outMessage == null) {
                return null;
            }
            String out = outMessage.toEncryptedXml(this.wxOpenConfigStorage);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("\n组装回复信息：{}", out);
            }
            return out;
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.debug("\n不可识别的消息加密类型：{}", encryptType);
        }
        return null;
    }

    /**
     * 获取临时素材微信存储地址
     *
     * @param mediaId 页面地址
     * @return 临时素材url
     * @throws WxErrorException 微信获取异常时抛出此异常
     */
    @CrossOrigin(maxAge = 3600)
    @RequestMapping(path = "/getMediaFile")
    public AjaxJson getMediaFile(@RequestParam String mediaId) throws WxErrorException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n获取临时素材微信存储地址请求:[mediaId:{}]", mediaId);
        }
        AjaxJson ajaxJson = new AjaxJson();
        File file = this.wxOpenService.getMaterialService().mediaDownload(mediaId);
        if(null != file){
            if (CdnUtil.cdnUploadFile(file.getName() ,file)){
                ajaxJson.setSuccess(true).setCode(200).setData(file.getName());
            }else{
                ajaxJson.setSuccess(false).setCode(201).setMsg("上传七牛存储失败！");
            }
        }else{
            ajaxJson.setSuccess(false).setCode(201).setMsg("获取微信临时素材失败！");
        }
        return ajaxJson;
    }
}
