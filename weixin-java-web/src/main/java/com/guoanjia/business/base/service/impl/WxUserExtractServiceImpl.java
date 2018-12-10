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

import com.github.asherli0103.core.jpa.domain.BaseRepository;
import com.github.asherli0103.core.jpa.service.impl.BaseServiceImpl;
import com.guoanjia.business.base.entity.WxUserExtract;
import com.guoanjia.business.base.entity.jpa.WxUserExtractRepository;
import com.guoanjia.business.base.service.WxUserExtractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Service
@Transactional
public class WxUserExtractServiceImpl extends BaseServiceImpl<WxUserExtract, String> implements WxUserExtractService {

    private final WxUserExtractRepository wxUserExtractRepository;
    public WxUserExtractServiceImpl(BaseRepository<WxUserExtract, String> baseRepository, WxUserExtractRepository wxUserExtractRepository) {
        super(baseRepository);
        this.wxUserExtractRepository = wxUserExtractRepository;
    }


    @Override
    public void deleteExtract(WxUserExtract wxUserExtract){
        wxUserExtractRepository.delete(wxUserExtract);
    }


}
