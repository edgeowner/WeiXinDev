package me.chanjar.weixin.open.demo;

import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Daniel Qian
 */
public class WxOpenEndpointServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected WxOpenConfigStorage wxOpenConfigStorage;
    protected WxOpenService wxOpenService;
    protected WxOpenMessageRouter wxOpenMessageRouter;

    public WxOpenEndpointServlet(WxOpenConfigStorage wxOpenConfigStorage, WxOpenService wxOpenService,
                                 WxOpenMessageRouter wxOpenMessageRouter) {
        this.wxOpenConfigStorage = wxOpenConfigStorage;
        this.wxOpenService = wxOpenService;
        this.wxOpenMessageRouter = wxOpenMessageRouter;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!this.wxOpenService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
                "raw" :
                request.getParameter("encrypt_type");

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromXml(request.getInputStream());
            WxOpenXmlOutMessage outMessage = this.wxOpenMessageRouter.route(inMessage);
            if (outMessage != null) {
                response.getWriter().write(outMessage.toXml());
            }
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedXml(request.getInputStream(), this.wxOpenConfigStorage, timestamp, nonce, msgSignature);
            WxOpenXmlOutMessage outMessage = this.wxOpenMessageRouter.route(inMessage);
            response.getWriter().write(outMessage.toEncryptedXml(this.wxOpenConfigStorage));
            return;
        }

        response.getWriter().println("不可识别的加密类型");
        return;
    }

}
