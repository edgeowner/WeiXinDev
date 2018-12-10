package me.chanjar.weixin.open.api.impl;

import com.google.inject.Inject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.ApiTestModule;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxXmlOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.bean.template.WxOpenTemplate;
import me.chanjar.weixin.open.bean.template.WxOpenTemplateData;
import me.chanjar.weixin.open.bean.template.WxOpenTemplateIndustry;
import me.chanjar.weixin.open.bean.template.WxOpenTemplateMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * Created by Binary Wang on 2016-10-14.
 * @author <a href="https://github.com/binarywang">binarywang(Binary Wang)</a>
 * </pre>
 */
@Guice(modules = ApiTestModule.class)
public class WxOpenTemplateMsgServiceImplTest {
    @Inject
    protected WxOpenService wxService;

    @Test(invocationCount = 5, threadPoolSize = 3)
    public void testSendTemplateMsg() throws WxErrorException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        WxXmlOpenInMemoryConfigStorage configStorage = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        WxOpenTemplateMessage templateMessage = WxOpenTemplateMessage.builder()
                .toUser(configStorage.getOpenid())
                .templateId(configStorage.getTemplateId()).build();
        templateMessage.addWxOpenTemplateData(
                new WxOpenTemplateData("first", dateFormat.format(new Date()), "#FF00FF"));
        templateMessage.addWxOpenTemplateData(
                new WxOpenTemplateData("remark", RandomStringUtils.randomAlphanumeric(100), "#FF00FF"));
        templateMessage.setUrl(" ");
        String msgId = this.wxService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        Assert.assertNotNull(msgId);
        System.out.println(msgId);
    }

    @Test
    public void testGetIndustry() throws Exception {
        final WxOpenTemplateIndustry industry = this.wxService.getTemplateMsgService().getIndustry();
        Assert.assertNotNull(industry);
        System.out.println(industry);
    }

    @Test
    public void testSetIndustry() throws Exception {
        WxOpenTemplateIndustry industry = new WxOpenTemplateIndustry(new WxOpenTemplateIndustry.Industry("1"),
                new WxOpenTemplateIndustry.Industry("04"));
        boolean result = this.wxService.getTemplateMsgService().setIndustry(industry);
        Assert.assertTrue(result);
    }

    @Test
    public void testAddTemplate() throws Exception {
        String result = this.wxService.getTemplateMsgService().addTemplate("TM00015");
        Assert.assertNotNull(result);
        System.err.println(result);
    }

    @Test
    public void testGetAllPrivateTemplate() throws Exception {
        List<WxOpenTemplate> result = this.wxService.getTemplateMsgService().getAllPrivateTemplate();
        Assert.assertNotNull(result);
        System.err.println(result);
    }

    @Test
    public void testDelPrivateTemplate() throws Exception {
        String templateId = "RPcTe7-4BkU5A2J3imC6W0b4JbjEERcJg0whOMKJKIc";
        boolean result = this.wxService.getTemplateMsgService().delPrivateTemplate(templateId);
        Assert.assertTrue(result);
    }

}
