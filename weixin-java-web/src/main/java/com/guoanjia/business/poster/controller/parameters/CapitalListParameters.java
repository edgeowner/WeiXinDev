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

package com.guoanjia.business.poster.controller.parameters;

import com.guoanjia.business.base.entity.WxVFunds;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */

public class CapitalListParameters extends WxVFunds {
    private Integer pageIndex;
    private Integer pageSize;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public CapitalListParameters setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex == null ? 0 : pageIndex;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public CapitalListParameters setPageSize(Integer pageSize) {
        this.pageSize = pageSize == null ? 0 : pageSize;
        return this;
    }
}
