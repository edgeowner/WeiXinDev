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
import com.guoanjia.business.blessing.entity.WxBlessingInfo;
import com.guoanjia.business.blessing.entity.jpa.WxBlessingInfoRepository;
import com.guoanjia.business.blessing.service.WxBlessingInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.github.asherli0103.utils.enums.ErrorCode.FAILED_LOAD_DATA;
import static com.github.asherli0103.utils.enums.ErrorCode.FAILED_SAVE_DATA;
import static com.github.asherli0103.utils.enums.ErrorCode.MISS_REQUIRED_PARAMETER;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxBlessingInfoServiceImpl extends BaseServiceImpl<WxBlessingInfo, String> implements WxBlessingInfoService {
    private final WxBlessingInfoRepository wxBlessingInfoRepository;

    public WxBlessingInfoServiceImpl(BaseRepository<WxBlessingInfo, String> baseRepository, WxBlessingInfoRepository wxBlessingInfoRepository) {
        super(baseRepository);
        this.wxBlessingInfoRepository = wxBlessingInfoRepository;
    }

    @Override
    public AjaxJson saveBlessingInfo(WxBlessingInfo wxBlessingInfo) {
        AjaxJson ajaxJson = new AjaxJson();
        if (ObjectUtil.isNotNull(wxBlessingInfo)) {
            String openid = wxBlessingInfo.getBiOpenid();
            if (StringUtils.isNoneBlank(openid,  wxBlessingInfo.getBiSender(), wxBlessingInfo.getBiContent(), wxBlessingInfo.getBiHeadimg())) {
                Criteria<WxBlessingInfo> blessingInfoCriteria = new Criteria<>();
                blessingInfoCriteria.add(Restrictions.eq("biOpenid", openid, true));
                WxBlessingInfo wxBlessingInfo1 = this.wxBlessingInfoRepository.findOne(blessingInfoCriteria);
                wxBlessingInfo.setBiCreateTime(new Date());
               if (ObjectUtil.isNotNull(wxBlessingInfo1)){
                   wxBlessingInfo.setId(wxBlessingInfo1.getId());
                   super.save(wxBlessingInfo);
               }else {
                   super.save(wxBlessingInfo);
               }
               return ajaxJson;
            }
            ajaxJson.setMsgAndCode(FAILED_SAVE_DATA);
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        ajaxJson.setMsgAndCode(MISS_REQUIRED_PARAMETER);
        ajaxJson.setSuccess(false);
        return ajaxJson;
    }

    @Override
    @Transactional(readOnly = true)
    public AjaxJson getBlessingInfo(String openid){
        AjaxJson ajaxJson = new AjaxJson();
        if (StringUtils.isNotBlank(openid)){
            Criteria<WxBlessingInfo> blessingInfoCriteria = new Criteria<>();
            blessingInfoCriteria.add(Restrictions.eq("biOpenid", openid, true));
            WxBlessingInfo wxBlessingInfo =  this.wxBlessingInfoRepository.findOne(blessingInfoCriteria);
            if (ObjectUtil.isNotNull(wxBlessingInfo)){
                ajaxJson.setData(wxBlessingInfo);
                return ajaxJson;
            }
            ajaxJson.setSuccess(false);
            ajaxJson.setMsgAndCode(FAILED_LOAD_DATA);
            return ajaxJson;
        }
        ajaxJson.setMsgAndCode(MISS_REQUIRED_PARAMETER);
        ajaxJson.setSuccess(false);
        return ajaxJson;
    }

}
