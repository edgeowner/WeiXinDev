package com.guoanjia.weixin.controller;

import com.github.asherli0103.utils.NetWorkUtil;
import com.guoanjia.weixin.core.utils.JsonUtils;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenPayService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.pay.request.WxPayUnifiedOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Created by asherli on 17-2-25.
 */
@RestController
@RequestMapping(path = {"/wexFive"})
public class WexFiveController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final WxOpenPayService wxOpenPayService;
    private final WxOpenService wxOpenService;

    public WexFiveController(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
        this.wxOpenPayService = wxOpenService.getPayService();
    }

    @RequestMapping(path = "wexFiveJsApi")
    public String wexFiveJsApi(HttpServletRequest request, String action) throws WxErrorException, IOException {
        if (Objects.nonNull(action) && Objects.equals("getTicket", action)) {
            return getTicket();
        } else if (Objects.nonNull(action) && Objects.equals("chooseWXPay", action)) {
            return chooseWXPay(request);
        }
        return "";
    }


    /**
     * 获取JsapiTicket For Wex5 专用
     *
     * @return JSSDK Ticket
     * @throws WxErrorException 微信获取异常时抛出此异常
     */
    public String getTicket() throws WxErrorException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n获取JSSDK Ticket");
        }
        try {
            String jsTicket = this.wxOpenService.getJsapiTicket();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("JSSDK Ticket {}", jsTicket);
            }
            return jsTicket;
        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            LOGGER.error("获取网页授权用户OpenId失败,{},\n{}", wxError.toString(), e.getMessage());
            throw new WxErrorException(e.getError());
        }
    }

    /**
     * 微信公众号支付 For Wex5 专用接口
     *
     * @param request 请求流
     * @return 预支付结果
     * @throws WxErrorException 支付异常抛出
     * @throws IOException      IP地址异常抛出
     */
    public String chooseWXPay(HttpServletRequest request) throws WxErrorException, IOException {

        LOGGER.info("访问支付接口，参数为{}", request.getParameter("openId")+" -------------" );
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setOpenid(request.getParameter("openId"));
        wxPayUnifiedOrderRequest.setBody(request.getParameter("body"));
        wxPayUnifiedOrderRequest.setNotifyURL(request.getParameter("notifyUrl"));
        wxPayUnifiedOrderRequest.setOutTradeNo(request.getParameter("outTradeNo"));
        wxPayUnifiedOrderRequest.setSpbillCreateIp(NetWorkUtil.getIpAddress(request));
        wxPayUnifiedOrderRequest.setTotalFee(Integer.valueOf(request.getParameter("totalFee")));
        wxPayUnifiedOrderRequest.setTradeType("JSAPI");
        Map<String, String> result = wxOpenPayService.getPayInfo(wxPayUnifiedOrderRequest);
        LOGGER.info("返回结果{}", JsonUtils.toJson(result));
        return JsonUtils.toJson(result);
    }


}
