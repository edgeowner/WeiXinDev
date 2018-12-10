package me.chanjar.weixin.open.api.impl;

import com.google.inject.Inject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.ApiTestModule;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.result.WxOpenQrCodeTicket;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.File;

/**
 * 测试用户相关的接口
 *
 * @author chanjarster
 */
@Test(groups = "qrCodeAPI")
@Guice(modules = ApiTestModule.class)
public class WxOpenQrCodeServiceImplTest {

    @Inject
    protected WxOpenService wxService;

    public void testQrCodeCreateTmpTicket() throws WxErrorException {
        WxOpenQrCodeTicket ticket = this.wxService.getQrCodeService().qrCodeCreateTmpTicket(1, null);
        Assert.assertNotNull(ticket.getUrl());
        Assert.assertNotNull(ticket.getTicket());
        Assert.assertTrue(ticket.getExpire_seconds() != -1);
        System.out.println(ticket);
    }

    public void testQrCodeCreateLastTicket() throws WxErrorException {
        WxOpenQrCodeTicket ticket = this.wxService.getQrCodeService().qrCodeCreateLastTicket(1);
        Assert.assertNotNull(ticket.getUrl());
        Assert.assertNotNull(ticket.getTicket());
        Assert.assertTrue(ticket.getExpire_seconds() == -1);
        System.out.println(ticket);
    }

    public void testQrCodePicture() throws WxErrorException {
        WxOpenQrCodeTicket ticket = this.wxService.getQrCodeService().qrCodeCreateLastTicket(1);
        File file = this.wxService.getQrCodeService().qrCodePicture(ticket);
        Assert.assertNotNull(file);
        System.out.println(file.getAbsolutePath());
    }

    public void testQrCodePictureUrl() throws WxErrorException {
        WxOpenQrCodeTicket ticket = this.wxService.getQrCodeService().qrCodeCreateLastTicket(1);
        String url = this.wxService.getQrCodeService().qrCodePictureUrl(ticket.getTicket());
        Assert.assertNotNull(url);
        System.out.println(url);
    }

}
