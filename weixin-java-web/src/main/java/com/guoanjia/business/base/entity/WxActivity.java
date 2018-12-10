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

@Entity
@Table(name = "wx_activity", schema = "")
@SuppressWarnings("serial")
public class WxActivity extends IdEntity implements Serializable {

    /**
     * 活动名称
     */
    @Column(name = "ACT_NAME", length = 100)
    private String actName;

    /**
     * 活动规则
     */
    @Column(name = "ACT_RULE", length = 100)
    private String actRule;

    /**
     * 背景图片
     */
    @Column(name = "ACT_BACKIMG")
    private String actBackimg;

    /**
     * 最小提现金额
     */
    @Column(name = "ACT_MIN_AMOUNT", length = 32)
    private Double actMinAmount;

    /**
     * 最大提现金额
     */
    @Column(name = "ACT_MAX_AMOUNT", length = 32)
    private Double actMaxAmount;

    /**
     * 活动开始时间
     */
    @Column(name = "ACT_START_TIME")
    private Date actStartTime;

    /**
     * 活动结束时间
     */
    @Column(name = "ACT_END_TIME")
    private Date actEndTime;

    /**
     * 活动截止条件
     */
    @Column(name = "ACT_END_CONDITION")
    private String actEndCondition;

    @Column(name = "ACT_TYPE")
    private String actType;

    @Column(name = "ACT_CREATE_TIME")
    private Date actCreateTime;

    public String getActName() {
        return actName;
    }

    public WxActivity setActName(String actName) {
        this.actName = actName;
        return this;
    }

    public String getActRule() {
        return actRule;
    }

    public WxActivity setActRule(String actRule) {
        this.actRule = actRule;
        return this;
    }

    public String getActBackimg() {
        return actBackimg;
    }

    public WxActivity setActBackimg(String actBackimg) {
        this.actBackimg = actBackimg;
        return this;
    }

    public Double getActMinAmount() {
        return actMinAmount;
    }

    public WxActivity setActMinAmount(Double actMinAmount) {
        this.actMinAmount = actMinAmount;
        return this;
    }

    public Double getActMaxAmount() {
        return actMaxAmount;
    }

    public WxActivity setActMaxAmount(Double actMaxAmount) {
        this.actMaxAmount = actMaxAmount;
        return this;
    }

    public Date getActStartTime() {
        return actStartTime;
    }

    public WxActivity setActStartTime(Date actStartTime) {
        this.actStartTime = actStartTime;
        return this;
    }

    public Date getActEndTime() {
        return actEndTime;
    }

    public WxActivity setActEndTime(Date actEndTime) {
        this.actEndTime = actEndTime;
        return this;
    }

    public String getActEndCondition() {
        return actEndCondition;
    }

    public WxActivity setActEndCondition(String actEndCondition) {
        this.actEndCondition = actEndCondition;
        return this;
    }

    public String getActType() {
        return actType;
    }

    public WxActivity setActType(String actType) {
        this.actType = actType;
        return this;
    }

    public Date getActCreateTime() {
        return actCreateTime;
    }

    public WxActivity setActCreateTime(Date actCreateTime) {
        this.actCreateTime = actCreateTime;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
