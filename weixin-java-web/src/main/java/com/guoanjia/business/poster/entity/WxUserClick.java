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
@Table(name = "wx_user_click")
public class WxUserClick extends IdEntity implements Serializable {

    /**
     * 用户Openid
     */
    @Column(name = "UC_OPENID", length = 50)
    private String ucOpenid;

    /**
     * 用户点击类型
     */
    @Column(name = "UC_CLICK_TYPE", length = 2)
    private String ucClickType;

    /**
     * 用户点击次数
     */
    @Column(name = "UC_CLICK_COUNT", length = 2)
    private Integer ucClickCount;

    /**
     * 每天点击次数
     */
    @Column(name = "UC_COUNT", length = 2)
    private Integer ucCount;


    /**
     * 当前计数天数
     */
    @Column(name = "UC_CLICK_TIME")
    private Date ucClickTime;

    public String getUcOpenid() {
        return ucOpenid;
    }

    public WxUserClick setUcOpenid(String ucOpenid) {
        this.ucOpenid = ucOpenid == null ? "" : ucOpenid;
        return this;
    }

    public String getUcClickType() {
        return ucClickType;
    }

    public WxUserClick setUcClickType(String ucClickType) {
        this.ucClickType = ucClickType == null ? "" : ucClickType;
        return this;
    }

    public Integer getUcClickCount() {
        return ucClickCount;
    }

    public WxUserClick setUcClickCount(Integer ucClickCount) {
        this.ucClickCount = ucClickCount == null ? 0 : ucClickCount;
        return this;
    }

    public Integer getUcCount() {
        return ucCount;
    }

    public WxUserClick setUcCount(Integer ucCount) {
        this.ucCount = ucCount == null ? 0 : ucCount;
        return this;
    }

    public Date getUcClickTime() {
        return ucClickTime;
    }

    public WxUserClick setUcClickTime(Date ucClickTime) {
        this.ucClickTime = ucClickTime;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
