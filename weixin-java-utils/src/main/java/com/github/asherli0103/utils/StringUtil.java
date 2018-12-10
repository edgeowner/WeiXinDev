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

import com.github.asherli0103.utils.lang.parsers.StringParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 不可变空字符数组.
     */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * 空字符串
     */
    private static final String EMPTY = "";

    /**
     * 默认分隔符
     */
    private static final String DEFAULT_DELIMITERS = ",";

    /**
     * 索引未发现
     */
    private static final int INDEX_NOT_FOUND = -1;

    /**
     * 大写字符匹配正则
     */
    private static final String UPPER_CASE = "(?=[A-Z])";

    private StringUtil() {
        //私有化,禁止实例化
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - -   Empty/Null/WhiteSpace   - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 判断某字符串是否为空，为空的标准是 str==null 或 str.length()==0
     *
     * @param cs 待检测字符串
     * @return true/false
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断某字符串是否非空，等于 !isEmpty(String str)
     *
     * @param cs 待检测字符串
     * @return true/false
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 判断字符串是否是null或Empty或"null"
     *
     * @param s 字符串
     * @return true:是 false:否
     */
    public static boolean isNullOrEmpty(String s) {
        return (isEmpty(s) || s.trim().toLowerCase().equals("null"));
    }

    /**
     * 判断某字符串不为空,{@code null}或"null"
     *
     * @param s 待检测字符串
     * @return true/false
     */
    public static boolean isNotNullOrEmpty(String s) {
        return !isNullOrEmpty(s);
    }

    /**
     * 　判断某字符串是否为空或长度为0或由空白符(whitespace) 构成
     *
     * @param cs 待检测字符串
     * @return true/false
     */
    public static boolean isBlank(final CharSequence cs) {
        if (isEmpty(cs)) {
            return true;
        }
        int strLen = cs.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断某字符串是否不为空且长度不为0且不由空白符(whitespace) 构成，等于 !isBlank(String str)
     *
     * @param cs 待检测字符串
     * @return true/false
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 判断多个字符串中是否包含null
     *
     * @param css 字符串数组
     * @return true/false
     */
    public static boolean hasBlank(final CharSequence... css) {
        if (ArrayUtil.isEmpty(css)) {
            return true;
        }

        for (CharSequence cs : css) {
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当给定字符串为null时，转换为Empty
     *
     * @param cs 原始字符串
     * @return null转换为"",否则返回原始数据
     */
    public static String nullToEmpty(final CharSequence cs) {
        return nullToDefault(cs, EMPTY);
    }

    /**
     * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
     *
     * @param cs         原始字符串
     * @param defaultStr 默认字符串
     * @return null转换为defaultStr, 否则返回原始数据
     */
    public static String nullToDefault(final CharSequence cs, String defaultStr) {
        return (cs == null) ? defaultStr : cs.toString();
    }

    /**
     * 取得字符串的实际长度（考虑了汉字的情况）
     *
     * @param SrcStr 源字符串
     * @return 字符串的实际长度
     */
    public static int getLength(String SrcStr) {
        int return_value = 0;
        if (SrcStr != null) {
            char[] theChars = SrcStr.toCharArray();
            for (char theChar : theChars) {
                return_value += (theChar <= 255) ? 1 : 2;
            }
        }
        return return_value;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - -       Repeat    - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 判断文字内容重复
     *
     * @param content 内容
     * @return true/false
     */
    public static boolean isRepeatChinese(String content) {
        int similarNum = 0;
        int forNum = 0;
        int subNum = 0;
        int thousandNum = 0;
        String startStr = "";
        String nextStr = "";
        boolean result = false;
        float endNum = (float) 0.0;
        if (content != null && content.length() > 0) {
            if (content.length() % 1000 > 0)
                thousandNum = (int) Math.floor(content.length() / 1000) + 1;
            else
                thousandNum = (int) Math.floor(content.length() / 1000);
            if (thousandNum < 3)
                subNum = 100 * thousandNum;
            else if (thousandNum < 6)
                subNum = 200 * thousandNum;
            else if (thousandNum < 9)
                subNum = 300 * thousandNum;
            else
                subNum = 3000;
            for (int j = 1; j < subNum; j++) {
                if (content.length() % j > 0)
                    forNum = (int) Math.floor(content.length() / j) + 1;
                else
                    forNum = (int) Math.floor(content.length() / j);
                if (result || j >= content.length())
                    break;
                else {
                    for (int m = 0; m < forNum; m++) {
                        if (m * j > content.length() || (m + 1) * j > content.length() || (m + 2) * j > content.length())
                            break;
                        startStr = content.substring(m * j, (m + 1) * j);
                        nextStr = content.substring((m + 1) * j, (m + 2) * j);
                        if (startStr.equals(nextStr)) {
                            similarNum = similarNum + 1;
                            endNum = (float) similarNum / forNum;
                            if (endNum > 0.4) {
                                result = true;
                                break;
                            }
                        } else
                            similarNum = 0;
                    }
                }
            }
        }
        return result;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - -        Join     - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 以 conjunction 为分隔符将集合转换为字符串
     *
     * @param collection 集合
     * @param symbol     分隔符
     * @param <T>        被处理的集合
     * @return 连接后的字符
     */
    public static <T> String join(Collection<T> collection, String symbol) {
        return join(collection.toArray(), symbol);
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param <T>    被处理的集合
     * @param array  数组
     * @param symbol 分隔符
     * @return 连接后的字符串
     */
    public static <T> String join(T[] array, String symbol) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (T item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(symbol);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串
     *
     * @param <T>        被处理的集合
     * @param collection 集合
     * @param symbol     分隔符
     * @return 连接后的字符串
     */
    public static <T> String join(Iterable<T> collection, String symbol) {
        return join(Collections.singletonList(collection), symbol);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - -  ToString   - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 将byte数组转为字符串
     *
     * @param bytes       byte数组
     * @param charsetName 编码名称
     * @return 字符串
     */
    public static String toString(final byte[] bytes, final String charsetName) {
        return toString(bytes, isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName));
    }

    /**
     * 将byte数组转为字符串
     *
     * @param bytes   byte数组
     * @param charset 编码格式
     * @return 字符串
     */
    public static String toString(final byte[] bytes, final Charset charset) {
        if (bytes == null) {
            return null;
        }
        if (null == charset) {
            return new String(bytes, Charset.defaultCharset());
        }
        return new String(bytes, charset);
    }

    /**
     * list 集合转换字符串,判断使用"[]"还是"()"进行包装
     *
     * @param arr    list集合
     * @param square 判断使用"[]"还是"()"进行包装
     * @param <T>    对象泛型
     * @return 字符串
     */
    public static <T> String toString(List<T> arr, boolean square) {
        return toString(arr.toArray(), square);
    }

    /**
     * 对象数组转换字符串,判断使用"[]"还是"()"进行包装
     *
     * @param <T>    对象泛型
     * @param arr    对象数组
     * @param square 判断使用"[]"还是"()"进行包装
     * @return 字符串
     */
    public static <T> String toString(T[] arr, boolean square) {
        if (arr == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        if (square) {
            sb.append("[");
        } else {
            sb.append("(");
        }
        for (int i = 0; i < arr.length; ++i) {
            if (i > 0) sb.append(",");
            if (arr[i] == null) {
                sb.append("null");
            } else {
                sb.append(arr[i].toString());
            }
        }
        if (square) {
            sb.append("]");
        } else {
            sb.append(")");
        }

        return sb.toString();
    }

    /**
     * 数字转字符串,如果num&lt;=0 则输出"";
     *
     * @param num 数字
     * @return 字符串格式数字
     */
    public static String toString(Number num) {
        if (num == null) {
            return null;
        } else if (num instanceof Integer && (Integer) num > 0) {
            return Integer.toString((Integer) num);
        } else if (num instanceof Long && (Long) num > 0) {
            return Long.toString((Long) num);
        } else if (num instanceof Float && (Float) num > 0) {
            return Float.toString((Float) num);
        } else if (num instanceof Double && (Double) num > 0) {
            return Double.toString((Double) num);
        } else {
            return EMPTY;
        }
    }

    /**
     * 货币转字符串
     *
     * @param money 数字Double或Float类型
     * @param style 样式 [default]要格式化成的格式 such as #.00, #.#
     * @return 货币字符串
     */
    public static String toString(Number money, String style) {
        if (money != null && style != null) {
            if (money instanceof Double || money instanceof Float) {
                Double num = Double.parseDouble(String.valueOf(money));//避免出现精度问题
                if (style.equalsIgnoreCase("default")) {
                    // 缺省样式 0 不输出 ,如果没有输出小数位则不输出.0
                    if (num == 0) {
                        return "";
                    } else if ((num * 10 % 10) == 0) {
                        return Integer.toString(num.intValue());
                    } else {
                        return num.toString();
                    }
                } else {
                    DecimalFormat df = new DecimalFormat(style);
                    return df.format(num);
                }
            }
        }
        return EMPTY;
    }

    /**
     * 泛型方法(通用)，把list转换成以“,”相隔的字符串 调用时注意类型初始化&lt;申明类型&gt; 如：List&lt;Integer&gt; intList = new ArrayList&lt;Integer&gt;(); 调用方法：StringUtil.listTtoString(intList); 效率：list中4条信息，1000000次调用时间为850ms左右
     *
     * @param <T>     泛型
     * @param list    list列表
     * @param slipStr 分隔符
     * @return 以“,”相隔的字符串
     */
    public static <T> String toString(List<T> list, String slipStr) {
        if (null == list || list.isEmpty()) {
            return "";
        }
        if (isBlank(slipStr)) {
            slipStr = ",";
        }
        Iterator<T> i = list.iterator();
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            T e = i.next();
            sb.append(e);
            if (!i.hasNext())
                return sb.toString();
            sb.append(slipStr);
        }
    }

    /**
     * 把数组转换成以“,”相隔的字符串
     *
     * @param array 数组a
     * @param <T>   泛型
     * @param token 分隔符
     * @return 以“,”相隔的字符串
     */
    public static <T> String toString(T[] array, String token) {
        if (array != null) {
            String separator = token == null ? "," : token;
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    buffer.append(separator);
                }
                buffer.append(array[i]);
            }
            return buffer.toString();
        }
        return null;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -    Change   - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 将全角的符号转换成半角符号.(即中文字符转英文字符)
     *
     * @param str 源字符串
     * @return String
     */
    public static String changeToHalf(String str) {
        String[] decode = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "!", "@", "#", "$", "%", "^", "&", "*",
                "(", ")", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
                "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "-", "_", "=", "+", "\\", "|", "[", "]", ";", ":", "'", "\"", ",", "<", ".", ">", "/", "?"
        };
        String source = "１２３４５６７８９０！＠＃＄％︿＆＊（）ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ" +
                "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ－＿＝＋＼｜【】；：'\\，〈。〉／？";
        return change(str, source, decode);
    }

    /**
     * 将半角的符号转换成全角符号.(即英文字符转中文字符)
     *
     * @param str 源字符串
     * @return String
     */
    public static String changeToFull(String str) {
        String source = "1234567890!@#$%^&*()abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_=+\\|[];:'\",<.>/?";
        String[] decode = {"１", "２", "３", "４", "５", "６", "７", "８", "９", "０", "！", "＠", "＃", "＄", "％",
                "︿", "＆", "＊", "（", "）", "ａ", "ｂ", "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ",
                "ｋ", "ｌ", "ｍ", "ｎ", "ｏ", "ｐ", "ｑ", "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ",
                "ｚ", "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ", "Ｍ", "Ｎ",
                "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ", "Ｙ", "Ｚ", "－", "＿", "＝",
                "＋", "＼", "｜", "【", "】", "；", "：", "'", "\"", "，", "〈", "。", "〉", "／", "？"
        };
        return change(str, source, decode);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -    Format   - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... values) {
        if (null == values || values.length == 0 || isBlank(template)) {
            return template;
        }

        final StringBuilder sb = new StringBuilder();
        final int length = template.length();

        int valueIndex = 0;
        char currentChar;
        for (int i = 0; i < length; i++) {
            if (valueIndex >= values.length) {
                sb.append(template.substring(i, length));
                break;
            }

            currentChar = template.charAt(i);
            if (currentChar == '{') {
                final char nextChar = template.charAt(++i);
                if (nextChar == '}') {
                    sb.append(values[valueIndex++]);
                } else {
                    sb.append('{').append(nextChar);
                }
            } else {
                sb.append(currentChar);
            }

        }

        return sb.toString();
    }

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param map      参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Map<?, ?> map) {
        if (null == map || map.isEmpty()) {
            return template;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return template;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -   SubString   - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 得到字符串的子串位置序列
     *
     * @param str 字符串
     * @param sub 子串
     * @param b   true子串前端,false子串后端
     * @return 字符串的子串位置序列
     */
    public static int[] substringPos(String str, String sub, boolean b) {
        String[] sp = null;
        int l = sub.length();
        sp = split(str, sub);
        if (sp == null) {
            return null;
        }
        int[] ip = new int[sp.length - 1];
        for (int i = 0; i < sp.length - 1; i++) {
            ip[i] = sp[i].length() + l;
            if (i != 0) {
                ip[i] += ip[i - 1];
            }
        }
        if (b) {
            for (int j = 0; j < ip.length; j++) {
                ip[j] = ip[j] - l;
            }
        }
        return ip;
    }

    /**
     * 截取字符串
     *
     * @param s   源字符串
     * @param jmp 跳过jmp
     * @param sb  取在sb
     * @param se  于se
     * @return 之间的字符串
     */
    public static String substring(String s, String jmp, String sb, String se) {
        if (isEmpty(s)) {
            return "";
        }
        int i = s.indexOf(jmp);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        i = s.indexOf(sb);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        if (Objects.equals(se, "")) {
            return s;
        } else {
            i = s.indexOf(se);
            if (i >= 0 && i < s.length()) {
                s = s.substring(i + 1);
            }
            return s;
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -   Convert   - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 字符串转换Object数组 通过StringParse接口实现数据类型转换
     *
     * @param str       要转换的字符串 例:["1,2,33,4,22"]
     * @param character 字符串分隔符 ","
     * @param parse     实现转换接口 StringParse
     * @param <T>       数据类型
     * @return 转换后的List集合
     */
    public static <T> List<T> convert(String str, String character, StringParse<T> parse) {
        if (isEmpty(str) || isEmpty(character) || parse == null) {
            return Collections.emptyList();
        }

        String[] temps = split(str, character);
        List<T> result = new ArrayList<>();
        for (String t : temps) {
            result.add(parse.parse(t));
        }

        return result;
    }

    /**
     * 字符串转换Object数组 通过StringParse接口实现数据类型转换,
     * 注:只转换","分割的字符串
     *
     * @param str   要转换的字符串 例:["1,2,33,4,22"]
     * @param parse 实现转换接口 StringParse
     * @param <T>   数据类型
     * @return 转换后的List集合
     */
    public static <T> List<T> convert(String str, StringParse<T> parse) {
        return convert(str, DEFAULT_DELIMITERS, parse);
    }


    /**
     * 对目标串中指定索引位的字符进行小写转换，返回转换后的新串
     * 如果不给参数:indexs，则将首字符转成小写
     *
     * @param target 目标字符串
     * @param indexs 索引参数
     * @return 修改后字符串
     */
    public static String toLowerCaseAt(String target, int... indexs) {
        if (isEmpty(target)) {
            return EMPTY;
        }
        char[] chs = target.toCharArray();
        if (indexs.length == 0) {
            //将第一个字母转成小写
            chs[0] = Character.toLowerCase(chs[0]);
        } else {
            for (int index : indexs) {
                if (index > -1 && index < chs.length) {
                    chs[index] = Character.toLowerCase(chs[index]);
                }
            }
        }
        return new String(chs);
    }

    /**
     * 对目标串中指定索引位的字符进行大写转换，返回转换后的新串
     * 如果不给参数:indexs，则将首字符转成大写
     *
     * @param target 目标字符串
     * @param indexs 索引参数
     * @return 修改后字符串
     */
    public static String toUpperCaseAt(String target, int... indexs) {
        if (isEmpty(target)) {
            return EMPTY;
        }
        char[] chs = target.toCharArray();
        if (indexs.length == 0) {
            //将第一个字母转成小写
            chs[0] = Character.toUpperCase(chs[0]);
        } else {
            for (int index : indexs) {
                if (index > -1 && index < chs.length) {
                    chs[index] = Character.toUpperCase(chs[index]);
                }
            }
        }
        return new String(chs);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - -  - - - -- - - -   Split   - - - - - - - - - - - - - -
    //- - - -  -- - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 按照指定分隔符将字符串分割为字符串数组
     *
     * @param str            字符串
     * @param separatorChars 分隔符
     * @return 字符数组, 当原始字符串为null时返回null
     */
    public static String[] split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * 根据大写字母分割字符串
     *
     * @param fields 字符串
     * @return 分割后的String[]
     */
    public static String[] splitByUpperCase(String fields) {
        return fields.split(UPPER_CASE);
    }

    /**
     * 根据正则表达式分割字符串
     *
     * @param str 源字符串
     * @param ms  正则表达式
     * @return 目标字符串组
     */
    public static String[] splitByRegex(String str, String ms) {
        Pattern p = Pattern.compile(ms, Pattern.CASE_INSENSITIVE);
        return p.split(str);
    }

    /**
     * 分割去重
     * 根据正则表达式提取字符串,相同的字符串只返回一个
     *
     * @param str     源字符串
     * @param pattern 正则表达式
     * @return 目标字符串数据组
     */
    public static String[] splitHeavy(String str, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(str);
        // 范型
        Set<String> result = new HashSet<String>();// 目的是：相同的字符串只返回一个。。。 不重复元素
        // boolean find() 尝试在目标字符串里查找下一个匹配子串。
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) { // int groupCount()
                // 返回当前查找所获得的匹配组的数量。
                result.add(matcher.group(i));
            }
        }
        String[] resultStr = null;
        if (result.size() > 0) {
            resultStr = new String[result.size()];
            return result.toArray(resultStr);// 将Set result转化为String[] resultStr
        }
        return null;

    }

    /**
     * 得到第一个b,e之间的字符串,并返回e后的子串
     *
     * @param s 源字符串
     * @param b 标志开始
     * @param e 标志结束
     * @return b, e之间的字符串
     */
    public static String[] splitString(String s, String b, String e) {
        int i = s.indexOf(b) + b.length();
        int j = s.indexOf(e, i);
        String[] sa = new String[2];
        if (i < b.length() || j < i + 1 || i > j) {
            sa[1] = s;
            sa[0] = null;
            return sa;
        } else {
            sa[0] = s.substring(i, j);
            sa[1] = s.substring(j);
            return sa;
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - - -  Private  - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 全椒半角字符替换
     *
     * @param str    将要替换的字符串
     * @param source 将要替换的字符表
     * @param decode 替换字符数组
     * @return 转换后的字符串
     */
    private static String change(String str, String source, String[] decode) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int pos = source.indexOf(str.charAt(i));
            if (pos != -1) {
                result += decode[pos];
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }

    /**
     * 用于拆分字符串,该方法移至自 Commons-lang3
     *
     * @param str               字符串
     * @param separatorChars    分隔符
     * @param max               输出的字符串数组的最大长度
     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token separators; if {@code
     *                          false}, adjacent separators are treated as one separator.
     * @return 字符串数组, 如果原始字符串为null则返回null
     */
    private static String[] splitWorker(final String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }


}
