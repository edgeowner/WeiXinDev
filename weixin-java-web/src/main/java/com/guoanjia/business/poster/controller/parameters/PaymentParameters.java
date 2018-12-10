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

package com.guoanjia.business.poster.controller.parameters;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class PaymentParameters {
    private Double money;
    private String openid;
    private String clientIp;
    private String userphone;
    private String vCode;
    private String actId;

    public Double getMoney() {
        return money;
    }

    public PaymentParameters setMoney(Double money) {
        this.money = money == null ? 0 : money;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public PaymentParameters setOpenid(String openid) {
        this.openid = openid == null ? "" : openid;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public PaymentParameters setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? "" : clientIp;
        return this;
    }

    public String getUserphone() {
        return userphone;
    }

    public PaymentParameters setUserphone(String userphone) {
        this.userphone = userphone == null ? "" : userphone;
        return this;
    }

    public String getvCode() {
        return vCode;
    }

    public PaymentParameters setvCode(String vCode) {
        this.vCode = vCode == null ? "" : vCode;
        return this;
    }

    public String getActId() {
        return actId;
    }

    public PaymentParameters setActId(String actId) {
        this.actId = actId == null ? "" : actId;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
