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

package com.guoanjia.business.poster.entity;

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
@Table(name = "wx_agent_funds")
public class WxAgentFunds extends IdEntity implements Serializable {

    @Column(name = "A_OPENID")
    private String aOpenid;

    @Column(name = "A_MONEY")
    private Double aMoney;

    @Column(name = "A_STATUS")
    private String aStatus;

    @Column(name = "A_DESCRIPTION")
    private String aDescription;

    @Column(name = "CREATETIME")
    private Date createtime;

    public String getaOpenid() {
        return aOpenid;
    }

    public WxAgentFunds setaOpenid(String aOpenid) {
        this.aOpenid = aOpenid == null ? "" : aOpenid;
        return this;
    }

    public Double getaMoney() {
        return aMoney;
    }

    public WxAgentFunds setaMoney(Double aMoney) {
        this.aMoney = aMoney == null ? 0 : aMoney;
        return this;
    }

    public String getaStatus() {
        return aStatus;
    }

    public WxAgentFunds setaStatus(String aStatus) {
        this.aStatus = aStatus == null ? "" : aStatus;
        return this;
    }

    public String getaDescription() {
        return aDescription;
    }

    public WxAgentFunds setaDescription(String aDescription) {
        this.aDescription = aDescription == null ? "" : aDescription;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public WxAgentFunds setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
