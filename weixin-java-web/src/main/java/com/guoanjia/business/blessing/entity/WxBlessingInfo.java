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
@Table(name = "wx_blessing_info")
public class WxBlessingInfo extends IdEntity implements Serializable {
    @Column(name = "BI_OPENID")
    private String biOpenid;
    @Column(name = "BI_SENDER")
    private String biSender;
    @Column(name = "BI_CONTENT")
    private String biContent;
    @Column(name = "BI_RECIPIENT")
    private String biRecipient;
    @Column(name = "BI_HEADIMG")
    private String biHeadimg;
    @Column(name = "BI_CREATE_TIME")
    private Date biCreateTime;

    public String getBiOpenid() {
        return biOpenid;
    }

    public WxBlessingInfo setBiOpenid(String biOpenid) {
        this.biOpenid = biOpenid == null ? "" : biOpenid;
        return this;
    }

    public String getBiSender() {
        return biSender;
    }

    public WxBlessingInfo setBiSender(String biSender) {
        this.biSender = biSender == null ? "" : biSender;
        return this;
    }


    public String getBiContent() {
        return biContent;
    }

    public WxBlessingInfo setBiContent(String biContent) {
        this.biContent = biContent;
        return this;
    }

    public String getBiRecipient() {
        return biRecipient;
    }

    public WxBlessingInfo setBiRecipient(String biRecipient) {
        this.biRecipient = biRecipient == null ? "" : biRecipient;
        return this;
    }

    public String getBiHeadimg() {
        return biHeadimg;
    }

    public WxBlessingInfo setBiHeadimg(String biHeadimg) {
        this.biHeadimg = biHeadimg == null ? "" : biHeadimg;
        return this;
    }

    public Date getBiCreateTime() {
        return biCreateTime;
    }

    public WxBlessingInfo setBiCreateTime(Date biCreateTime) {
        this.biCreateTime = biCreateTime;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
