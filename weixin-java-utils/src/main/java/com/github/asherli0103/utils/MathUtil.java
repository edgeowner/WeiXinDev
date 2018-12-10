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

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * 数学运算辅助类
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
public class MathUtil {

    /**
     * 保留小数位
     *
     * @param number 被保留小数的数字
     * @param digit  保留的小数位数
     * @return 保留小数后的字符串
     */
    public static String roundStr(double number, int digit) {
        return String.format("%." + digit + 'f', number);
    }

    /**
     * 保留小数位
     *
     * @param number 被保留小数的数字
     * @param digit  保留的小数位数
     * @return 保留小数后的字符串
     */
    public static double round(double number, int digit) {
        return new BigDecimal(number).setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 得到两个 <code>double</code>值中最大的一个
     *
     * @param a 值 1
     * @param b 值 2
     * @return 最大的值
     */
    public static float getMax(float a, float b) {
        if (Float.isNaN(a)) {
            return b;
        } else if (Float.isNaN(b)) {
            return a;
        } else {
            return Math.max(a, b);
        }
    }

    /**
     * 得到两个 <code>double</code>值中最大的一个
     *
     * @param a 值 1
     * @param b 值 2
     * @return 最大的值
     */
    public static double getMax(double a, double b) {
        if (Double.isNaN(a)) {
            return b;
        } else if (Double.isNaN(b)) {
            return a;
        } else {
            return Math.max(a, b);
        }
    }

    /**
     * 得到数组中最大的一个
     *
     * @param array 数组不能为null，也不能为空。
     * @return 得到数组中最大的一个.
     * @throws IllegalArgumentException 如果 <code>数组</code> 是 <code>null</code>
     * @throws IllegalArgumentException 如果 <code>数组</code>是空
     */
    public static float getMax(float[] array) {
        if (array == null) {
            throw new IllegalArgumentException("数组不能为null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("数组不能为空.");
        }
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            max = getMax(array[j], max);
        }
        return max;
    }

    /**
     * 得到数组中最大的一个
     *
     * @param array 数组不能为null，也不能为空。
     * @return 得到数组中最大的一个.
     * @throws IllegalArgumentException 如果 <code>数组</code> 是 <code>null</code>
     * @throws IllegalArgumentException 如果 <code>数组</code>是空
     */
    public static double getMax(double[] array) {
        if (array == null) {
            throw new IllegalArgumentException("数组不能为null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("数组不能为空.");
        }
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            max = getMax(array[j], max);
        }
        return max;
    }


    /**
     * 得到两个float中最小的一个
     *
     * @param a 值 1
     * @param b 值 2
     * @return double值最小的
     */
    public static float getMin(float a, float b) {
        if (Float.isNaN(a)) {
            return b;
        } else if (Float.isNaN(b)) {
            return a;
        } else {
            return Math.min(a, b);
        }
    }

    /**
     * 得到两个double中最小的一个
     *
     * @param a 值 1
     * @param b 值 2
     * @return double值最小的
     */
    public static double getMin(double a, double b) {
        if (Double.isNaN(a)) {
            return b;
        } else if (Double.isNaN(b)) {
            return a;
        } else {
            return Math.min(a, b);
        }
    }


    /**
     * 返回数组中最小的数值。
     *
     * @param array 数组不能为null，也不能为空。
     * @return 数组里面最小的float
     * @throws IllegalArgumentException 如果<code>数组</code>是<code>null</code>
     * @throws IllegalArgumentException 如果<code>数组</code>是空
     */
    public static float getMin(float[] array) {
        if (array == null) {
            throw new IllegalArgumentException("数组不能为null。");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("数组不能为空。");
        }

        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = getMin(array[i], min);
        }
        return min;
    }

