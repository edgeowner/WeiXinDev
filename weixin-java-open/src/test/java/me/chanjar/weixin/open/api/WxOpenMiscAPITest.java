package me.chanjar.weixin.open.api;

import com.google.inject.Inject;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author chanjarster
 */
@Test(groups = "miscAPI")
@Guice(modules = ApiTestModule.class)
public class WxOpenMiscAPITest {

    @Inject
    protected WxOpenService wxService;

    @Test
    public void testGetCallbackIP() throws WxErrorException {
        String[] ipArray = this.wxService.getCallbackIP();
        System.out.println(Arrays.toString(ipArray));
        Assert.assertNotNull(ipArray);
        Assert.assertNotEquals(ipArray.length, 0);
    }

}
