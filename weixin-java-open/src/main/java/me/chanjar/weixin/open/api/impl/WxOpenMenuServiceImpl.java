package me.chanjar.weixin.open.api.impl;

import com.google.gson.JsonObject;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenMenuService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.menu.WxOpenGetSelfMenuInfoResult;
import me.chanjar.weixin.open.bean.result.WxOpenMenuConditionalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class WxOpenMenuServiceImpl implements WxOpenMenuService {

    private static final String API_URL_PREFIX = "https://api.weixin.qq.com/cgi-bin/menu";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final WxOpenService wxOpenService;

    public WxOpenMenuServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public boolean createMenu(WxMenu menu) throws WxErrorException {
        String menuJson = menu.toJson();
        String url = API_URL_PREFIX + "/create";
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("开始创建菜单：{}", menuJson);
        }
        String resultContent = this.wxOpenService.post(url, menuJson);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("创建菜单：{},结果：{}", menuJson, resultContent);
        }
        return resultContent != null;
    }

    @Override
    public WxOpenMenuConditionalResult createConditionalMenu(WxMenu menu) throws WxErrorException {
        String menuJson = menu.toJson();
        String url = API_URL_PREFIX + "/addconditional";
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("开始创建个性化菜单：{}", menuJson);
        }
        String resultContent = this.wxOpenService.post(url, menuJson);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("创建个性化菜单：{},结果：{}", menuJson, resultContent);
        }
        return WxOpenMenuConditionalResult.fromJson(resultContent);
    }

    @Override
    public boolean deleteMenu() throws WxErrorException {
        String url = API_URL_PREFIX + "/delete";
        String result = this.wxOpenService.get(url, null);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("删除菜单结果：{}", result);
        }
        return result != null;
    }

    @Override
    public boolean deleteConditionalMenu(String menuid) throws WxErrorException {
        String url = API_URL_PREFIX + "/delconditional";
        JsonObject o = new JsonObject();
        o.addProperty("menuid", menuid);
        String result = this.wxOpenService.post(url, o.toString());
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("根据MeunId({})删除菜单结果：{}", menuid, result);
        }
        return result != null;
    }

    @Override
    public WxMenu getMenu() throws WxErrorException {
        String url = API_URL_PREFIX + "/get";
        try {
            String resultContent = this.wxOpenService.get(url, null);
            return WxMenu.fromJson(resultContent);
        } catch (WxErrorException e) {
            // 46003 不存在的菜单数据
            if (e.getError().getErrorCode() == 46003) {
                return null;
            }
            throw e;
        }
    }

    @Override
    public WxMenu tryMatchConditionalMenu(String userid) throws WxErrorException {
        String url = API_URL_PREFIX + "/trymatch";
        try {
            JsonObject o = new JsonObject();
            o.addProperty("user_id", userid);
            String resultContent = this.wxOpenService.post(url, o.toString());
            return WxMenu.fromJson(resultContent);
        } catch (WxErrorException e) {
            // 46003 不存在的菜单数据     46002 不存在的菜单版本
            if (e.getError().getErrorCode() == 46003 || e.getError().getErrorCode() == 46002) {
                return null;
            }
            throw e;
        }
    }

    @Override
    public WxOpenGetSelfMenuInfoResult getSelfMenuInfo() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info";
        String resultContent = this.wxOpenService.get(url, null);
        return WxOpenGetSelfMenuInfoResult.fromJson(resultContent);
    }
}
