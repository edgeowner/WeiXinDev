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

package com.github.asherli0103.utils.lang.date;

/**
 * 日期格式样式,用于自动判断日期格式
 *
 * @author Asherli
 * @version 1.0.00
 */
public enum DateFormatStyle {

    YYYY_MM_CONNECTOR(DateFormatConstant.YYYY_MM_CONNECTOR, 1, false),
    YYYY_MM_DD_CONNECTOR(DateFormatConstant.YYYY_MM_DD_CONNECTOR, 2, false),
    YYYY_MM_DD_HH_CONNECTOR(DateFormatConstant.YYYY_MM_DD_HH_CONNECTOR, 3, false),
    YYYY_MM_DD_HH_MM_CONNECTOR(DateFormatConstant.YYYY_MM_DD_HH_MM_CONNECTOR, 4, false),
    YYYY_MM_DD_HH_MM_SS_CONNECTOR(DateFormatConstant.YYYY_MM_DD_HH_MM_SS_CONNECTOR, 5, false),

    YYYY_MM_BACKSLASH(DateFormatConstant.YYYY_MM_BACKSLASH, 6, false),
    YYYY_MM_DD_BACKSLASH(DateFormatConstant.YYYY_MM_DD_BACKSLASH, 7, false),
    YYYY_MM_DD_HH_BACKSLASH(DateFormatConstant.YYYY_MM_DD_HH_BACKSLASH, 8, false),
    YYYY_MM_DD_HH_MM_BACKSLASH(DateFormatConstant.YYYY_MM_DD_HH_MM_BACKSLASH, 9, false),
    YYYY_MM_DD_HH_MM_SS_BACKSLASH(DateFormatConstant.YYYY_MM_DD_HH_MM_SS_BACKSLASH, 10, false),

    YYYY_MM_CHINESE(DateFormatConstant.YYYY_MM_CHINESE, 11, false),
    YYYY_MM_DD_CHINESE(DateFormatConstant.YYYY_MM_DD_CHINESE, 12, false),

    YYYY_MM_DD_HH_CHINESE(DateFormatConstant.YYYY_MM_DD_HH_CHINESE, 13, false),
    YYYY_MM_DD_HH_MM_CHINESE(DateFormatConstant.YYYY_MM_DD_HH_MM_CHINESE, 14, false),
    YYYY_MM_DD_HH_MM_SS_CHINESE(DateFormatConstant.YYYY_MM_DD_HH_MM_SS_CHINESE, 15, false),

    HH_MM(DateFormatConstant.HH_MM, 16, true),
    HH_MM_SS(DateFormatConstant.HH_MM_SS, 17, true),

    MM_DD_CONNECTOR(DateFormatConstant.MM_DD_CONNECTOR, 18, true),
    MM_DD_HH_CONNECTOR(DateFormatConstant.MM_DD_HH_CONNECTOR, 19, true),
    MM_DD_HH_MM_CONNECTOR(DateFormatConstant.MM_DD_HH_MM_CONNECTOR, 20, true),
    MM_DD_HH_MM_SS_CONNECTOR(DateFormatConstant.MM_DD_HH_MM_SS_CONNECTOR, 21, true),

    MM_DD_BACKSLASH(DateFormatConstant.MM_DD_BACKSLASH, 22, true),
    MM_DD_HH_BACKSLASH(DateFormatConstant.MM_DD_HH_BACKSLASH, 23, true),
    MM_DD_HH_MM_BACKSLASH(DateFormatConstant.MM_DD_HH_MM_BACKSLASH, 24, true),
    MM_DD_HH_MM_SS_BACKSLASH(DateFormatConstant.MM_DD_HH_MM_SS_BACKSLASH, 25, true),

    MM_DD_CHINESE(DateFormatConstant.MM_DD_CHINESE, 26, true),
    MM_DD_HH_CHINESE(DateFormatConstant.MM_DD_HH_CHINESE, 27, true),
    MM_DD_HH_MM_CHINESE(DateFormatConstant.MM_DD_HH_MM_CHINESE, 28, true),
    MM_DD_HH_MM_SS_CHINESE(DateFormatConstant.MM_DD_HH_MM_SS_CHINESE, 29, true);

    //样式
    private String value;
    //序列
    private Integer number;
    //是仅用于显示
    private Boolean isShowOnly;

    DateFormatStyle(String value, Integer number, Boolean isShowOnly) {
        this.value = value;
        this.isShowOnly = isShowOnly;
        this.number = number;
    }

    public String getValue() {
        return value;
    }

    public Boolean isShowOnly() {
        return isShowOnly;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "DateFormatStyle{" +
                "value='" + value + '\'' +
                ", number=" + number +
                ", isShowOnly=" + isShowOnly +
                '}';
    }
}
