package me.chanjar.weixin.open.api.impl;

import me.chanjar.weixin.open.api.ApiTestModule;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxXmlOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.bean.result.WxOpenUserBlacklistGetResult;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author miller
 */
@Test(groups = "userAPI")
@Guice(modules = ApiTestModule.class)
public class WxOpenUserBlacklistServiceImplTest {
    @Inject
    protected WxOpenService wxService;

    @Test
    public void testGetBlacklist() throws Exception {
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        WxOpenUserBlacklistGetResult wxOpenUserBlacklistGetResult = this.wxService.getBlackListService().getBlacklist(configStorage.getOpenid());
        Assert.assertNotNull(wxOpenUserBlacklistGetResult);
        Assert.assertFalse(wxOpenUserBlacklistGetResult.getCount() == -1);
        Assert.assertFalse(wxOpenUserBlacklistGetResult.getTotal() == -1);
        Assert.assertFalse(wxOpenUserBlacklistGetResult.getOpenidList().size() == -1);
        System.out.println(wxOpenUserBlacklistGetResult);
    }

    @Test
    public void testPushToBlacklist() throws Exception {
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        List<String> openidList = new ArrayList<>();
        openidList.add(configStorage.getOpenid());
        this.wxService.getBlackListService().pushToBlacklist(openidList);
    }

    @Test
    public void testPullFromBlacklist() throws Exception {
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        List<String> openidList = new ArrayList<>();
        openidList.add(configStorage.getOpenid());
        this.wxService.getBlackListService().pullFromBlacklist(openidList);
    }

}
