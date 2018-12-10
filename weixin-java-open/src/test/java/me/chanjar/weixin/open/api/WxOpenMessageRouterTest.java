package me.chanjar.weixin.open.api;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.StandardSessionManager;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * 测试消息路由器
 *
 * @author chanjarster
 */
@Test
public class WxOpenMessageRouterTest {

    @Test(enabled = false)
    public void prepare(boolean async, StringBuffer sb, WxOpenMessageRouter router) {
        router
                .rule()
                .async(async)
                .msgType(WxConsts.XML_MSG_TEXT).event(WxConsts.EVT_CLICK).eventKey("KEY_1").content("CONTENT_1")
                .handler(new WxEchoOpenMessageHandler(sb, "COMBINE_4"))
                .end()
                .rule()
                .async(async)
                .msgType(WxConsts.XML_MSG_TEXT).event(WxConsts.EVT_CLICK).eventKey("KEY_1")
                .handler(new WxEchoOpenMessageHandler(sb, "COMBINE_3"))
                .end()
                .rule()
                .async(async)
                .msgType(WxConsts.XML_MSG_TEXT).event(WxConsts.EVT_CLICK)
                .handler(new WxEchoOpenMessageHandler(sb, "COMBINE_2"))
                .end()
                .rule().async(async).msgType(WxConsts.XML_MSG_TEXT).handler(new WxEchoOpenMessageHandler(sb, WxConsts.XML_MSG_TEXT)).end()
                .rule().async(async).event(WxConsts.EVT_CLICK).handler(new WxEchoOpenMessageHandler(sb, WxConsts.EVT_CLICK)).end()
                .rule().async(async).eventKey("KEY_1").handler(new WxEchoOpenMessageHandler(sb, "KEY_1")).end()
                .rule().async(async).content("CONTENT_1").handler(new WxEchoOpenMessageHandler(sb, "CONTENT_1")).end()
                .rule().async(async).rContent(".*bc.*").handler(new WxEchoOpenMessageHandler(sb, "abcd")).end()
                .rule().async(async).matcher(new WxOpenMessageMatcher() {
            @Override
            public boolean match(WxOpenXmlMessage message) {
                return "strangeformat".equals(message.getFormat());
            }
        }).handler(new WxEchoOpenMessageHandler(sb, "matcher")).end()
                .rule().async(async).handler(new WxEchoOpenMessageHandler(sb, "ALL")).end();
    }

    @Test(dataProvider = "messages-1")
    public void testSync(WxOpenXmlMessage message, String expected) {
        StringBuffer sb = new StringBuffer();
        WxOpenMessageRouter router = new WxOpenMessageRouter(null);
        prepare(false, sb, router);
        router.route(message);
        Assert.assertEquals(sb.toString(), expected);
    }

    @Test(dataProvider = "messages-1")
    public void testAsync(WxOpenXmlMessage message, String expected) throws InterruptedException {
        StringBuffer sb = new StringBuffer();
        WxOpenMessageRouter router = new WxOpenMessageRouter(null);
        prepare(true, sb, router);
        router.route(message);
        Thread.sleep(500l);
        Assert.assertEquals(sb.toString(), expected);
    }

    public void testConcurrency() throws InterruptedException {
        final WxOpenMessageRouter router = new WxOpenMessageRouter(null);
        router.rule().handler(new WxOpenMessageHandler() {
            @Override
            public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService,
                                              WxSessionManager sessionManager) {
                return null;
            }
        }).end();

