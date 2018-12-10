package me.chanjar.weixin.open.bean.message;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class WxOpenXmlOutImageMessageTest {

    public void test() {
        WxOpenXmlOutImageMessage m = new WxOpenXmlOutImageMessage();
        m.setMediaId("ddfefesfsdfef");
        m.setCreateTime(1122l);
        m.setFromUserName("from");
        m.setToUserName("to");

        String expected = "<xml>"
                + "<ToUserName><![CDATA[to]]></ToUserName>"
                + "<FromUserName><![CDATA[from]]></FromUserName>"
                + "<CreateTime>1122</CreateTime>"
                + "<MsgType><![CDATA[image]]></MsgType>"
                + "<Image><MediaId><![CDATA[ddfefesfsdfef]]></MediaId></Image>"
                + "</xml>";
        System.out.println(m.toXml());
        Assert.assertEquals(m.toXml().replaceAll("\\s", ""), expected.replaceAll("\\s", ""));
    }

    public void testBuild() {
        WxOpenXmlOutImageMessage m = WxOpenXmlOutMessage.IMAGE().mediaId("ddfefesfsdfef").fromUser("from").toUser("to").build();
        String expected = "<xml>"
                + "<ToUserName><![CDATA[to]]></ToUserName>"
                + "<FromUserName><![CDATA[from]]></FromUserName>"
                + "<CreateTime>1122</CreateTime>"
                + "<MsgType><![CDATA[image]]></MsgType>"
                + "<Image><MediaId><![CDATA[ddfefesfsdfef]]></MediaId></Image>"
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
