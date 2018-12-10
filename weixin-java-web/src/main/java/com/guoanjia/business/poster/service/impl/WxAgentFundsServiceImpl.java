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
import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Functions;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.StringUtil;
import com.guoanjia.business.poster.entity.WxAgentFunds;
import com.guoanjia.business.poster.entity.jpa.WxAgentFundsRepository;
import com.guoanjia.business.poster.service.WxAgentFundsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.github.asherli0103.core.jpa.criteria.Function.FunctionName.SUM;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxAgentFundsServiceImpl extends BaseServiceImpl<WxAgentFunds, String> implements WxAgentFundsService {
    private final WxAgentFundsRepository wxAgentFundsRepository;

    public WxAgentFundsServiceImpl(BaseRepository<WxAgentFunds, String> baseRepository, WxAgentFundsRepository wxAgentFundsRepository) {
        super(baseRepository);
        this.wxAgentFundsRepository = wxAgentFundsRepository;
    }

    @Override
    public double queryAgentFunds(String openid) {
        if (StringUtil.isBlank(openid)) {
            openid = "0";
        }

        Criteria<WxAgentFunds> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("aOpenid", openid, true));

        List<Functions> selections = new ArrayList<>();
        selections.add(new Functions("aMoney", SUM));

        Double dd = wxAgentFundsRepository.functionQuery(criteria, Double.class, null, selections);
        if (null == dd) {
            return 0;
        }
        return dd;
    }

    @Override
    public AjaxJson queryAllAgentFunds(WxAgentFunds wxAgentFunds) {
        AjaxJson ajaxJson = new AjaxJson();
        String openid = wxAgentFunds.getaOpenid();
        if (StringUtil.isBlank(openid)) {
            openid = "0";
        }
        Criteria<WxAgentFunds> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("aOpenid", openid, true));

        List<WxAgentFunds> wxAgentFundss = wxAgentFundsRepository.findAll(criteria);
        if (wxAgentFundss.size() == 0) {
            ajaxJson.setData(new ArrayList<>());
            return ajaxJson;
        }
        ajaxJson.setData(wxAgentFundss);
        return ajaxJson;
    }
}
