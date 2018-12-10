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
@Table(name = "wx_v_funds")
public class WxVFunds extends IdEntity implements Serializable {

    @Column(name = "VF_OPENID")
    private String vfOpenid;

    @Column(name = "VF_TYPE")
    private String vfType;

    @Column(name = "VF_MONEY")
    private Double vfMoney;

    @Column(name = "VF_CREATETIME")
    private Date vfCreatetime;

    @Column(name = "VF_USERPHONE")
    private String vfUserphone;
    /**
     * 活动名称
     */
    @Column(name = "ACT_NAME", length = 100)
    private String actName;

    public WxVFunds() {
    }

    public String getVfOpenid() {
        return vfOpenid;
    }

    public WxVFunds setVfOpenid(String vfOpenid) {
        this.vfOpenid = vfOpenid == null ? "" : vfOpenid;
        return this;
    }

    public String getVfType() {
        return vfType;
    }

    public WxVFunds setVfType(String vfType) {
        this.vfType = vfType == null ? "" : vfType;
        return this;
    }

    public Double getVfMoney() {
        return vfMoney;
    }

    public WxVFunds setVfMoney(Double vfMoney) {
        this.vfMoney = vfMoney == null ? 0 : vfMoney;
        return this;
    }

    public Date getVfCreatetime() {
        return vfCreatetime;
    }

    public WxVFunds setVfCreatetime(Date vfCreatetime) {
        this.vfCreatetime = vfCreatetime;
        return this;
    }

    public String getVfUserphone() {
        return vfUserphone;
    }

    public WxVFunds setVfUserphone(String vfUserphone) {
        this.vfUserphone = vfUserphone == null ? "" : vfUserphone;
        return this;
    }

    public String getActName() {
        return actName;
    }

    public WxVFunds setActName(String actName) {
        this.actName = actName == null ? "" : actName;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