        final WxOpenXmlMessage m = new WxOpenXmlMessage();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                router.route(m);
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                }
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(r).start();
        }

        Thread.sleep(1000l * 2);
    }

    @DataProvider(name = "messages-1")
    public Object[][] messages2() {
        WxOpenXmlMessage message1 = new WxOpenXmlMessage();
        message1.setMsgType(WxConsts.XML_MSG_TEXT);

        WxOpenXmlMessage message2 = new WxOpenXmlMessage();
        message2.setEvent(WxConsts.EVT_CLICK);

        WxOpenXmlMessage message3 = new WxOpenXmlMessage();
        message3.setEventKey("KEY_1");

        WxOpenXmlMessage message4 = new WxOpenXmlMessage();
        message4.setContent("CONTENT_1");

        WxOpenXmlMessage message5 = new WxOpenXmlMessage();
        message5.setContent("BLA");

        WxOpenXmlMessage message6 = new WxOpenXmlMessage();
        message6.setContent("abcd");

        WxOpenXmlMessage message7 = new WxOpenXmlMessage();
        message7.setFormat("strangeformat");

        WxOpenXmlMessage c2 = new WxOpenXmlMessage();
        c2.setMsgType(WxConsts.XML_MSG_TEXT);
        c2.setEvent(WxConsts.EVT_CLICK);

        WxOpenXmlMessage c3 = new WxOpenXmlMessage();
        c3.setMsgType(WxConsts.XML_MSG_TEXT);
        c3.setEvent(WxConsts.EVT_CLICK);
        c3.setEventKey("KEY_1");

        WxOpenXmlMessage c4 = new WxOpenXmlMessage();
        c4.setMsgType(WxConsts.XML_MSG_TEXT);
        c4.setEvent(WxConsts.EVT_CLICK);
        c4.setEventKey("KEY_1");
        c4.setContent("CONTENT_1");

        return new Object[][]{
                new Object[]{message1, WxConsts.XML_MSG_TEXT + ","},
                new Object[]{message2, WxConsts.EVT_CLICK + ","},
                new Object[]{message3, "KEY_1,"},
                new Object[]{message4, "CONTENT_1,"},
                new Object[]{message5, "ALL,"},
                new Object[]{message6, "abcd,"},
                new Object[]{message7, "matcher,"},
                new Object[]{c2, "COMBINE_2,"},
                new Object[]{c3, "COMBINE_3,"},
                new Object[]{c4, "COMBINE_4,"}
        };

    }

    @DataProvider
    public Object[][] standardSessionManager() {

        // 故意把session存活时间变短，清理更频繁
        StandardSessionManager ism = new StandardSessionManager();
        ism.setMaxInactiveInterval(1);
        ism.setProcessExpiresFrequency(1);
        ism.setBackgroundProcessorDelay(1);

        return new Object[][]{
                new Object[]{ism}
        };

    }

    @Test(dataProvider = "standardSessionManager")
    public void testSessionClean1(StandardSessionManager ism) throws InterruptedException {

        // 两个同步请求，看是否处理完毕后会被清理掉
        final WxOpenMessageRouter router = new WxOpenMessageRouter(null);
        router.setSessionManager(ism);
        router
                .rule().async(false).handler(new WxSessionMessageHandler()).next()
                .rule().async(false).handler(new WxSessionMessageHandler()).end();

        WxOpenXmlMessage msg = new WxOpenXmlMessage();
        msg.setFromUser("abc");
        router.route(msg);

        Thread.sleep(2000l);
        Assert.assertEquals(ism.getActiveSessions(), 0);

    }

    @Test(dataProvider = "standardSessionManager")
    public void testSessionClean2(StandardSessionManager ism) throws InterruptedException {

        // 1个同步,1个异步请求，看是否处理完毕后会被清理掉
        {
            final WxOpenMessageRouter router = new WxOpenMessageRouter(null);
            router.setSessionManager(ism);
            router
                    .rule().async(false).handler(new WxSessionMessageHandler()).next()
                    .rule().async(true).handler(new WxSessionMessageHandler()).end();

            WxOpenXmlMessage msg = new WxOpenXmlMessage();
            msg.setFromUser("abc");
            router.route(msg);

            Thread.sleep(2000l);
            Assert.assertEquals(ism.getActiveSessions(), 0);
        }
        {
            final WxOpenMessageRouter router = new WxOpenMessageRouter(null);
            router.setSessionManager(ism);
            router
                    .rule().async(true).handler(new WxSessionMessageHandler()).next()
                    .rule().async(false).handler(new WxSessionMessageHandler()).end();

            WxOpenXmlMessage msg = new WxOpenXmlMessage();
            msg.setFromUser("abc");
            router.route(msg);

            Thread.sleep(2000l);
            Assert.assertEquals(ism.getActiveSessions(), 0);
        }

    }

    @Test(dataProvider = "standardSessionManager")
    public void testSessionClean3(StandardSessionManager ism) throws InterruptedException {

        // 2个异步请求，看是否处理完毕后会被清理掉
        final WxOpenMessageRouter router = new WxOpenMessageRouter(null);
        router.setSessionManager(ism);
        router
                .rule().async(true).handler(new WxSessionMessageHandler()).next()
                .rule().async(true).handler(new WxSessionMessageHandler()).end();

        WxOpenXmlMessage msg = new WxOpenXmlMessage();
        msg.setFromUser("abc");
        router.route(msg);

        Thread.sleep(2000l);
        Assert.assertEquals(ism.getActiveSessions(), 0);

    }

    @Test(dataProvider = "standardSessionManager")
    public void testSessionClean4(StandardSessionManager ism) throws InterruptedException {

        // 一个同步请求，看是否处理完毕后会被清理掉
        {
            final WxOpenMessageRouter router = new WxOpenMessageRouter(null);
            router.setSessionManager(ism);
            router
                    .rule().async(false).handler(new WxSessionMessageHandler()).end();

            WxOpenXmlMessage msg = new WxOpenXmlMessage();
            msg.setFromUser("abc");
            router.route(msg);

            Thread.sleep(2000l);
            Assert.assertEquals(ism.getActiveSessions(), 0);
        }

        {
            final WxOpenMessageRouter router = new WxOpenMessageRouter(null);
            router.setSessionManager(ism);
            router
                    .rule().async(true).handler(new WxSessionMessageHandler()).end();

            WxOpenXmlMessage msg = new WxOpenXmlMessage();
            msg.setFromUser("abc");
            router.route(msg);

            Thread.sleep(2000l);
            Assert.assertEquals(ism.getActiveSessions(), 0);
        }
    }

    public static class WxEchoOpenMessageHandler implements WxOpenMessageHandler {

        private StringBuffer sb;
        private String echoStr;

        public WxEchoOpenMessageHandler(StringBuffer sb, String echoStr) {
            this.sb = sb;
            this.echoStr = echoStr;
        }

        @Override
        public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService,
                                          WxSessionManager sessionManager) {
            this.sb.append(this.echoStr).append(',');
            return null;
        }

    }

    public static class WxSessionMessageHandler implements WxOpenMessageHandler {

        @Override
        public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService,
                                          WxSessionManager sessionManager) {
            sessionManager.getSession(wxMessage.getFromUser());
            return null;
        }

    }

}
