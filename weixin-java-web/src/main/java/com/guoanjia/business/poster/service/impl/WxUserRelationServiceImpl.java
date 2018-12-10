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

package com.guoanjia.business.poster.service.impl;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.guoanjia.business.base.entity.WxUserFunds;
import com.guoanjia.business.poster.entity.WxUserRelation;
import com.guoanjia.business.base.entity.jpa.WxUserFundsRepository;
import com.guoanjia.business.poster.entity.jpa.WxUserRelationRepository;
import com.guoanjia.business.poster.service.WxUserRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxUserRelationServiceImpl extends BaseServiceImpl<WxUserRelation, String> implements WxUserRelationService {

    private final WxUserRelationRepository wxUserRelationRepository;

    private final WxUserFundsRepository wxUserFundsRepository;

    public WxUserRelationServiceImpl(BaseRepository<WxUserRelation, String> baseRepository, WxUserRelationRepository wxUserRelationRepository, WxUserFundsRepository wxUserFundsRepository) {
        super(baseRepository);
        this.wxUserRelationRepository = wxUserRelationRepository;
        this.wxUserFundsRepository = wxUserFundsRepository;
    }

    @Override
    public AjaxJson saveUserRelation(String urOpenid, String urParentOpenid, Double money, String actId) {
        AjaxJson ajaxJson = new AjaxJson();
        WxUserRelation wxUserRelation = save(new WxUserRelation().setUrOpenid(urOpenid).setUrParentOpenid(urParentOpenid).setUrCreateTime(new Date()));
        if (null != wxUserRelation) {
            WxUserFunds wxUserFunds = wxUserFundsRepository.save(new WxUserFunds().setUrId(wxUserRelation.getId()).setUfCreatetime(new Date()).setUfOpenid(urOpenid).setUfType("01").setUfMoney(money).setActId(actId));
            if (null != wxUserFunds) {
                ajaxJson.setSuccess(true);
                return ajaxJson;
            }
        }
        ajaxJson.setSuccess(false);
        return ajaxJson;
    }

}
