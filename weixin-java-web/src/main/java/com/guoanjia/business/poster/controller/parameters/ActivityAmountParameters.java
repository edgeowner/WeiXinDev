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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */

public class ActivityAmountParameters {
    private String openid;
    private String actid;

    public String getOpenid() {
        return openid;
    }

    public ActivityAmountParameters setOpenid(String openid) {
        this.openid = openid == null ? "" : openid;
        return this;
    }

    public String getActid() {
        return actid;
    }

    public ActivityAmountParameters setActid(String actid) {
        this.actid = actid == null ? "" : actid;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
