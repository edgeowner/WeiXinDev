package me.chanjar.weixin.open.api.impl;

import com.google.inject.Inject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.ApiTestModule;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxXmlOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.bean.WxOpenUserQuery;
import me.chanjar.weixin.open.bean.result.WxOpenUser;
import me.chanjar.weixin.open.bean.result.WxOpenUserList;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试用户相关的接口
 *
 * @author chanjarster
 * @author Binary Wang
 */
@Test(groups = "userAPI")
@Guice(modules = ApiTestModule.class)
public class WxOpenUserServiceImplTest {

    @Inject
    private WxOpenService wxService;

    private WxXmlOpenInMemoryConfigStorage configProvider;

    @BeforeTest
    public void setup() {
        this.configProvider = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
    }

    public void testUserUpdateRemark() throws WxErrorException {
        this.wxService.getUserService()
                .userUpdateRemark(this.configProvider.getOpenid(), "测试备注名");
    }

    public void testUserInfo() throws WxErrorException {
        WxOpenUser user = this.wxService.getUserService()
                .userInfo(this.configProvider.getOpenid(), null);
        Assert.assertNotNull(user);
        System.out.println(user);
    }

    public void testUserInfoList() throws WxErrorException {
        List<String> openids = new ArrayList<>();
        openids.add(this.configProvider.getOpenid());
        List<WxOpenUser> userList = this.wxService.getUserService()
                .userInfoList(openids);
        Assert.assertEquals(userList.size(), 1);
        System.out.println(userList);
    }

    public void testUserInfoListByWxOpenUserQuery() throws WxErrorException {
        WxOpenUserQuery query = new WxOpenUserQuery();
        query.add(this.configProvider.getOpenid(), "zh_CN");
        List<WxOpenUser> userList = this.wxService.getUserService()
                .userInfoList(query);
        Assert.assertEquals(userList.size(), 1);
        System.out.println(userList);
    }

    public void testUserList() throws WxErrorException {
        WxOpenUserList wxOpenUserList = this.wxService.getUserService().userList(null);
        Assert.assertNotNull(wxOpenUserList);
        Assert.assertFalse(wxOpenUserList.getCount() == -1);
        Assert.assertFalse(wxOpenUserList.getTotal() == -1);
        Assert.assertFalse(wxOpenUserList.getOpenIds().size() == -1);
        System.out.println(wxOpenUserList);
    }

}
