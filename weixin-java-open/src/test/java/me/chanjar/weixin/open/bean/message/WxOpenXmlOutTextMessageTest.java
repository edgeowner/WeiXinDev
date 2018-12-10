package me.chanjar.weixin.open.bean.message;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class WxOpenXmlOutTextMessageTest {

    public void test() {
        WxOpenXmlOutTextMessage m = new WxOpenXmlOutTextMessage();
        m.setContent("content");
        m.setCreateTime(1122l);
        m.setFromUserName("from");
        m.setToUserName("to");

        String expected = "<xml>"
                + "<ToUserName><![CDATA[to]]></ToUserName>"
                + "<FromUserName><![CDATA[from]]></FromUserName>"
                + "<CreateTime>1122</CreateTime>"
                + "<MsgType><![CDATA[text]]></MsgType>"
                + "<Content><![CDATA[content]]></Content>"
                + "</xml>";
        System.out.println(m.toXml());
        Assert.assertEquals(m.toXml().replaceAll("\\s", ""), expected.replaceAll("\\s", ""));
    }

    public void testBuild() {
        WxOpenXmlOutTextMessage m = WxOpenXmlOutMessage.TEXT().content("content").fromUser("from").toUser("to").build();
        String expected = "<xml>"
                + "<ToUserName><![CDATA[to]]></ToUserName>"
                + "<FromUserName><![CDATA[from]]></FromUserName>"
                + "<CreateTime>1122</CreateTime>"
                + "<MsgType><![CDATA[text]]></MsgType>"
                + "<Content><![CDATA[content]]></Content>"
                + "</xml>";
        System.out.println(m.toXml());
        Assert.assertEquals(
                m
                        .toXml()
                        .replaceAll("\\s", "")
                        .replaceAll("<CreateTime>.*?</CreateTime>", ""),
                expected
                        .replaceAll("\\s", "")
                        .replaceAll("<CreateTime>.*?</CreateTime>", "")
        );

    }


}
