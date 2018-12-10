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

public class SendVCodeParameters {
    private String phoneNumber;
    private String openid;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public SendVCodeParameters setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? "" : phoneNumber;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public SendVCodeParameters setOpenid(String openid) {
        this.openid = openid == null ? "" : openid;
        return this;
    }
}
