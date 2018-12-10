package me.chanjar.weixin.open.demo;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.WxOpenMessageHandler;
import me.chanjar.weixin.open.api.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.io.InputStream;

public class WxOpenDemoServer {

    private static WxOpenConfigStorage wxOpenConfigStorage;
    private static WxOpenService wxOpenService;
    private static WxOpenMessageRouter wxOpenMessageRouter;

    public static void main(String[] args) throws Exception {
        initWeixin();

        Server server = new Server(8080);

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        ServletHolder endpointServletHolder = new ServletHolder(
                new WxOpenEndpointServlet(wxOpenConfigStorage, wxOpenService,
                        wxOpenMessageRouter));
        servletHandler.addServletWithMapping(endpointServletHolder, "/*");

        ServletHolder oauthServletHolder = new ServletHolder(
                new WxOpenOAuth2Servlet(wxOpenService));
        servletHandler.addServletWithMapping(oauthServletHolder, "/oauth2/*");

        server.start();
        server.join();
    }

    private static void initWeixin() {
        try (InputStream is1 = ClassLoader
                .getSystemResourceAsStream("test-config.xml")) {
            WxOpenDemoInMemoryConfigStorage config = WxOpenDemoInMemoryConfigStorage
                    .fromXml(is1);

            wxOpenConfigStorage = config;
            wxOpenService = new WxOpenServiceImpl();
            wxOpenService.setWxOpenConfigStorage(config);

            WxOpenMessageHandler logHandler = new DemoLogHandler();
            WxOpenMessageHandler textHandler = new DemoTextHandler();
            WxOpenMessageHandler imageHandler = new DemoImageHandler();
            WxOpenMessageHandler oauth2handler = new DemoOAuth2Handler();
            DemoGuessNumberHandler guessNumberHandler = new DemoGuessNumberHandler();

            wxOpenMessageRouter = new WxOpenMessageRouter(wxOpenService);
            wxOpenMessageRouter.rule().handler(logHandler).next().rule()
                    .msgType(WxConsts.XML_MSG_TEXT).matcher(guessNumberHandler)
                    .handler(guessNumberHandler).end().rule().async(false).content("哈哈")
                    .handler(textHandler).end().rule().async(false).content("图片")
                    .handler(imageHandler).end().rule().async(false).content("oauth")
                    .handler(oauth2handler).end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
