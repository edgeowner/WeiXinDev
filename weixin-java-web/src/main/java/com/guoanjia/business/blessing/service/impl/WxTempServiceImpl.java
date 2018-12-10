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

package com.guoanjia.business.blessing.service.impl;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.ObjectUtil;
import com.github.asherli0103.utils.RandomUtil;
import com.guoanjia.business.base.entity.WxActivity;
import com.guoanjia.business.base.entity.WxUserExtract;
import com.guoanjia.business.base.entity.jpa.*;
import com.guoanjia.business.blessing.entity.WxCapitalPool;
import com.guoanjia.business.blessing.entity.WxTemp;
import com.guoanjia.business.blessing.entity.jpa.WxCapitalPoolRepository;
import com.guoanjia.business.blessing.service.WxCapitalPoolService;
import com.guoanjia.business.blessing.service.WxTempService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * //TODO  需增加日志记录
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxTempServiceImpl extends BaseServiceImpl<WxTemp, String> implements WxTempService {

    public WxTempServiceImpl(BaseRepository<WxTemp, String> baseRepository) {
        super(baseRepository);
    }

}
