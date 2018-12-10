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
@Table(name = "wx_user_extract")
public class WxUserExtract extends IdEntity implements Serializable {

    @Column(name = "UE_ID")
    private String urId;

    @Column(name = "UE_OPENID")
    private String ueOpenid;

    @Column(name = "UE_TYPE")
    private String ueType;

    @Column(name = "UE_MONEY")
    private Double ueMoney;

    @Column(name = "UE_CREATETIME")
    private Date ueCreatetime;

    @Column(name = "UE_USERPHONE")
    private String ueUserphone;

    @Column(name = "ACT_ID")
    private String actId;

    public WxUserExtract() {
    }

    public String getUrId() {
        return urId;
    }

    public WxUserExtract setUrId(String urId) {
        this.urId = urId == null ? "" : urId;
        return this;
    }

    public String getUeOpenid() {
        return ueOpenid;
    }

    public WxUserExtract setUeOpenid(String ueOpenid) {
        this.ueOpenid = ueOpenid == null ? "" : ueOpenid;
        return this;
    }

    public String getUeType() {
        return ueType;
    }

    public WxUserExtract setUeType(String ueType) {
        this.ueType = ueType == null ? "" : ueType;
        return this;
    }

    public Double getUeMoney() {
        return ueMoney;
    }

    public WxUserExtract setUeMoney(Double ueMoney) {
        this.ueMoney = ueMoney == null ? 0 : ueMoney;
        return this;
    }

    public Date getUeCreatetime() {
        return ueCreatetime;
    }

    public WxUserExtract setUeCreatetime(Date ueCreatetime) {
        this.ueCreatetime = ueCreatetime;
        return this;
    }

    public String getUeUserphone() {
        return ueUserphone;
    }

    public WxUserExtract setUeUserphone(String ueUserphone) {
        this.ueUserphone = ueUserphone == null ? "" : ueUserphone;
        return this;
    }

    public String getActId() {
        return actId;
    }

    public WxUserExtract setActId(String actId) {
        this.actId = actId == null ? "" : actId;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
