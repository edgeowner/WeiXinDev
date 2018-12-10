package com.guoanjia.business.wechatinformation.service.imp;

import com.guoanjia.business.wechatinformation.entity.WechatInformationEntity;
import com.guoanjia.business.wechatinformation.entity.jpa.WechatInformationRepository;
import com.guoanjia.business.wechatinformation.service.WechatInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by Administrator on 2017/3/10 0010.
 */
@Service
@Transactional
public class WechatInformationServiceImp implements WechatInformationService{
    private final WechatInformationRepository wechatInformationRepository;

    public WechatInformationServiceImp(WechatInformationRepository wechatInformationRepository) {
        this.wechatInformationRepository = wechatInformationRepository;
    }


    @Override
    public boolean saveWechatInformation(WechatInformationEntity wechatInformationEntity) {
        this.wechatInformationRepository.save(wechatInformationEntity);
        return true;
    }
}
