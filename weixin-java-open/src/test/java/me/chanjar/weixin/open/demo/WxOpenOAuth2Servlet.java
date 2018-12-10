package me.chanjar.weixin.open.demo;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.component.WxOpenOAuth2AccessToken;
import me.chanjar.weixin.open.bean.result.WxOpenUser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WxOpenOAuth2Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected WxOpenService wxOpenService;

    public WxOpenOAuth2Servlet(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String code = request.getParameter("code");
        try {
            response.getWriter().println("<h1>code</h1>");
            response.getWriter().println(code);

            WxOpenOAuth2AccessToken wxOpenOAuth2AccessToken = this.wxOpenService.getOAuth2AccessToken(code);
            response.getWriter().println("<h1>access token</h1>");
            response.getWriter().println(wxOpenOAuth2AccessToken.toString());

            WxOpenUser wxOpenUser = this.wxOpenService.oauth2getUserInfo(wxOpenOAuth2AccessToken, null);
            response.getWriter().println("<h1>user info</h1>");
            response.getWriter().println(wxOpenUser.toString());

            wxOpenOAuth2AccessToken = this.wxOpenService.refreshOauth2AccessToken(wxOpenOAuth2AccessToken.getRefreshToken());
            response.getWriter().println("<h1>after refresh</h1>");
            response.getWriter().println(wxOpenOAuth2AccessToken.toString());

        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        response.getWriter().flush();
        response.getWriter().close();

    }

}
