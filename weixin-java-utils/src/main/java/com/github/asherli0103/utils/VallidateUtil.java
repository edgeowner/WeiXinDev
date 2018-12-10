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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.asherli0103.utils.constant.RegularExpression.*;


/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class VallidateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(VallidateUtil.class);

    public static boolean isImage(String value) {
        return isMactchRegex(IMAGE, value);
    }

    public static boolean isHttp(String value) {
        return isMactchRegex(HTTP, value);
    }

    public static boolean isDate(String value) {
        return isMactchRegex(DATE, value);
    }

    /**
     * 判断是否位数字,并可为空
     *
     * @param src 数据
     * @return true/false
     */
    public static boolean isIntegersAndCanNull(String src) {
        return StringUtil.isEmpty(src) || src.length() > 0 && isIntegers(src);
    }


    public static boolean isFloatAndCanNull(String src) {
        return StringUtil.isEmpty(src) || src.length() > 0 && isRationalNumber(src);
    }

    public static boolean isPhone(String value) {
        return isMactchRegex(PHONE, value);
    }

    public static boolean isNonSpecialSymbols(String value) {
        return isMactchRegex(NON_SPECIAL_SYMBOLS, value);
    }

    public static boolean isNonNegativeIntegers(String value) {
        return isMactchRegex(NON_NEGATIVE_INTEGERS, value);
    }

    public static boolean isNonZeroNegativeIntegers(String value) {
        return isMactchRegex(NON_ZERO_NEGATIVE_INTEGERS, value);
    }

    /**
     * 验证该字符串是否是数字
     *
     * @param value 字符串内容
     * @return 是否是数字
     */
    public static boolean isIntegers(String value) {
        return isMactchRegex(INTEGERS, value);
    }

    public static boolean isNonPositiveIntegers(String value) {
        return isMactchRegex(NON_POSITIVE_INTEGERS, value);
    }

    public static boolean isWhitespace(String value) {
        return isMactchRegex(WHITESPACE, value);
    }

    public static boolean isDoubleByteCharacters(String value) {
        return isMactchRegex(DOUBLE_BYTE_CHARACTERS, value);
    }

    public static boolean isNonNegativeRationalNumbers(String value) {
        return isMactchRegex(NON_NEGATIVE_RATIONAL_NUMBERS, value);
    }

    public static boolean isNonPositiveRationalNumbers(String value) {
        return isMactchRegex(NON_POSITIVE_RATIONAL_NUMBERS, value);
    }

    public static boolean isAlphabet(String value) {
        return isMactchRegex(ALPHABET, value);
    }

    public static boolean isUpwardAlphabet(String value) {
        return isMactchRegex(UPWARD_ALPHABET, value);
    }

    public static boolean isLowerAlphabet(String value) {
        return isMactchRegex(LOWER_ALPHABET, value);
    }

    public static boolean isNumberAlphabet(String value) {
        return isMactchRegex(NUMBER_ALPHABET, value);
    }

    public static boolean isDatetime(String value) {
        return isMactchRegex(DATETIME, value);
    }

    /**
     * 验证是否为英文字母 、数字和下划线
     *
     * @param value 值
     * @return 是否为英文字母 、数字和下划线
     */
    public static boolean isNumberAlphabetUnderline(String value) {
        return isMactchRegex(NUMBER_ALPHABET_UNDERLINE, value);
    }

    /**
     * 验证是否为可用邮箱地址
     *
     * @param value 值
     * @return 否为可用邮箱地址
     */
    public static boolean isEmail(String value) {
        return isMactchRegex(EMAIL, value);
    }

    /**
     * 验证是否为给定长度范围的英文字母 、数字和下划线
     *
     * @param value 值
     * @param min   最小长度，负数自动识别为0
     * @param max   最大长度，0或负数表示不限制最大长度
     * @return 是否为给定长度范围的英文字母 、数字和下划线
     */
    public static boolean isLetterNumberUnderline(String value, int min, int max) {
        String reg = "^\\w{" + min + "," + max + "}$";
        if (min < 0) {
            min = 0;
        }
        if (max <= 0) {
            reg = "^\\w{" + min + ",}$";
        }
        return isMactchRegex(reg, value);
    }


    public static boolean isNumber(String value) {
        return isRationalNumber(value) || isMactchRegex(INTEGERS, value);
    }

    /**
     * 验证是否为邮政编码（中国）
     *
     * @param value 值
     * @return 是否为邮政编码（中国）
     */
    public static boolean isZipCode(String value) {
        return isMactchRegex(ZIP_CODE, value);
    }

    /**
     * 验证是否为浮点数
     *
     * @param value 值
     * @return 是否为浮点
     */
    public static boolean isRationalNumber(String value) {
        return isMactchRegex(RATIONAL_NUMBERS, value);
    }

    /**
     * 验证是否为手机号码（中国）
     *
     * @param value 值
     * @return 是否为手机号码（中国）
     */
    public static boolean isMobile(String value) {
        return isMactchRegex(MOBILE, value);
    }

    /**
     * 验证是否为生日<br>
     *
     * @param value 值
     * @return 是否为生日
     */
    public static boolean isBirthday(String value) {
        if (isMactchRegex(DATETIME, value)) {
            int year = Integer.parseInt(value.substring(0, 4));
            int month = Integer.parseInt(value.substring(5, 7));
            int day = Integer.parseInt(value.substring(8, 10));

            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
                return false;
            }
            if (month == 2) {
                boolean isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                if (day > 29 || (day == 29 && !isleap)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证是否为IPV4地址
     *
     * @param value 值
     * @return 是否为IPV4地址
     */
    public static boolean isIpv4(String value) {
        return isMactchRegex(IPV4, value);
    }

    /**
     * 验证是否为URL
     *
     * @param value 值
     * @return 是否为URL
     */
    public static boolean isUrl(String value) {
        try {
            new java.net.URL(value);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    public static boolean isURL(String value) {
        return isMactchRegex(URL, value);
    }

    /**
     * 验证是否为汉字
     *
     * @param value 值
     * @return 是否为汉字
     */
    public static boolean isChinese(String value) {
        return isMactchRegex(CHINESE, value);
    }

    /**
     * 验证是否为中文字、英文字母、数字和下划线
     *
     * @param value 值
     * @return 是否为中文字、英文字母、数字和下划线
     */
    public static boolean isGeneralWithChinese(String value) {
        return isMactchRegex(GENERAL_WITH_CHINESE, value);
    }

    /**
     * 验证是否为UUID<br>
     * 包括带横线标准格式和不带横线的简单模式
     *
     * @param value 值
     * @return 是否为UUID
     */
    public static boolean isUUID(String value) {
        return isMactchRegex(UUID, value) || isMactchRegex(UUID_SIMPLE, value);
    }

    /**
     * 判断指定的字符串是否符合某个正则表达式，大小写敏感
     *
     * @param content 字符串
     * @param regex   正则表达式
     * @return 符合返回true，否则返回false
     */
    public static boolean isMactchRegex(String regex, String content) {
        return isMatchString(content, regex, true);
    }

    /**
     * 判断指定的字符串是否符合某个正则表达式
     *
     * @param content       字符串
     * @param regex         正则表达式
     * @param caseSentivite 是否大小写敏感，true区分大小写，false不区分
     * @return 符合返回true，否则返回false
     */
    public static boolean isMatchString(String content, String regex, boolean caseSentivite) {
        boolean result = false;
        if (null == content || null == regex) {
            throw new NullPointerException("正则表达式或字符串不能为空");
        }

        Pattern pattern = null;
        if (!caseSentivite) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regex);
        }

        Matcher matcher = pattern.matcher(content);
        result = matcher.matches();

        return result;
    }


}
