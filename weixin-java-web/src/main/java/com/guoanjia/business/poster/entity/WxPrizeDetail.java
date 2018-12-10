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

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Entity
@Table(name = "wx_prize_detail")
public class WxPrizeDetail extends IdEntity implements Serializable {

    /**
     * 活动ID
     */
    @Column(name = "ACT_ID", length = 50)
    private String actId;

    /**
     * 奖品领取条件
     */
    @Column(name = "PD_CONDITION", length = 50)
    private String pdCondition;

    /**
     * 奖品类型
     */
    @Column(name = "PD_TYPR", length = 50)
    private String pdType;

    /**
     * 奖品数量或金额
     */
    @Column(name = "PD_ITEM", length = 50)
    private Double pdItem;

    /**
     * 奖品状态
     */
    @Column(name = "PD_STATUS", length = 50)
    private String pdStatus;

    public String getActId() {
        return actId;
    }

    public WxPrizeDetail setActId(String actId) {
        this.actId = actId == null ? "" : actId;
        return this;
    }

    public String getPdCondition() {
        return pdCondition;
    }

    public WxPrizeDetail setPdCondition(String pdCondition) {
        this.pdCondition = pdCondition == null ? "" : pdCondition;
        return this;
    }

    public String getPdType() {
        return pdType;
    }

    public WxPrizeDetail setPdType(String pdType) {
        this.pdType = pdType == null ? "" : pdType;
        return this;
    }

    public Double getPdItem() {
        return pdItem;
    }

    public WxPrizeDetail setPdItem(Double pdItem) {
        this.pdItem = pdItem == null ? 0 : pdItem;
        return this;
    }

    public String getPdStatus() {
        return pdStatus;
    }

    public WxPrizeDetail setPdStatus(String pdStatus) {
        this.pdStatus = pdStatus == null ? "" : pdStatus;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
