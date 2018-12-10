package com.guoanjia.business.wechatinformation.entity.jpa;

import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.guoanjia.business.wechatinformation.entity.WechatInformationEntity;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;

/**
 * Created by Administrator on 2017/3/10 0010.
 */
@Repository
public interface WechatInformationRepository extends BaseRepository<WechatInformationEntity,String>{
}
