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


import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.utils.JacksonUtil;
import com.guoanjia.business.base.controller.BaseController;
import com.guoanjia.business.base.service.WxRedPackRecordService;
import com.guoanjia.business.lanfest.entity.model.GuessModel;
import com.guoanjia.business.lanfest.service.GuessService;
import me.chanjar.weixin.open.api.WxOpenService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/guessController")
public class GuessController extends BaseController {
    private final WxOpenService wxOpenService;
    private final GuessService guessService;
    private final WxRedPackRecordService wxRedPackRecordService;

    public GuessController(WxOpenService wxOpenService, GuessService guessService, WxRedPackRecordService wxRedPackRecordService) {
        super(wxOpenService);
        this.wxOpenService = wxOpenService;
        this.guessService = guessService;
        this.wxRedPackRecordService = wxRedPackRecordService;
    }


    //登录
//    @RequestMapping(params = "guessLogin")
//    public AjaxJson guessLogin(@RequestBody GuessParameters parameters) {
//        return guessService.loginGuess(parameters.getPhonenum());
//    }


    @RequestMapping(params = "initDate")
    public AjaxJson initDate() {
        AjaxJson j = new AjaxJson();
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sf.format(date);
        if ("2017-02-11".equals(currentDate)||"2017-02-12".equals(currentDate)) {
            j.setMsg("活动时间禁止初始化数据");
            j.setSuccess(false);
            j.setCode(203);
            return j;
        }

        try {
            randomProductRedenvelope();
            saveAllTitles();
            j.setMsg("初始化数据成功");
            return j;
        } catch (Exception e) {
            j.setMsg("初始化数据失败,Exception: " + e.getMessage());
            return j;
        }
    }


    /**
     * 随机生成红包
     * 10%-1.88
     * 20%-1.68
     * 30%-1.18
     * 40%-1.08
     */
    @RequestMapping(params = "randomProductRedenvelope")
    public void randomProductRedenvelope() {

        int key = 0;
        Double level1 = 0.1;
        Double level2 = 0.2;
        Double level3 = 0.3;
        Double level4 = 0.4;
        int level1number = 0;
        int level2number = 0;
        int level3number = 0;
        int level4number = 0;
        Double totalRedenvelope = 20000.00;
        level1number = (int) (totalRedenvelope * level1 / 1.88 / 2); // 1063
        level2number = (int) (totalRedenvelope * level2 / 1.68 / 2); // 2380
        level3number = (int) (totalRedenvelope * level3 / 1.18 / 2); // 5084
        level4number = (int) (totalRedenvelope * level4 / 1.08 / 2); // 7407
        for (int i = 0; i < level4number; i++) {
            if (i < level1number) {
                rpush("redenvelopelevel1", "1.88");
            }
            if (i < level2number) {
                rpush("redenvelopelevel1", "1.68");
            }
            if (i < level3number) {
                rpush("redenvelopelevel1", "1.18");
            }
            rpush("redenvelopelevel1", "1.08");
        }
        for (int i = 0; i < level4number; i++) {
            if (i < level1number) {
                rpush("redenvelopelevel2", "1.88");
            }
            if (i < level2number) {
                rpush("redenvelopelevel2", "1.68");
            }
            if (i < level3number) {
                rpush("redenvelopelevel2", "1.18");
            }
            rpush("redenvelopelevel2", "1.08");
        }
    }

    //查询题目存到redis中
    @RequestMapping(params = "saveAllTitles")
    public void saveAllTitles() {
        guessService.listGuess(jedisPool);
    }

