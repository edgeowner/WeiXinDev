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
@Table(name = "wx_user_activity")
public class WxUserActivity extends IdEntity implements Serializable {

    @Column(name = "UACT_NAME")
    private String uactName;
    @Column(name = "UACT_OPENID")
    private String uactOpenid;
    @Column(name = "UACT_UID")
    private String uactUid;
    @Column(name = "UACT_ACTID")
    private String uactActid;
    @Column(name = "UACT_CREATE_TIME")
    private Date uactCreateTime;
    @Column(name = "UACT_USERPHONE")
    private String uactUserphone;


    public String getUactName() {
        return uactName;
    }

    public WxUserActivity setUactName(String uactName) {
        this.uactName = uactName;
        return this;
    }

    public String getUactOpenid() {
        return uactOpenid;
    }

    public WxUserActivity setUactOpenid(String uactOpenid) {
        this.uactOpenid = uactOpenid;
        return this;
    }

    public String getUactUid() {
        return uactUid;
    }

    public WxUserActivity setUactUid(String uactUid) {
        this.uactUid = uactUid;
        return this;
    }

    public String getUactActid() {
        return uactActid;
    }

    public WxUserActivity setUactActid(String uactActid) {
        this.uactActid = uactActid;
        return this;
    }

    public Date getUactCreateTime() {
        return uactCreateTime;
    }

    public WxUserActivity setUactCreateTime(Date uactCreateTime) {
        this.uactCreateTime = uactCreateTime;
        return this;
    }

    public String getUactUserphone() {
        return uactUserphone;
    }

    public WxUserActivity setUactUserphone(String uactUserphone) {
        this.uactUserphone = uactUserphone;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
