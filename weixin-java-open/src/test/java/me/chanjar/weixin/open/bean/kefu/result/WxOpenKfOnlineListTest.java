package me.chanjar.weixin.open.bean.kefu.result;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class WxOpenKfOnlineListTest {

    @Test
    public void testFromJson() {
        String json = "{\r\n" +
                "   \"kf_online_list\": [\r\n" +
                "       {\r\n" +
                "           \"kf_account\": \"test1@test\", \r\n" +
                "           \"status\": 1, \r\n" +
                "           \"kf_id\": \"1001\", \r\n" +
                "           \"auto_accept\": 0, \r\n" +
                "           \"accepted_case\": 1\r\n" +
                "       },\r\n" +
                "       {\r\n" +
                "           \"kf_account\": \"test2@test\", \r\n" +
                "           \"status\": 1, \r\n" +
                "           \"kf_id\": \"1002\", \r\n" +
                "           \"auto_accept\": 0, \r\n" +
                "           \"accepted_case\": 2\r\n" +
                "       }\r\n" +
                "   ]\r\n" +
                "}";

        WxOpenKfOnlineList wxOpenKfOnlineList = WxOpenKfOnlineList.fromJson(json);
        Assert.assertNotNull(wxOpenKfOnlineList);
        System.err.println(ToStringBuilder.reflectionToString(wxOpenKfOnlineList));

    }

}
