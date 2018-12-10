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

package com.guoanjia.business.lanfest.entity.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class BonusPoolModel {

    private int id;
    private String redenvelope;
    private String openid;
    private Date createdate;

    public int getId() {
        return id;
    }

    public BonusPoolModel setId(int id) {
        this.id = id;
        return this;
    }

    public String getRedenvelope() {
        return redenvelope;
    }

    public BonusPoolModel setRedenvelope(String redenvelope) {
        this.redenvelope = redenvelope;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public BonusPoolModel setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public BonusPoolModel setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
