package com.guoanjia.business.wechatinformation.service;

import com.guoanjia.business.wechatinformation.entity.WechatInformationEntity;

/**
 * Created by Administrator on 2017/3/10 0010.
 */
public interface WechatInformationService {
    /**
     * 保存采集的用户信息
     * @param WechatInformationEntity
     * @return
     */
    boolean saveWechatInformation(WechatInformationEntity WechatInformationEntity);
}
