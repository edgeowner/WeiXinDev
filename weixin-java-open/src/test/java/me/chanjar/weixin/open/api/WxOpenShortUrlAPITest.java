package me.chanjar.weixin.open.api;

import com.google.inject.Inject;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * 测试短连接
 *
 * @author chanjarster
 */
@Test(groups = "shortURLAPI")
@Guice(modules = ApiTestModule.class)
public class WxOpenShortUrlAPITest {

    @Inject
    protected WxOpenService wxService;

    public void testShortUrl() throws WxErrorException {
        String shortUrl = this.wxService.getQrCodeService().shortUrl("www.baidu.com");
        Assert.assertNotNull(shortUrl);
    }

}
