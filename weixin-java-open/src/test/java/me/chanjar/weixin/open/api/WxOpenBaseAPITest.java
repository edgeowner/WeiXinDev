package me.chanjar.weixin.open.api;

import com.google.inject.Inject;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * 基础API测试
 *
 * @author chanjarster
 */
@Test(groups = "baseAPI")
@Guice(modules = ApiTestModule.class)
public class WxOpenBaseAPITest {

    @Inject
    protected WxOpenService wxService;

    public void testRefreshAccessToken() throws WxErrorException {
        WxOpenConfigStorage configStorage = this.wxService.getWxOpenConfigStorage();
        String before = configStorage.getAccessToken();
        this.wxService.getAccessToken(false);
        String after = configStorage.getAccessToken();
        Assert.assertNotEquals(before, after);
        Assert.assertTrue(StringUtils.isNotBlank(after));
    }

}
