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

package com.guoanjia.business.base.service.impl;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.ObjectUtil;
import com.github.asherli0103.utils.RandomUtil;
import com.guoanjia.business.base.entity.WxUser;
import com.guoanjia.business.base.entity.WxUserSubscribe;
import com.guoanjia.business.base.entity.jpa.WxUserRepository;
import com.guoanjia.business.base.entity.jpa.WxUserSubscribeRepository;
import com.guoanjia.business.base.service.WxUserService;
import com.guoanjia.business.base.service.WxUserSubscribeService;
import com.guoanjia.business.poster.controller.parameters.CheckVCodeParameters;
import com.guoanjia.business.poster.controller.parameters.SendVCodeParameters;
import com.guoanjia.business.utils.ContextHolderUtils;
import com.guoanjia.business.utils.MessageUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxUserSubscribeServiceImpl extends BaseServiceImpl<WxUserSubscribe, String> implements WxUserSubscribeService {

    private final WxUserSubscribeRepository wxUserSubscribeRepository;

    public WxUserSubscribeServiceImpl(BaseRepository<WxUserSubscribe, String> baseRepository, WxUserSubscribeRepository wxUserSubscribeRepository) {
        super(baseRepository);
        this.wxUserSubscribeRepository = wxUserSubscribeRepository;
    }



}
