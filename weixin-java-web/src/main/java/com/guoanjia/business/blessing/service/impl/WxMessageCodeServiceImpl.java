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
import com.github.asherli0103.utils.DateUtil;
import com.github.asherli0103.utils.ObjectUtil;
import com.github.asherli0103.utils.enums.ErrorCode;
import com.guoanjia.business.blessing.entity.WxMessageCode;
import com.guoanjia.business.blessing.entity.jpa.WxMessageCodeRepository;
import com.guoanjia.business.blessing.service.WxMessageCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class WxMessageCodeServiceImpl extends BaseServiceImpl<WxMessageCode, String> implements WxMessageCodeService {

    private final WxMessageCodeRepository wxMessageCodeRepository;

    public WxMessageCodeServiceImpl(BaseRepository<WxMessageCode, String> baseRepository, WxMessageCodeRepository wxMessageCodeRepository) {
        super(baseRepository);
        this.wxMessageCodeRepository = wxMessageCodeRepository;
    }

    @Override
    public void saveCode(WxMessageCode wxMessageCode) {
        Criteria<WxMessageCode> codeCriteria = new Criteria<>();
        codeCriteria.add(Restrictions.eq("mcUserphone", wxMessageCode.getMcUserphone(), true));
        WxMessageCode wxMessageCode1 = wxMessageCodeRepository.findOne(codeCriteria);
        if (ObjectUtil.isNotEmpty(wxMessageCode1)) {
            wxMessageCode.setId(wxMessageCode1.getId());
        }
        wxMessageCodeRepository.save(wxMessageCode);
    }

    @Override
    @Transactional(readOnly = true)
    public AjaxJson getCode(String userphone, String vcode) {
        AjaxJson ajaxJson = new AjaxJson();
        Criteria<WxMessageCode> codeCriteria = new Criteria<>();
        codeCriteria.add(Restrictions.eq("mcUserphone", userphone, true));
        WxMessageCode wxMessageCode1 = wxMessageCodeRepository.findOne(codeCriteria);
        if (ObjectUtil.isNotEmpty(wxMessageCode1)) {
            Date currentDate = new Date();
            Date date = DateUtil.addSecond(wxMessageCode1.getMcCreateTime(), 60);
            if (DateUtil.betweenDays(wxMessageCode1.getMcCreateTime(), date, currentDate)) {
                if (Objects.equals(wxMessageCode1.getMcCode(), vcode)) {
                    return ajaxJson;
                }
                ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_VALIDATE_ERROR);
                return ajaxJson;
            }
            ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_VALIDATE_TIME_OUT);
            return ajaxJson;

        }
        ajaxJson.setMsgAndCode(ErrorCode.BUSINESS_VALIDATE_ERROR);
        return ajaxJson;
    }
}