    /**
     * 随机取三道题目
     */
    @RequestMapping(params = "randomChooseThreeTitles")
    public AjaxJson randomChooseThreeTitles() {

        AjaxJson j = new AjaxJson();
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sf.format(date);
        if (!"2017-02-11".equals(currentDate)&&!"2017-02-12".equals(currentDate)) {
            j.setMsg("活动开始时间为2月11日0点整至2月12日24点整");
            j.setSuccess(false);
            j.setCode(203);
            return j;
        }

        if ("2017-02-11".equals(currentDate)){
            Long count =  llen("redenvelopelevel1");
            if (count<=0){
                j.setMsg("当前红包已发完,请明天再来");
                j.setSuccess(false);
                j.setCode(204);
                return j;
            }
        } else if ("2017-02-12".equals(currentDate)) {
            Long count =  llen("redenvelopelevel2");
            if (count<=0){
                j.setMsg("红包已发完");
                j.setSuccess(false);
                j.setCode(204);
                return j;
            }
        }

        List<GuessModel> guessModelList = new ArrayList<>();
        List<Integer> titleNoList = new ArrayList<>();
        Integer fristTitleno = (int) (Math.random() * 400 + 1);
        Integer secondTitleno = fristTitleno + 1;
        Integer thirdTitleno = fristTitleno + 2;
        titleNoList.add(fristTitleno);
        titleNoList.add(secondTitleno);
        titleNoList.add(thirdTitleno);
        for (int i : titleNoList) {
            GuessModel guessModel = new GuessModel();
            try {
                 guessModel = JacksonUtil.parseObject(getString("guessModel" + i), GuessModel.class);
            }catch (Exception e){
                randomChooseThreeTitles();
            }
            guessModelList.add(guessModel);
        }
        j.setSuccess(true);
        j.setCode(200);
        j.setData(guessModelList);
        return j;
    }


    /**
     * 开奖
     */
    @RequestMapping(params = "openWinning")
    public AjaxJson openWinning(@RequestBody BaseParameters parameters) {
        AjaxJson j = new AjaxJson();
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sf.format(date);
        if (!"2017-02-11".equals(currentDate)&&!"2017-02-12".equals(currentDate)) {
            j.setMsg("活动开始时间为2月11日0点整至2月12日24点整");
            j.setSuccess(false);
            j.setCode(203);
            return j;
        }
        if ("2017-02-11".equals(currentDate)){
           Long count =  llen("redenvelopelevel1");
           if (count<=0){
               j.setMsg("当前红包已发完,请明天再来");
               j.setSuccess(false);
               j.setCode(204);
               return j;
           }
        } else if ("2017-02-12".equals(currentDate)) {
            Long count =  llen("redenvelopelevel2");
            if (count<=0){
                j.setMsg("红包已发完");
                j.setSuccess(false);
                j.setCode(204);
                return j;
            }
        }

            //1、每个用户发红包只有一次
        String openid = getString(parameters.getOpenid());
        if (openid != null&& Objects.equals("0",openid)) {
            j.setMsg("你已参加过此活动！");
            j.setCode(201);
            return j;
        } else if(openid != null&&Double.valueOf(openid)>0) {
            Map<String,String> map = new HashMap<>();
            map.put("money",openid);
            j.setObj(map);
            j.setMsg("恭喜您获得了" + Double.valueOf(openid) + "元红包");
            j.setCode(200);
            return j;
        }

        //3、中奖概率1/3
        if (Math.random() < 0.67) {
            set(parameters.getOpenid(), "0");
            j.setMsg("未中奖！");
            j.setCode(202);
            return j;
        }
        //2、红包数量为0时提示未中奖 //周六周日两天
        String redenvelope = null;
        if ("2017-02-11".equals(currentDate)) {
                redenvelope = lpop("redenvelopelevel1");
        } else if ("2017-02-12".equals(currentDate)) {
            redenvelope = lpop("redenvelopelevel2");
        }
        if (redenvelope != null) {
            //wxRedPackRecordService.sendRedPack(new RedPackParameters().setMoney(Double.valueOf(redenvelope)).setOpenid(parameters.getOpenid()), 0);4
            Map<String,String> map = new HashMap<>();
            map.put("money",redenvelope);
            set(parameters.getOpenid(), redenvelope);
            j.setObj(map);
            j.setMsg("恭喜您获得了" + redenvelope + "元红包");
            j.setCode(200);
            return j;
        } else {
            set(parameters.getOpenid(), "0");
            j.setMsg("未中奖！");
            j.setCode(202);
            return j;
        }
    }


}
