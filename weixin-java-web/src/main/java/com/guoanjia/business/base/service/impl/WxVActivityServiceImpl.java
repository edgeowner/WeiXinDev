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
import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.guoanjia.business.base.entity.WxVActivity;
import com.guoanjia.business.base.entity.jpa.WxVActivityRepository;
import com.guoanjia.business.base.service.WxVActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional(readOnly = true)
public class WxVActivityServiceImpl extends BaseServiceImpl<WxVActivity, String> implements WxVActivityService {

    private final WxVActivityRepository wxVActivityRepository;

    @Autowired
    public WxVActivityServiceImpl(BaseRepository<WxVActivity, String> baseRepository, WxVActivityRepository wxVActivityRepository) {
        super(baseRepository);
        this.wxVActivityRepository = wxVActivityRepository;
    }

    /**
     * 根据条件查询活动信息
     *
     * @param wxActivity 根据条件查询活动信息
     * @return
     */
    @Override
    public AjaxJson findActivityByCondition(WxVActivity wxActivity) {
        AjaxJson ajaxJson = new AjaxJson();
        Example<WxVActivity> wxActivityExample = Example.of(wxActivity);
        List<WxVActivity> wxVActivities = wxVActivityRepository.findAll(wxActivityExample);
        if (wxVActivities.size() == 0) {
            ajaxJson.setSuccess(true);
            ajaxJson.setData(new ArrayList<>());
            ajaxJson.setMsg("活动不存在");
            return ajaxJson;
        }
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("活动存在");
        ajaxJson.setData(wxVActivities);
        return ajaxJson;
    }


}
