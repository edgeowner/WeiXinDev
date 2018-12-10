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

package com.guoanjia.business.blessing.entity;

import com.github.asherli0103.core.entity.IdEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Entity
@Table(name = "wx_capital_pool")
public class WxCapitalPool extends IdEntity implements Serializable {

    @Column(name = "CP_MONEY")
    private Double cpMoney;
    @Column(name = "CP_BALANCE")
    private Double cpBalance;
    @Column(name = "CP_CREATE_TIME")
    private Date cpCreateTime;
    @Column(name = "CP_END_TIME")
    private Date cpEndTime;

    @Column(name = "CP_DATE")
    private Date cpDate;

    @Column(name = "CP_STATUS")
    private String cpStatus;

    public Double getCpMoney() {
        return cpMoney;
    }

    public WxCapitalPool setCpMoney(Double cpMoney) {
        this.cpMoney = cpMoney == null ? 0 : cpMoney;
        return this;
    }

    public Double getCpBalance() {
        return cpBalance;
    }

    public WxCapitalPool setCpBalance(Double cpBalance) {
        this.cpBalance = cpBalance == null ? 0 : cpBalance;
        return this;
    }

    public Date getCpCreateTime() {
        return cpCreateTime;
    }

    public WxCapitalPool setCpCreateTime(Date cpCreateTime) {
        this.cpCreateTime = cpCreateTime;
        return this;
    }

    public Date getCpEndTime() {
        return cpEndTime;
    }

    public WxCapitalPool setCpEndTime(Date cpEndTime) {
        this.cpEndTime = cpEndTime;
        return this;
    }

    public Date getCpDate() {
        return cpDate;
    }

    public WxCapitalPool setCpDate(Date cpDate) {
        this.cpDate = cpDate;
        return this;
    }

    public String getCpStatus() {
        return cpStatus;
    }

    public WxCapitalPool setCpStatus(String cpStatus) {
        this.cpStatus = cpStatus;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
