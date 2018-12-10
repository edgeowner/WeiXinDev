package me.chanjar.weixin.open.api.impl;

import com.google.inject.Inject;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.ApiTestModule;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxXmlOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;
import me.chanjar.weixin.open.bean.kefu.request.WxOpenKfAccountRequest;
import me.chanjar.weixin.open.bean.kefu.result.*;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Date;

/**
 * 测试客服相关接口
 *
 * @author Binary Wang
 */
@Test
@Guice(modules = ApiTestModule.class)
public class WxOpenKefuServiceImplTest {

    @Inject
    protected WxOpenService wxService;

    public void testSendKefuOpenNewsMessage() throws WxErrorException {
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        WxOpenKefuMessage message = new WxOpenKefuMessage();
        message.setMsgType(WxConsts.CUSTOM_MSG_MPNEWS);
        message.setToUser(configStorage.getOpenid());
        message.setOpenNewsMediaId("52R6dL2FxDpM9N1rCY3sYBqHwq-L7K_lz1sPI71idMg");

        this.wxService.getKefuService().sendKefuMessage(message);
    }

    public void testSendKefuMessage() throws WxErrorException {
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        WxOpenKefuMessage message = new WxOpenKefuMessage();
        message.setMsgType(WxConsts.CUSTOM_MSG_TEXT);
        message.setToUser(configStorage.getOpenid());
        message.setContent(
                "欢迎欢迎，热烈欢迎\n换行测试\n超链接:<a href=\"http://www.baidu.com\">Hello World</a>");

        this.wxService.getKefuService().sendKefuMessage(message);
    }

    public void testSendKefuMessageWithKfAccount() throws WxErrorException {
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        WxOpenKefuMessage message = new WxOpenKefuMessage();
        message.setMsgType(WxConsts.CUSTOM_MSG_TEXT);
        message.setToUser(configStorage.getOpenid());
        message.setKfAccount(configStorage.getKfAccount());
        message.setContent(
                "欢迎欢迎，热烈欢迎\n换行测试\n超链接:<a href=\"http://www.baidu.com\">Hello World</a>");

        this.wxService.getKefuService().sendKefuMessage(message);
    }

    public void testKfList() throws WxErrorException {
        WxOpenKfList kfList = this.wxService.getKefuService().kfList();
        Assert.assertNotNull(kfList);
        for (WxOpenKfInfo k : kfList.getKfList()) {
            System.err.println(k);
        }
    }

    public void testKfOnlineList() throws WxErrorException {
        WxOpenKfOnlineList kfOnlineList = this.wxService.getKefuService()
                .kfOnlineList();
        Assert.assertNotNull(kfOnlineList);
        for (WxOpenKfInfo k : kfOnlineList.getKfOnlineList()) {
            System.err.println(k);
        }
    }

    @DataProvider
    public Object[][] getKfAccount() {
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        return new Object[][]{{configStorage.getKfAccount()}};
    }

    @Test(dataProvider = "getKfAccount")
    public void testKfAccountAdd(String kfAccount) throws WxErrorException {
        WxOpenKfAccountRequest request = WxOpenKfAccountRequest.builder()
                .kfAccount(kfAccount).nickName("我晕").build();
        Assert.assertTrue(this.wxService.getKefuService().kfAccountAdd(request));
    }

    @Test(dependsOnMethods = {
            "testKfAccountAdd"}, dataProvider = "getKfAccount")
    public void testKfAccountUpdate(String kfAccount) throws WxErrorException {
        WxOpenKfAccountRequest request = WxOpenKfAccountRequest.builder()
                .kfAccount(kfAccount).nickName("我晕").build();
        Assert.assertTrue(this.wxService.getKefuService().kfAccountUpdate(request));
    }

    @Test(dependsOnMethods = {
            "testKfAccountAdd"}, dataProvider = "getKfAccount")
    public void testKfAccountInviteWorker(String kfAccount) throws WxErrorException {
        WxOpenKfAccountRequest request = WxOpenKfAccountRequest.builder()
                .kfAccount(kfAccount).inviteWx("    ").build();
        Assert.assertTrue(this.wxService.getKefuService().kfAccountInviteWorker(request));
    }

    @Test(dependsOnMethods = {
            "testKfAccountUpdate"}, dataProvider = "getKfAccount")
    public void testKfAccountUploadHeadImg(String kfAccount)
            throws WxErrorException {
        File imgFile = new File("src\\test\\resources\\mm.jpeg");
        boolean result = this.wxService.getKefuService()
                .kfAccountUploadHeadImg(kfAccount, imgFile);
        Assert.assertTrue(result);
    }

    @Test(dataProvider = "getKfAccount")
    public void testKfAccountDel(String kfAccount) throws WxErrorException {
        boolean result = this.wxService.getKefuService().kfAccountDel(kfAccount);
        Assert.assertTrue(result);
    }

    @DataProvider
    public Object[][] getKfAccountAndOpenid() {
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        return new Object[][]{
                {configStorage.getKfAccount(), configStorage.getOpenid()}};
    }

    @Test(dataProvider = "getKfAccountAndOpenid")
    public void testKfSessionCreate(String kfAccount, String openid)
            throws WxErrorException {
        boolean result = this.wxService.getKefuService().kfSessionCreate(openid,
                kfAccount);
        Assert.assertTrue(result);
    }

    @Test(dataProvider = "getKfAccountAndOpenid")
    public void testKfSessionClose(String kfAccount, String openid)
            throws WxErrorException {
        boolean result = this.wxService.getKefuService().kfSessionClose(openid,
                kfAccount);
        Assert.assertTrue(result);
    }

    @Test(dataProvider = "getKfAccountAndOpenid")
    public void testKfSessionGet(@SuppressWarnings("unused") String kfAccount,
                                 String openid) throws WxErrorException {
        WxOpenKfSessionGetResult result = this.wxService.getKefuService()
                .kfSessionGet(openid);
        Assert.assertNotNull(result);
        System.err.println(result);
    }

    @Test(dataProvider = "getKfAccount")
    public void testKfSessionList(String kfAccount) throws WxErrorException {
        WxOpenKfSessionList result = this.wxService.getKefuService()
                .kfSessionList(kfAccount);
        Assert.assertNotNull(result);
        System.err.println(result);
    }

    @Test
    public void testKfSessionGetWaitCase() throws WxErrorException {
        WxOpenKfSessionWaitCaseList result = this.wxService.getKefuService()
                .kfSessionGetWaitCase();
        Assert.assertNotNull(result);
        System.err.println(result);
    }

    @Test
    public void testKfMsgList() throws WxErrorException {
        Date startTime = DateTime.now().minusDays(1).toDate();
        Date endTime = DateTime.now().minusDays(0).toDate();
        WxOpenKfMsgList result = this.wxService.getKefuService().kfMsgList(startTime, endTime, 1L, 50);
        Assert.assertNotNull(result);
        System.err.println(result);
    }

    @Test
    public void testKfMsgListAll() throws WxErrorException {
        Date startTime = DateTime.now().minusDays(1).toDate();
        Date endTime = DateTime.now().minusDays(0).toDate();
        WxOpenKfMsgList result = this.wxService.getKefuService().kfMsgList(startTime, endTime);
        Assert.assertNotNull(result);
        System.err.println(result);
    }
}
