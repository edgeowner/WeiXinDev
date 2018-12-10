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

package com.guoanjia.business.lanfest.controller;

import com.guoanjia.business.base.controller.BaseController;
import com.guoanjia.business.lanfest.service.GuessUserService;
import me.chanjar.weixin.open.api.WxOpenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/guessUserController")
public class GuessUserController extends BaseController {
    private final WxOpenService wxOpenService;
    private final GuessUserService guessUserService;

    public GuessUserController(WxOpenService wxOpenService, GuessUserService guessUserService) {
        super(wxOpenService);
        this.wxOpenService = wxOpenService;
        this.guessUserService = guessUserService;
    }

    /**
     * 保存guess信息
     */
//    @RequestMapping(params = "doSaveGuess")
//    public AjaxJson doSaveGuess(@RequestBody Map<String, String> params) {
//        AjaxJson j = new AjaxJson();
//        String company = params.get("company");
//        String name = params.get("name");
//        String phone = params.get("phone");//手机号
//        String score = params.get("score");//分数
//        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(score) || StringUtil.isEmpty(company)) {
//            j.setCode(201);
//            j.setMsg("提交信息不全");
//            j.setSuccess(false);
//            return j;
//        }
//
//        if (guessUserService.doSaveGuess(name, phone, score, company)) {
//            Long maxCount = guessUserService.count(Example.of(new GuessUser().setMaxscore("15")));
//            //获奖数量
//            if (maxCount >= 400) {
//                j.setCode(202);
//                j.setMsg("很遗憾,奖品已发送完毕。");
//                j.setSuccess(false);
//                return j;
//            }
//            j.setCode(200);
//            j.setMsg("保存成功");
//            j.setSuccess(true);
//        }
//        return j;
//    }
}
