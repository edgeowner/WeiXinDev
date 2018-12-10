package com.guoanjia.business.wechatinformation.controller;

import com.guoanjia.business.wechatinformation.entity.WechatInformationEntity;
import com.guoanjia.business.wechatinformation.service.WechatInformationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by Administrator on 2017/3/10 0010.
 *
 * 用于采集用户数据信息；
 */
@RestController
@RequestMapping(value = {"getWechatInformation"})
public class WechatInformationController {

    private final WechatInformationService wechatInformationService;

    public WechatInformationController(WechatInformationService wechatInformationService) {
        this.wechatInformationService = wechatInformationService;
    }
    /**
     * 采集用户访问页面信息
     * @param
     */
    @RequestMapping(path = "saveWechatInfromation")
    public String saveWechatInfromation(@RequestBody WechatInformationEntity wechatInformationEntity){
        wechatInformationEntity.setCreatetime(new Date());
        wechatInformationEntity.setLeavepagetime(new Date());
        wechatInformationEntity.setCreatetime(new Date());
//        this.testServices.insertTest(testEntity);
        boolean b = wechatInformationService.saveWechatInformation(wechatInformationEntity);
        return "测试成功";
    }
}
