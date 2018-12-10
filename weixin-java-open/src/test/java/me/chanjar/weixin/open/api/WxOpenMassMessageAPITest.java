package me.chanjar.weixin.open.api;

import com.google.inject.Inject;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.bean.WxOpenMassNews;
import me.chanjar.weixin.open.bean.WxOpenMassOpenIdsMessage;
import me.chanjar.weixin.open.bean.WxOpenMassTagMessage;
import me.chanjar.weixin.open.bean.WxOpenMassVideo;
import me.chanjar.weixin.open.bean.result.WxOpenMassSendResult;
import me.chanjar.weixin.open.bean.result.WxOpenMassUploadResult;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 测试群发消息
 *
 * @author chanjarster
 */
@Test(groups = "massAPI", dependsOnGroups = {"baseAPI", "mediaAPI", "groupAPI"})
@Guice(modules = ApiTestModule.class)
public class WxOpenMassMessageAPITest {

    @Inject
    protected WxOpenService wxService;

    @Test
    public void testTextMassOpenIdsMessageSend() throws WxErrorException {
        // 发送群发消息
        WxXmlOpenInMemoryConfigStorage configProvider = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        WxOpenMassOpenIdsMessage massMessage = new WxOpenMassOpenIdsMessage();
        massMessage.setMsgType(WxConsts.MASS_MSG_TEXT);
        massMessage.setContent("测试群发消息\n欢迎欢迎，热烈欢迎\n换行测试\n超链接:<a href=\"http://www.baidu.com\">Hello World</a>");
        massMessage.getToUsers().add(configProvider.getOpenid());

        WxOpenMassSendResult massResult = this.wxService.getMassService()
                .massOpenIdsMessageSend(massMessage);
        Assert.assertNotNull(massResult);
        Assert.assertNotNull(massResult.getMsgId());
    }

    @Test(dataProvider = "massMessages")
    public void testMediaMassOpenIdsMessageSend(String massMsgType,
                                                String mediaId) throws WxErrorException {
        // 发送群发消息
        WxXmlOpenInMemoryConfigStorage configProvider = (WxXmlOpenInMemoryConfigStorage) this.wxService
                .getWxOpenConfigStorage();
        WxOpenMassOpenIdsMessage massMessage = new WxOpenMassOpenIdsMessage();
        massMessage.setMsgType(massMsgType);
        massMessage.setMediaId(mediaId);
        massMessage.getToUsers().add(configProvider.getOpenid());

        WxOpenMassSendResult massResult = this.wxService.getMassService()
                .massOpenIdsMessageSend(massMessage);
        Assert.assertNotNull(massResult);
        Assert.assertNotNull(massResult.getMsgId());
    }

    @Test
    public void testTextMassGroupMessageSend() throws WxErrorException {
        WxOpenMassTagMessage massMessage = new WxOpenMassTagMessage();
        massMessage.setMsgtype(WxConsts.MASS_MSG_TEXT);
        massMessage.setContent("测试群发消息\n欢迎欢迎，热烈欢迎\n换行测试\n超链接:<a href=\"http://www.baidu.com\">Hello World</a>");
        massMessage
                .setTagId(this.wxService.getUserTagService().tagGet().get(0).getId());

        WxOpenMassSendResult massResult = this.wxService.getMassService()
                .massGroupMessageSend(massMessage);
        Assert.assertNotNull(massResult);
        Assert.assertNotNull(massResult.getMsgId());
    }

    @Test(dataProvider = "massMessages")
    public void testMediaMassGroupMessageSend(String massMsgType, String mediaId)
            throws WxErrorException {
        WxOpenMassTagMessage massMessage = new WxOpenMassTagMessage();
        massMessage.setMsgtype(massMsgType);
        massMessage.setMediaId(mediaId);
        massMessage
                .setTagId(this.wxService.getUserTagService().tagGet().get(0).getId());

        WxOpenMassSendResult massResult = this.wxService.getMassService()
                .massGroupMessageSend(massMessage);
        Assert.assertNotNull(massResult);
        Assert.assertNotNull(massResult.getMsgId());
    }

