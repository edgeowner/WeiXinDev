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
import java.util.Date;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Entity
@Table(name = "wx_user_funds")
public class WxUserFunds extends IdEntity implements Serializable {

    @Column(name = "UR_ID")
    private String urId;

    @Column(name = "UF_OPENID")
    private String ufOpenid;

    @Column(name = "UF_TYPE")
    private String ufType;

    @Column(name = "UF_MONEY")
    private Double ufMoney;

    @Column(name = "UF_CREATETIME")
    private Date ufCreatetime;

    @Column(name = "UF_USERPHONE")
    private String ufUserphone;

    @Column(name = "ACT_ID")
    private String actId;

    public WxUserFunds() {
    }

    public WxUserFunds(String urId, String ufOpenid, String ufType, Double ufMoney, Date ufCreatetime) {
        this.urId = urId;
        this.ufOpenid = ufOpenid;
        this.ufType = ufType;
        this.ufMoney = ufMoney;
        this.ufCreatetime = ufCreatetime;
    }

    public String getUrId() {
        return urId;
    }

    public WxUserFunds setUrId(String urId) {
        this.urId = urId == null ? "" : urId;
        return this;
    }

    public String getUfOpenid() {
        return ufOpenid;
    }

    public WxUserFunds setUfOpenid(String ufOpenid) {
        this.ufOpenid = ufOpenid == null ? "" : ufOpenid;
        return this;
    }

    public String getUfType() {
        return ufType;
    }

    public WxUserFunds setUfType(String ufType) {
        this.ufType = ufType == null ? "" : ufType;
        return this;
    }

    public Double getUfMoney() {
        return ufMoney;
    }

    public WxUserFunds setUfMoney(Double ufMoney) {
        this.ufMoney = ufMoney == null ? 0 : ufMoney;
        return this;
    }

    public Date getUfCreatetime() {
        return ufCreatetime;
    }

    public WxUserFunds setUfCreatetime(Date ufCreatetime) {
        this.ufCreatetime = ufCreatetime;
        return this;
    }

    public String getUfUserphone() {
        return ufUserphone;
    }

    public WxUserFunds setUfUserphone(String ufUserphone) {
        this.ufUserphone = ufUserphone == null ? "" : ufUserphone;
        return this;
    }

    public String getActId() {
        return actId;
    }

    public WxUserFunds setActId(String actId) {
        this.actId = actId == null ? "" : actId;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
