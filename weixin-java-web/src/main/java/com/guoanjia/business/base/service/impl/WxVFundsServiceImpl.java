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

import com.github.asherli0103.core.jpa.criteria.Criteria;
import com.github.asherli0103.core.jpa.criteria.Functions;
import com.github.asherli0103.core.jpa.criteria.Restrictions;
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.github.asherli0103.utils.StringUtil;
import com.guoanjia.business.poster.controller.parameters.ActivityAmountParameters;
import com.guoanjia.business.poster.controller.parameters.CapitalListParameters;
import com.guoanjia.business.base.entity.WxVFunds;
import com.guoanjia.business.base.entity.jpa.WxVFundsRepository;
import com.guoanjia.business.base.service.WxVFundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.asherli0103.core.jpa.criteria.Function.FunctionName.SUM;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional(readOnly = true)
public class WxVFundsServiceImpl extends BaseServiceImpl<WxVFunds, String> implements WxVFundsService {
    private final WxVFundsRepository wxVFundsRepository;

    @Autowired
    public WxVFundsServiceImpl(BaseRepository<WxVFunds, String> baseRepository, WxVFundsRepository wxVFundsRepository) {
        super(baseRepository);
        this.wxVFundsRepository = wxVFundsRepository;
    }


    @Override
    public List<WxVFunds> findAll() {
        return wxVFundsRepository.findAll();
    }

    @Override
    public Page<WxVFunds> queryUserVFunds(CapitalListParameters capitalListParameters) {
        String openid = capitalListParameters.getVfOpenid();
        if (StringUtil.isBlank(capitalListParameters.getVfOpenid())) {
            openid = "0";
        }

        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "vfCreatetime");
        Sort sort = new Sort(order);

        Pageable pageable = new PageRequest(capitalListParameters.getPageIndex() - 1, capitalListParameters.getPageSize(), sort);

        Criteria<WxVFunds> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("vfOpenid", openid, true));
        criteria.add(Restrictions.eq("actName", capitalListParameters.getActName(), true));

        return wxVFundsRepository.findAll(criteria, pageable);
    }

    @Override
    public double queryPersonalFunds(ActivityAmountParameters activityAmountParameters) {
        String openid = activityAmountParameters.getOpenid();
        String actId = activityAmountParameters.getActid();
        if (StringUtil.isBlank(openid)) {
            openid = "0";
        }
        String fundsType = "00,01,02";
        String[] fundsTypeArray = fundsType.split(",");
        List<String> fundsTypes = Arrays.asList(fundsTypeArray);

        Criteria<WxVFunds> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("vfOpenid", openid, true));
        //criteria.add(Restrictions.eq("actName", actId, true));
        criteria.add(Restrictions.in("vfType", fundsTypes, true));

        List<Functions> selections = new ArrayList<>();
        selections.add(new Functions("vfMoney", SUM));

        Double dd = wxVFundsRepository.functionQuery(criteria, Double.class, null, selections);
        if (null == dd) {
            return 0;
        }
        return dd;
    }


}
