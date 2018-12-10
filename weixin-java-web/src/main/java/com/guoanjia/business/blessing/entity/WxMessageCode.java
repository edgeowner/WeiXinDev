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
@Table(name = "wx_message_code")
public class WxMessageCode extends IdEntity implements Serializable {

    @Column(name = "MC_USERPHONE")
    private String mcUserphone;

    @Column(name = "MC_CODE")
    private String mcCode;

    @Column(name = "MC_CREATE_TIME")
    private Date mcCreateTime;

    public String getMcUserphone() {
        return mcUserphone;
    }

    public WxMessageCode setMcUserphone(String mcUserphone) {
        this.mcUserphone = mcUserphone;
        return this;
    }

    public String getMcCode() {
        return mcCode;
    }

    public WxMessageCode setMcCode(String mcCode) {
        this.mcCode = mcCode;
        return this;
    }

    public Date getMcCreateTime() {
        return mcCreateTime;
    }

    public WxMessageCode setMcCreateTime(Date mcCreateTime) {
        this.mcCreateTime = mcCreateTime;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
