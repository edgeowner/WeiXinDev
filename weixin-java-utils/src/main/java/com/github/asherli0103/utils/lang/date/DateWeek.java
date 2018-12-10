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
 * 日期周枚举类
 *
 * @author Asherli
 * @version 1.0.00
 */
@SuppressWarnings(value = {"unused"})
public enum DateWeek {
    MONDAY("星期一", "Monday", "Mon.", 1), TUESDAY("星期二", "Tuesday", "Tues.", 2),
    WEDNESDAY("星期三", "Wednesday", "Wed.", 3), THURSDAY("星期四", "Thursday", "Thur.", 4),
    FRIDAY("星期五", "Friday", "Fri.", 5), SATURDAY("星期六", "Saturday", "Sat.", 6), SUNDAY("星期日", "Sunday", "Sun.", 7);
    //中文
    String name_cn;
    //英文
    String name_en;
    //英文简写
    String name_enShort;
    //数字
    int number;

    /**
     * @param name_cn      String 中文名称
     * @param name_en      String 英文名琛
     * @param name_enShort String 英文短名称
     * @param number       int 数字名称
     */
    DateWeek(String name_cn, String name_en, String name_enShort, int number) {
        this.name_cn = name_cn;
        this.name_en = name_en;
        this.name_enShort = name_enShort;
        this.number = number;
    }

    /**
     * @return a value of String
     */
    public String getChineseName() {
        return name_cn;
    }

    /**
     * @return a value of String
     */
    public String getName() {
        return name_en;
    }

    /**
     * @return a value of String
     */
    public String getShortName() {
        return name_enShort;
    }

    /**
     * @return a value of int
     */
    public int getNumber() {
        return number;
    }
}
