package me.chanjar.weixin.open.api.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.BeanUtils;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.WxOpenStoreService;
import me.chanjar.weixin.open.bean.store.WxOpenStoreBaseInfo;
import me.chanjar.weixin.open.bean.store.WxOpenStoreInfo;
import me.chanjar.weixin.open.bean.store.WxOpenStoreListResult;
import me.chanjar.weixin.open.util.json.WxOpenGsonBuilder;

import java.util.List;

/**
 * Created by Binary Wang on 2016/9/26.
 *
 * @author binarywang (https://github.com/binarywang)
 */
public class WxOpenStoreServiceImpl implements WxOpenStoreService {
    private static final String API_BASE_URL = "http://api.weixin.qq.com/cgi-bin/poi";

    private WxOpenService wxOpenService;

    public WxOpenStoreServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public void add(WxOpenStoreBaseInfo request) throws WxErrorException {
        BeanUtils.checkRequiredFields(request);

        String url = API_BASE_URL + "/addpoi";
        String response = this.wxOpenService.post(url, request.toJson());
        WxError wxError = WxError.fromJson(response);
        if (wxError.getErrorCode() != 0) {
            throw new WxErrorException(wxError);
        }
    }

    @Override
    public WxOpenStoreBaseInfo get(String poiId) throws WxErrorException {
        String url = API_BASE_URL + "/getpoi";
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("poi_id", poiId);
        String response = this.wxOpenService.post(url, paramObject.toString());
        WxError wxError = WxError.fromJson(response);
        if (wxError.getErrorCode() != 0) {
            throw new WxErrorException(wxError);
        }
        return WxOpenStoreBaseInfo.fromJson(new JsonParser().parse(response).getAsJsonObject()
                .get("business").getAsJsonObject().get("base_info").toString());
    }

    @Override
    public void delete(String poiId) throws WxErrorException {
        String url = API_BASE_URL + "/delpoi";
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("poi_id", poiId);
        String response = this.wxOpenService.post(url, paramObject.toString());
        WxError wxError = WxError.fromJson(response);
        if (wxError.getErrorCode() != 0) {
            throw new WxErrorException(wxError);
        }
    }

    @Override
    public WxOpenStoreListResult list(int begin, int limit)
            throws WxErrorException {
        String url = API_BASE_URL + "/getpoilist";
        JsonObject params = new JsonObject();
        params.addProperty("begin", begin);
        params.addProperty("limit", limit);
        String response = this.wxOpenService.post(url, params.toString());

        WxError wxError = WxError.fromJson(response);
        if (wxError.getErrorCode() != 0) {
            throw new WxErrorException(wxError);
        }

        return WxOpenStoreListResult.fromJson(response);
    }

    @Override
    public List<WxOpenStoreInfo> listAll() throws WxErrorException {
        int limit = 50;
        WxOpenStoreListResult list = this.list(0, limit);
        List<WxOpenStoreInfo> stores = list.getBusinessList();
        if (list.getTotalCount() > limit) {
            int begin = limit;
            WxOpenStoreListResult followingList = this.list(begin, limit);
            while (followingList.getBusinessList().size() > 0) {
                stores.addAll(followingList.getBusinessList());
                begin += limit;
                if (begin >= list.getTotalCount()) {
                    break;
                }
                followingList = this.list(begin, limit);
            }
        }

        return stores;
    }

    @Override
    public void update(WxOpenStoreBaseInfo request) throws WxErrorException {
        String url = API_BASE_URL + "/updatepoi";
        String response = this.wxOpenService.post(url, request.toJson());
        WxError wxError = WxError.fromJson(response);
        if (wxError.getErrorCode() != 0) {
            throw new WxErrorException(wxError);
        }
    }

    @Override
    public List<String> listCategories() throws WxErrorException {
        String url = API_BASE_URL + "/getwxcategory";
        String response = this.wxOpenService.get(url, null);
        WxError wxError = WxError.fromJson(response);
        if (wxError.getErrorCode() != 0) {
            throw new WxErrorException(wxError);
        }

        return WxOpenGsonBuilder.create().fromJson(
                new JsonParser().parse(response).getAsJsonObject().get("category_list"),
                new TypeToken<List<String>>() {
                }.getType());
    }

}
