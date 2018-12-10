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

/**
 * 用户发送短信验证码
 *
 * @author AsherLi0103
 * @version 1.0.00
 */

public class CheckVCodeParameters {
    private String vCode;
    private String openid;
    private String phoneNumber;
    private String nickname;
    private String city;
    private String coountry;
    private String province;

    public String getvCode() {
        return vCode;
    }

    public CheckVCodeParameters setvCode(String vCode) {
        this.vCode = vCode == null ? "" : vCode;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public CheckVCodeParameters setOpenid(String openid) {
        this.openid = openid == null ? "" : openid;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CheckVCodeParameters setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? "" : phoneNumber;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public CheckVCodeParameters setNickname(String nickname) {
        this.nickname = nickname == null ? "" : nickname;
        return this;
    }

    public String getCity() {
        return city;
    }

    public CheckVCodeParameters setCity(String city) {
        this.city = city == null ? "" : city;
        return this;
    }

    public String getCoountry() {
        return coountry;
    }

    public CheckVCodeParameters setCoountry(String coountry) {
        this.coountry = coountry == null ? "" : coountry;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public CheckVCodeParameters setProvince(String province) {
        this.province = province == null ? "" : province;
        return this;
    }
}
