package me.chanjar.weixin.open.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.chanjar.weixin.open.bean.*;
import me.chanjar.weixin.open.bean.component.WxOpenOAuth2AccessToken;
import me.chanjar.weixin.open.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.open.bean.datacube.WxDataCubeUserSummary;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;
import me.chanjar.weixin.open.bean.material.*;
import me.chanjar.weixin.open.bean.result.*;
import me.chanjar.weixin.open.bean.template.WxOpenTemplateIndustry;
import me.chanjar.weixin.open.bean.template.WxOpenTemplateMessage;

public class WxOpenGsonBuilder {

    public static final GsonBuilder INSTANCE = new GsonBuilder();

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.registerTypeAdapter(WxOpenKefuMessage.class, new WxOpenKefuMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMassNews.class, new WxOpenMassNewsGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMassTagMessage.class, new WxOpenMassTagMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMassOpenIdsMessage.class, new WxOpenMassOpenIdsMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenUser.class, new WxOpenUserGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenUserList.class, new WxUserListGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMassVideo.class, new WxOpenMassVideoAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMassSendResult.class, new WxOpenMassSendResultAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMassUploadResult.class, new WxOpenMassUploadResultAdapter());
        INSTANCE.registerTypeAdapter(WxOpenQrCodeTicket.class, new WxQrCodeTicketAdapter());
        INSTANCE.registerTypeAdapter(WxOpenTemplateMessage.class, new WxOpenTemplateMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenSemanticQueryResult.class, new WxOpenSemanticQueryResultAdapter());
        INSTANCE.registerTypeAdapter(WxOpenOAuth2AccessToken.class, new WxOpenOAuth2AccessTokenAdapter());
        INSTANCE.registerTypeAdapter(WxDataCubeUserSummary.class, new WxOpenUserSummaryGsonAdapter());
        INSTANCE.registerTypeAdapter(WxDataCubeUserCumulate.class, new WxOpenUserCumulateGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialUploadResult.class, new WxOpenMaterialUploadResultAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialVideoInfoResult.class, new WxOpenMaterialVideoInfoResultAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMassNews.WxOpenMassNewsArticle.class, new WxOpenMassNewsArticleGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialArticleUpdate.class, new WxOpenMaterialArticleUpdateGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialCountResult.class, new WxOpenMaterialCountResultAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialNews.class, new WxOpenMaterialNewsGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialNews.WxOpenMaterialNewsArticle.class, new WxOpenMaterialNewsArticleGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialNewsBatchGetResult.class, new WxOpenMaterialNewsBatchGetGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem.class, new WxOpenMaterialNewsBatchGetGsonItemAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialFileBatchGetResult.class, new WxOpenMaterialFileBatchGetGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem.class, new WxOpenMaterialFileBatchGetGsonItemAdapter());
        INSTANCE.registerTypeAdapter(WxOpenCardResult.class, new WxOpenCardResultGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenCard.class, new WxOpenCardGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenMassPreviewMessage.class, new WxOpenMassPreviewMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMediaImgUploadResult.class, new WxMediaImgUploadResultGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenTemplateIndustry.class, new WxOpenIndustryGsonAdapter());
        INSTANCE.registerTypeAdapter(WxOpenUserBlacklistGetResult.class, new WxUserBlacklistGetResultGsonAdapter());
    }

    public static Gson create() {
        return INSTANCE.create();
    }

}
