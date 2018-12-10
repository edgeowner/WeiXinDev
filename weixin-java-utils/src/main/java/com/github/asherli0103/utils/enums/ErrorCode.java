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

package com.github.asherli0103.utils.enums;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public enum ErrorCode {


    MISS_REQUIRED_PARAMETER(100000, "缺少必要参数",false),



    FAILED_SAVE_DATA(110000, "保存数据失败",false),
    FAILED_LOAD_DATA(110001, "数据加载失败",false),


    //业务逻辑异常
    BUSINESS_MSG_PHONE_EMPTY(120001, "手机号码不能为空",false),
    BUSINESS_MSG_PHONE_ERROR(120002, "请输入正确的手机号",false),
    BUSINESS_MSG_SEND_SUCCESS(120003, "短信已发送,请注意查收",true),
    BUSINESS_VALIDATE_ERROR(120004, "验证码错误",false),
    BUSINESS_VALIDATE_TIME_OUT(120005, "验证码超时",false),
    BUSINESS_VALIDATE_EMPTY(120006, "验证码不能为空",false),
    BUSINESS_MSG_SEND_ERROR(120007, "验证短信发送失败,请稍后重试",false),
    BUSINESS_BLESSING_HAS_INVOLVED(120008, "您已经抢过红包啦！",false),
    BUSINESS_BLESSING_SEND_ERROR(120009, "红包发送失败！",false),
    BUSINESS_SEND_REDPACK_SUCCESS(120010, "您已领取红包",false),
    BUSINESS_SEND_REDPACK_ERROR(120010, "您不是中奖用户",false),
    UNAUTHORIZED_ACCESS(100001, "非法访问",false);
//    BUSINESS_MSG_PHONE_EMPTY(110001, "数据加载失败");
//    BUSINESS_MSG_PHONE_EMPTY(110001, "数据加载失败");


    private int code;
    private String message;
    private boolean success;

    ErrorCode(int code, String message,boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public ErrorCode setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ErrorCode setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