    /**
     * 返回数组中最小的double
     *
     * @param array 数组不能为null，也不能为空。
     * @return 数组里面最小的double
     * @throws IllegalArgumentException 如果<code>数组</code>是<code>null</code>
     * @throws IllegalArgumentException 如果<code>数组</code>是空
     */
    public static double getMin(double[] array) {
        if (array == null) {
            throw new IllegalArgumentException("数组不能为null。");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("数组不能为空。");
        }
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = getMin(array[i], min);
        }
        return min;
    }

    /**
     * 返回两个double的商 first除以second。
     *
     * @param first        第一个double
     * @param second       第二个double
     * @param scale        保留小数点的位数
     * @param roundingMode 小数模式。若不想选择可填写-1，会默认使用BigDecimal.ROUND_HALF_UP，即5舍6入。<br>
     *                     BigDecimal.ROUND_CEILING 如果 BigDecimal 是正的，则做 ROUND_UP 操作；如果为负，则做 ROUND_DOWN 操作。<br>
     *                     BigDecimal.ROUND_DOWN 从不在舍弃(即截断)的小数之前增加数字。<br>
     *                     BigDecimal.ROUND_FLOOR 如果 BigDecimal 为正，则作 ROUND_UP ；如果为负，则作 ROUND_DOWN 。<br>
     *                     BigDecimal.ROUND_HALF_DOWN 若舍弃部分&gt; .5，则作 ROUND_UP；否则，作 ROUND_DOWN 。<br>
     *                     BigDecimal.ROUND_HALF_EVEN 如果舍弃部分左边的数字为奇数，则作 ROUND_HALF_UP ；如果它为偶数，则作 ROUND_HALF_DOWN 。<br>
     *                     BigDecimal.ROUND_HALF_UP 若舍弃部分&gt;=.5，则作 ROUND_UP ；否则，作 ROUND_DOWN 。<br>
     *                     BigDecimal.ROUND_UNNECESSARY 该“伪舍入模式”实际是指明所要求的操作必须是精确的，，因此不需要舍入操作。<br>
     *                     BigDecimal.ROUND_UP 总是在非 0 舍弃小数(即截断)之前增加数字。<br>
     * @return double
     */
    public static double divideDouble(double first, double second, int scale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(first);
        BigDecimal b2 = new BigDecimal(second);
        if (roundingMode == -1) {
            roundingMode = BigDecimal.ROUND_HALF_EVEN;
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * 返回两个double的乘积 first*second。
     *
     * @param first  第一个double
     * @param second 第二个double
     * @return double
     */
    public static double multiplyDouble(double first, double second) {
        BigDecimal b1 = new BigDecimal(first);
        BigDecimal b2 = new BigDecimal(second);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 返回两个double的差值 first-second。
     *
     * @param first  第一个double
     * @param second 第二个double
     * @return double
     */
    public static double subtractDouble(double first, double second) {
        BigDecimal b1 = new BigDecimal(first);
        BigDecimal b2 = new BigDecimal(second);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 返回两个double的和值 first+second。
     *
     * @param first  第一个double
     * @param second 第二个double
     * @return double
     */
    public static double sumDouble(double first, double second) {
        BigDecimal b1 = new BigDecimal(first);
        BigDecimal b2 = new BigDecimal(second);
        return b1.add(b2).doubleValue();
    }

    /**
     * 格式化浮点小数。例如将11.123格式化为11.1。
     *
     * @param value    原double数字。
     * @param decimals 小数位数。
     * @return 格式化后的double，注意为硬格式化不存在四舍五入。
     */
    public static String formatFloatPoint(double value, int decimals) {
        String doubleStr = "" + value;
        int index = doubleStr.contains(".") ? doubleStr.indexOf(".") : doubleStr.indexOf(",");
        if (index == -1) {
            return doubleStr;
        }
        if (decimals == 0) {
            return doubleStr.substring(0, index);
        }
        int len = index + decimals + 1;
        if (len >= doubleStr.length()) {
            len = doubleStr.length();
        }
        double d = Double.parseDouble(doubleStr.substring(0, len));
        return String.valueOf(d);
    }

    /**
     * 格式化浮点小数。例如将11.123格式化为11.1。
     *
     * @param f      float数字
     * @param format 要格式化成的格式 such as #.00, #.#
     * @return 格式化后字符串
     */
    public static String formatFloatPoint(float f, String format) {
        return new DecimalFormat(format).format(f);
    }

    /**
     * 生成一个指定位数的随机数，并将其转换为字符串作为函数的返回值。
     *
     * @param numberLength 随机数的位数。
     * @return String 注意随机数可能以0开头。
     */
    public static String randomNumber(int numberLength) {
        // 记录生成的每一位随机数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberLength; i++) {
            // 每次生成一位,随机生成一个0-10之间的随机数,不含10。
            Double ranDouble = Math.floor(Math.random() * 10);
            sb.append(ranDouble.intValue());
        }
        return sb.toString();
    }

    /**
     * 生成一个在最大数和最小数之间的随机数。会出现最小数，但不会出现最大数。
     *
     * @param minNum 最小数
     * @param maxNum 最大数
     * @return int
     */
    public static int randomNumber(int minNum, int maxNum) {
        if (maxNum <= minNum) {
            throw new RuntimeException("maxNum必须大于minNum!");
        }
        //计算出来差值
        int subtract = maxNum - minNum;
        Double ranDouble = Math.floor(Math.random() * subtract);
        return ranDouble.intValue() + minNum;
    }

}
