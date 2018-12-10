/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.guoanjia.weixin.core.handler;

import com.guoanjia.weixin.core.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenMenuService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenMenuServiceImpl;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MenuHandler extends AbstractHandler {

    /**
     * @param wxMessage
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param wxOpenService
     * @param sessionManager
     * @return xml格式的消息，如果在异步规则里处理的话，可以返回null
     */
    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService, WxSessionManager sessionManager) throws WxErrorException {
        menu(wxOpenService);
        return new TextBuilder().build("菜单更新成功", wxMessage, wxOpenService);
    }

    /**
     * 菜单生成
     *
     * @param wxOpenService
     * @throws WxErrorException
     */
    private void menu(WxOpenService wxOpenService) throws WxErrorException {
        final WxOpenMenuService wxOpenMenuService = new WxOpenMenuServiceImpl(wxOpenService);
        //首先删除菜单
        wxOpenMenuService.deleteMenu();

        //构建菜单
        List<WxMenuButton> x5Meuns = new ArrayList<>();
        WxMenuButton threeMenu = new WxMenuButton();
        threeMenu.setName("国安之家");

        WxMenuButton show = new WxMenuButton();
        show.setName("展示空间");
        show.setType(WxConsts.BUTTON_CLICK);//
        show.setKey("show");


        //TODO 该按钮待修正
        WxMenuButton pinpai = new WxMenuButton();
        pinpai.setName("品牌介绍");
        pinpai.setType(WxConsts.BUTTON_VIEW);//
        pinpai.setUrl("http://www.guoanfamily.com/login/kuaimatest.html");

        LinkedList<WxMenuButton> buttons1 = new LinkedList<>();
        buttons1.add(pinpai);
        buttons1.add(show);

        threeMenu.setSubButtons(buttons1);

        x5Meuns.add(threeMenu);
        WxMenu x5Menu = new WxMenu();
        x5Menu.setButtons(x5Meuns);
        //发布新菜单
        wxOpenMenuService.createMenu(x5Menu);
    }
}