    @DataProvider
    public Object[][] massMessages() throws WxErrorException, IOException {
        Object[][] messages = new Object[4][];

    /*
     * 视频素材
     */
        try (InputStream inputStream = ClassLoader
                .getSystemResourceAsStream("mm.mp4")) {
            // 上传视频到媒体库
            WxMediaUploadResult uploadMediaRes = this.wxService.getMaterialService()
                    .mediaUpload(WxConsts.MEDIA_VIDEO, WxConsts.FILE_MP4, inputStream);
            Assert.assertNotNull(uploadMediaRes);
            Assert.assertNotNull(uploadMediaRes.getMediaId());

            // 把视频变成可被群发的媒体
            WxOpenMassVideo video = new WxOpenMassVideo();
            video.setTitle("测试标题");
            video.setDescription("测试描述");
            video.setMediaId(uploadMediaRes.getMediaId());
            WxOpenMassUploadResult uploadResult = this.wxService.getMassService().massVideoUpload(video);
            Assert.assertNotNull(uploadResult);
            Assert.assertNotNull(uploadResult.getMediaId());
            messages[0] = new Object[]{WxConsts.MASS_MSG_VIDEO, uploadResult.getMediaId()};
        }

        /**
         * 图片素材
         */
        try (InputStream inputStream = ClassLoader
                .getSystemResourceAsStream("mm.jpeg")) {
            WxMediaUploadResult uploadMediaRes = this.wxService.getMaterialService()
                    .mediaUpload(WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG, inputStream);
            Assert.assertNotNull(uploadMediaRes);
            Assert.assertNotNull(uploadMediaRes.getMediaId());
            messages[1] = new Object[]{WxConsts.MASS_MSG_IMAGE, uploadMediaRes.getMediaId()};
        }

        /**
         * 语音素材
         */
        try (InputStream inputStream = ClassLoader
                .getSystemResourceAsStream("mm.mp3")) {
            WxMediaUploadResult uploadMediaRes = this.wxService.getMaterialService()
                    .mediaUpload(WxConsts.MEDIA_VOICE, WxConsts.FILE_MP3, inputStream);
            Assert.assertNotNull(uploadMediaRes);
            Assert.assertNotNull(uploadMediaRes.getMediaId());
            messages[2] = new Object[]{WxConsts.MASS_MSG_VOICE, uploadMediaRes.getMediaId()};
        }

        /**
         * 图文素材
         */
        try (InputStream inputStream = ClassLoader
                .getSystemResourceAsStream("mm.jpeg")) {
            // 上传照片到媒体库
            WxMediaUploadResult uploadMediaRes = this.wxService.getMaterialService()
                    .mediaUpload(WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG, inputStream);
            Assert.assertNotNull(uploadMediaRes);
            Assert.assertNotNull(uploadMediaRes.getMediaId());

            // 上传图文消息
            WxOpenMassNews news = new WxOpenMassNews();
            WxOpenMassNews.WxOpenMassNewsArticle article1 = new WxOpenMassNews.WxOpenMassNewsArticle();
            article1.setTitle("标题1");
            article1.setContent("内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1");
            article1.setThumbMediaId(uploadMediaRes.getMediaId());
            news.addArticle(article1);

            WxOpenMassNews.WxOpenMassNewsArticle article2 = new WxOpenMassNews.WxOpenMassNewsArticle();
            article2.setTitle("标题2");
            article2.setContent("内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2内容2");
            article2.setThumbMediaId(uploadMediaRes.getMediaId());
            article2.setShowCoverPic(true);
            article2.setAuthor("作者2");
            article2.setContentSourceUrl("www.baidu.com");
            article2.setDigest("摘要2");
            news.addArticle(article2);

            WxOpenMassUploadResult massUploadResult = this.wxService.getMassService()
                    .massNewsUpload(news);
            Assert.assertNotNull(massUploadResult);
            Assert.assertNotNull(uploadMediaRes.getMediaId());
            messages[3] = new Object[]{WxConsts.MASS_MSG_NEWS, massUploadResult.getMediaId()};
        }

        return messages;
    }

}
