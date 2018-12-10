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
@Table(name = "wx_user_relation")
public class WxUserRelation extends IdEntity implements Serializable {

    /**
     * 用户Openid
     */
    @Column(name = "UR_OPENID", length = 50)
    private String urOpenid;

    /**
     * 传播者Openid
     */
    @Column(name = "UR_PARENT_OPENID", length = 50)
    private String urParentOpenid;

    /**
     * 关系创建时间
     */
    @Column(name = "UR_CREATE_TIME", length = 50)
    private Date urCreateTime;

    public String getUrOpenid() {
        return urOpenid;
    }

    public WxUserRelation setUrOpenid(String urOpenid) {
        this.urOpenid = urOpenid == null ? "" : urOpenid;
        return this;
    }

    public String getUrParentOpenid() {
        return urParentOpenid;
    }

    public WxUserRelation setUrParentOpenid(String urParentOpenid) {
        this.urParentOpenid = urParentOpenid == null ? "" : urParentOpenid;
        return this;
    }

    public Date getUrCreateTime() {
        return urCreateTime;
    }

    public WxUserRelation setUrCreateTime(Date urCreateTime) {
        this.urCreateTime = urCreateTime;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
