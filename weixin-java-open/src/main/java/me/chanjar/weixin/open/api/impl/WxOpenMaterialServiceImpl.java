package me.chanjar.weixin.open.api.impl;

import com.google.gson.JsonObject;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.common.util.http.MediaDownloadRequestExecutor;
import me.chanjar.weixin.common.util.http.MediaUploadRequestExecutor;
import me.chanjar.weixin.open.api.WxOpenMaterialService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.material.*;
import me.chanjar.weixin.open.util.http.*;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by Binary Wang on 2016/7/21.
 */
public class WxOpenMaterialServiceImpl implements WxOpenMaterialService {
    private static final String MEDIA_API_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/media";
    private static final String MATERIAL_API_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/material";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxOpenService wxOpenService;

    public WxOpenMaterialServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public WxMediaUploadResult mediaUpload(String mediaType, String fileType, InputStream inputStream) throws WxErrorException {
        try {
            return this.mediaUpload(mediaType, FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), fileType));
        } catch (IOException e) {
            this.logger.error("素材上传失败,{}", e.getMessage());
            throw new WxErrorException(WxError.newBuilder().setErrorMsg(e.getMessage()).build());
        }
    }

    @Override
    public WxMediaUploadResult mediaUpload(String mediaType, File file) throws WxErrorException {
        String url = MEDIA_API_URL_PREFIX + "/upload?type=" + mediaType;
        return this.wxOpenService.execute(new MediaUploadRequestExecutor(), url, file);
    }

    @Override
    public File mediaDownload(String media_id) throws WxErrorException {
        String url = MEDIA_API_URL_PREFIX + "/get";
        return this.wxOpenService.execute(new MediaDownloadRequestExecutor(this.wxOpenService.getWxOpenConfigStorage().getTmpDirFile()), url, "media_id=" + media_id);
    }

    @Override
    public WxOpenMaterialUploadResult materialNewsUpload(WxOpenMaterialNews news) throws WxErrorException {
        if (news == null || news.isEmpty()) {
            throw new IllegalArgumentException("news is empty!");
        }
        String url = MATERIAL_API_URL_PREFIX + "/add_news";
        String responseContent = this.wxOpenService.post(url, news.toJson());
        return WxOpenMaterialUploadResult.fromJson(responseContent);
    }

    @Override
    public WxMediaImgUploadResult mediaImgUpload(File file) throws WxErrorException {
        String url = MEDIA_API_URL_PREFIX + "/uploadimg";
        return this.wxOpenService.execute(new MediaImgUploadRequestExecutor(), url, file);
    }

    @Override
    public WxOpenMaterialUploadResult materialFileUpload(String mediaType, WxOpenMaterial material) throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/add_material?type=" + mediaType;
        return this.wxOpenService.execute(new MaterialUploadRequestExecutor(), url, material);
    }

    @Override
    public InputStream materialImageOrVoiceDownload(String media_id) throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/get_material";
        return this.wxOpenService.execute(new MaterialVoiceAndImageDownloadRequestExecutor(this.wxOpenService.getWxOpenConfigStorage().getTmpDirFile()), url, media_id);
    }

    @Override
    public WxOpenMaterialVideoInfoResult materialVideoInfo(String media_id) throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/get_material";
        return this.wxOpenService.execute(new MaterialVideoInfoRequestExecutor(), url, media_id);
    }

    @Override
    public WxOpenMaterialNews materialNewsInfo(String media_id) throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/get_material";
        return this.wxOpenService.execute(new MaterialNewsInfoRequestExecutor(), url, media_id);
    }

    @Override
    public boolean materialNewsUpdate(WxOpenMaterialArticleUpdate wxOpenMaterialArticleUpdate) throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/update_news";
        String responseText = this.wxOpenService.post(url, wxOpenMaterialArticleUpdate.toJson());
        WxError wxError = WxError.fromJson(responseText);
        if (wxError.getErrorCode() == 0) {
            return true;
        } else {
            throw new WxErrorException(wxError);
        }
    }


    @Override
    public boolean materialDelete(String media_id) throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/del_material";
        return this.wxOpenService.execute(new MaterialDeleteRequestExecutor(), url, media_id);
    }

    @Override
    public WxOpenMaterialCountResult materialCount() throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/get_materialcount";
        String responseText = this.wxOpenService.get(url, null);
        WxError wxError = WxError.fromJson(responseText);
        if (wxError.getErrorCode() == 0) {
            return WxOpenGsonBuilder.create().fromJson(responseText, WxOpenMaterialCountResult.class);
        } else {
            throw new WxErrorException(wxError);
        }
    }

    @Override
    public WxOpenMaterialNewsBatchGetResult materialNewsBatchGet(int offset, int count) throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/batchget_material";
        JsonObject o = new JsonObject();
        o.addProperty("type", WxConsts.MATERIAL_NEWS);
        o.addProperty("offset", offset);
        o.addProperty("count", count);
        String responseText = this.wxOpenService.post(url, o.toString());
        WxError wxError = WxError.fromJson(responseText);
        if (wxError.getErrorCode() == 0) {
            return WxOpenGsonBuilder.create().fromJson(responseText, WxOpenMaterialNewsBatchGetResult.class);
        } else {
            throw new WxErrorException(wxError);
        }
    }

    @Override
    public WxOpenMaterialFileBatchGetResult materialFileBatchGet(String type, int offset, int count) throws WxErrorException {
        String url = MATERIAL_API_URL_PREFIX + "/batchget_material";
        JsonObject o = new JsonObject();
        o.addProperty("type", type);
        o.addProperty("offset", offset);
        o.addProperty("count", count);
        String responseText = this.wxOpenService.post(url, o.toString());
        WxError wxError = WxError.fromJson(responseText);
        if (wxError.getErrorCode() == 0) {
            return WxOpenGsonBuilder.create().fromJson(responseText, WxOpenMaterialFileBatchGetResult.class);
        } else {
            throw new WxErrorException(wxError);
        }
    }

}
