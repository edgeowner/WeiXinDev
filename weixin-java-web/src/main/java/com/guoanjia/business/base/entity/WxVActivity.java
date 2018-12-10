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

package com.guoanjia.business.base.entity;

import com.github.asherli0103.core.entity.IdEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Entity
@Table(name = "wx_v_activity")
public class WxVActivity extends IdEntity implements Serializable {

    @Column(name = "ACT_NAME")
    private String actName;

    @Column(name = "ACT_MONEY")
    private Double actMoney;

    @Column(name = "ACT_OPENID")
    private String actOpenid;

    public String getActName() {
        return actName;
    }

    public WxVActivity setActName(String actName) {
        this.actName = actName == null ? "" : actName;
        return this;
    }

    public Double getActMoney() {
        return actMoney;
    }

    public WxVActivity setActMoney(Double actMoney) {
        this.actMoney = actMoney == null ? 0 : actMoney;
        return this;
    }

    public String getActOpenid() {
        return actOpenid;
    }

    public WxVActivity setActOpenid(String actOpenid) {
        this.actOpenid = actOpenid == null ? "" : actOpenid;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
