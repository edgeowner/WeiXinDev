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

package com.github.asherli0103.utils;


import com.github.asherli0103.utils.exception.UtilException;

import java.math.BigDecimal;
import java.text.MessageFormat;


/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class NumberUtil {

    /**
     * 转换为int<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    private static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            if (StringUtil.isBlank(String.valueOf(value))) {
                return defaultValue;
            }
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将字符串转换为BigDecimal，一般用于数字运算时。
     *
     * @param str 字符串
     * @return BigDecimal, str为empty时返回null。
     */
    public static BigDecimal toBigDecimal(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        if (!VallidateUtil.isNumber(str)) {
            throw new UtilException(MessageFormat.format("输入字符串 {0} 不是数字类型或不完全为数组类型", str));
        }

        return new BigDecimal(str);
    }

    /**
     * 将字符串抓换为double，如果失败返回默认值。
     *
     * @param str          字符串
     * @param defaultValue 失败时返回的默认值
     * @return double
     */
    public static double toDouble(String str, double defaultValue) {
        if (StringUtil.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 将字符串抓换为float，如果失败返回默认值。
     *
     * @param str          字符串
     * @param defaultValue 失败时返回的默认值
     * @return float
     */
    public static float toFloat(String str, float defaultValue) {
        if (StringUtil.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 将字符串抓换为long，如果失败返回默认值。
     *
     * @param str          字符串
     * @param defaultValue 失败时返回的默认值
     * @return long
     */
    public static long toLong(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 将字符串抓换为int，如果失败返回默认值。
     *
     * @param str          字符串
     * @param defaultValue 失败时返回的默认值
     * @return int
     */
    public static int toInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

}
