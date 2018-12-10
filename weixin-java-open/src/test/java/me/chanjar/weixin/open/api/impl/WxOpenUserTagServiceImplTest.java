package me.chanjar.weixin.open.api.impl;

import com.google.inject.Inject;
import me.chanjar.weixin.open.api.ApiTestModule;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxXmlOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.bean.tag.WxTagListUser;
import me.chanjar.weixin.open.bean.tag.WxUserTag;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author <a href="https://github.com/binarywang">binarywang(Binary Wang)</a>
 *         Created by Binary Wang on 2016/9/2.
 */
@Test
@Guice(modules = ApiTestModule.class)
public class WxOpenUserTagServiceImplTest {
    @Inject
    protected WxOpenService wxService;

    private Long tagId = 2L;

    @Test
    public void testTagCreate() throws Exception {
        String tagName = "测试标签" + System.currentTimeMillis();
        WxUserTag res = this.wxService.getUserTagService().tagCreate(tagName);
        System.out.println(res);
        this.tagId = res.getId();
        Assert.assertEquals(tagName, res.getName());
    }

    @Test
    public void testTagGet() throws Exception {
        List<WxUserTag> res = this.wxService.getUserTagService().tagGet();
        System.out.println(res);
        Assert.assertNotNull(res);
    }

    @Test(dependsOnMethods = {"testTagCreate"})
    public void testTagUpdate() throws Exception {
        String tagName = "修改标签" + System.currentTimeMillis();
        Boolean res = this.wxService.getUserTagService().tagUpdate(this.tagId, tagName);
        System.out.println(res);
        Assert.assertTrue(res);
    }

    @Test(dependsOnMethods = {"testTagCreate"})
    public void testTagDelete() throws Exception {
        Boolean res = this.wxService.getUserTagService().tagDelete(this.tagId);
        System.out.println(res);
        Assert.assertTrue(res);
    }

    @Test
    public void testTagListUser() throws Exception {
        WxTagListUser res = this.wxService.getUserTagService().tagListUser(this.tagId, null);
        System.out.println(res);
        Assert.assertNotNull(res);
    }

    @Test
    public void testBatchTagging() throws Exception {
        String[] openids = new String[]{((WxXmlOpenInMemoryConfigStorage) this.wxService.getWxOpenConfigStorage()).getOpenid()};
        boolean res = this.wxService.getUserTagService().batchTagging(this.tagId, openids);
        System.out.println(res);
        Assert.assertTrue(res);
    }

    @Test
    public void testBatchUntagging() throws Exception {
        String[] openids = new String[]{((WxXmlOpenInMemoryConfigStorage) this.wxService.getWxOpenConfigStorage()).getOpenid()};
        boolean res = this.wxService.getUserTagService().batchUntagging(this.tagId, openids);
        System.out.println(res);
        Assert.assertTrue(res);
    }

    @Test
    public void testUserTagList() throws Exception {
        List<Long> res = this.wxService.getUserTagService().userTagList(
                ((WxXmlOpenInMemoryConfigStorage) this.wxService.getWxOpenConfigStorage()).getOpenid());
        System.out.println(res);
        Assert.assertNotNull(res);
    }
}
