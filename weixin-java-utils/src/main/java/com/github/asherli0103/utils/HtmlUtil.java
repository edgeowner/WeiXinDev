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

import com.github.asherli0103.utils.enums.HtmlSpecialCharTableEnum;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class HtmlUtil {


    /**
     * 过滤用户输入的URL地址（防治用户广告） 目前只针对以http或www开头的URL地址 本方法调用的正则表达式，不建议用在对性能严格的地方例如:循环及list页面等
     *
     * @param str 需要处理的字符串
     * @return 返回处理后的字符串
     */
    public static String removeURL(String str) {
        if (str != null) {
            str = str.toLowerCase().replaceAll("(http|www|com|cn|org|\\.)+", "");
        }
        return str;
    }

    /**
     * 用于将字符串中的特殊字符转换成Web页中可以安全显示的字符串.
     * 可对表单数据据进行处理对一些页面特殊字符进行处理如'&lt;','&gt;','"','''
     *
     * @param cs 要进行替换操作的字符串
     * @return 替换特殊字符后的字符串
     */
    public static String htmlEncode(CharSequence cs) {
        return htmlEncode(cs, 0);
    }

    /**
     * 用于将字符串中的特殊字符转换成Web页中可以安全显示的字符串.
     * 可对表单数据据进行处理对一些页面特殊字符进行处理如'&lt;','&gt;','"','''
     *
     * @param cs     要进行替换操作的字符串
     * @param quotes 为0时单引号和双引号都替换，为1时不替换单引号，为2时不替换双引号，为3时单引号和双引号都不替换
     * @return 替换特殊字符后的字符串
     */
    public static String htmlEncode(CharSequence cs, int quotes) {
        if (cs == null) {
            return "";
        }
        if (quotes == 0) {
            return htmlEncode(cs);
        }

        char[] arr_cSrc = cs.toString().toCharArray();
        StringBuilder buf = new StringBuilder(arr_cSrc.length);
        char ch;

        for (char anArr_cSrc : arr_cSrc) {
            ch = anArr_cSrc;
            boolean flag = true;
            HtmlSpecialCharTableEnum[] charTableEna = HtmlSpecialCharTableEnum.values();
            for (HtmlSpecialCharTableEnum charEnum : charTableEna) {
                char chr = charEnum.getCharValue().charAt(0);
                String special = charEnum.getStrValue().toString();
                if (ch == chr && chr == '"' && quotes == 1) {
                    buf.append(special);
                    flag = false;
                } else if (ch == chr && chr == '\'' && quotes == 2) {
                    buf.append(special);
                    flag = false;
                } else if (ch == chr && !(chr == '\'') && !(chr == '"')) {
                    buf.append(special);
                    flag = false;
                }
            }
            if (flag) {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    /**
     * 和htmlEncode正好相反
     *
     * @param cs 要进行转换的字符串
     * @return 转换后的字符串
     */
    public static String htmlDecode(CharSequence cs) {
        if (cs == null) {
            return "";
        }
        HtmlSpecialCharTableEnum[] charTableEna = HtmlSpecialCharTableEnum.values();
        for (HtmlSpecialCharTableEnum charEnum : charTableEna) {
            cs = cs.toString().replace(charEnum.getStrValue(), charEnum.getCharValue());
        }
        return cs.toString();
    }

    /**
     * 将html的省略写法替换成非省略写法
     *
     * @param str html字符串
     * @param pt  标签如table
     * @return 结果串
     */
    public static String formatToFullTag(String str, String pt) {
        String regEx = "<" + pt + "\\s+([\\S&&[^<>]]*)/>";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        String[] sa = null;
        String sf = "";
        String sf2 = "";
        String sf3 = "";
        for (; m.find(); ) {
            sa = p.split(str);
            if (sa == null) {
                break;
            }
            sf = str.substring(sa[0].length(), str.indexOf("/>", sa[0].length()));
            sf2 = sf + "></" + pt + ">";
            sf3 = str.substring(sa[0].length() + sf.length() + 2);
            str = sa[0] + sf2 + sf3;
            sa = null;
        }
        return str;
    }

    /**
     * 解析前台encodeURIComponent编码后的参数
     *
     * @param property (encodeURIComponent(no))
     * @return exception
     */
    public static String getEncodeParams(String property) {
        String trem = "";
        if (StringUtil.isNotEmpty(property)) {
            try {
                trem = URLDecoder.decode(property, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return trem;
    }

    /**
     * 截取字符串
     *
     * @param str  原始字符串
     * @param len  要截取的长度
     * @param tail 结束加上的后缀
     * @return 截取后的字符串
     */
    public static String htmlSubString(String str, int len, String tail) {
        if (str == null || str.length() <= len) {
            return str;
        }
        int length = str.length();
        char c = ' ';
        String tag = null;
        String name = null;
        int size = 0;
        String result = "";
        boolean isTag = false;
        List<String> tags = new ArrayList<>();
        int i = 0;
        for (int end = 0, spanEnd = 0; i < length && len > 0; i++) {
            c = str.charAt(i);
            if (c == '<') {
                end = str.indexOf('>', i);
            }

            if (end > 0) {
                // 截取标签
                tag = str.substring(i, end + 1);
                int n = tag.length();
                if (tag.endsWith("/>")) {
                    isTag = true;
                } else if (tag.startsWith("</")) { // 结束符
                    name = tag.substring(2, end - i);
                    size = tags.size() - 1;
                    // 堆栈取出html开始标签
                    if (size >= 0 && name.equals(tags.get(size))) {
                        isTag = true;
                        tags.remove(size);
                    }
                } else { // 开始符
                    spanEnd = tag.indexOf(' ', 0);
                    spanEnd = spanEnd > 0 ? spanEnd : n;
                    name = tag.substring(1, spanEnd);
                    if (name.trim().length() > 0) {
                        // 如果有结束符则为html标签
                        spanEnd = str.indexOf("</" + name + ">", end);
                        if (spanEnd > 0) {
                            isTag = true;
                            tags.add(name);
                        }
                    }
                }
                // 非html标签字符
                if (!isTag) {
                    if (n >= len) {
                        result += tag.substring(0, len);
                        break;
                    } else {
                        len -= n;
                    }
                }

                result += tag;
                isTag = false;
                i = end;
                end = 0;
            } else { // 非html标签字符
                len--;
                result += c;
            }
        }
        // 添加未结束的html标签
        for (String endTag : tags) {
            result += "</" + endTag + ">";
        }
        if (i < length) {
            result += tail;
        }
        return result;
    }

    /**
     * html 必须是格式良好的
     *
     * @param str exception
     * @return exception
     * @throws Exception exception
     */
    public static String formatHtml(String str) throws Exception {
        Document document = null;
        document = DocumentHelper.parseText(str);

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        StringWriter writer = new StringWriter();

        HTMLWriter htmlWriter = new HTMLWriter(writer, format);

        htmlWriter.write(document);
        htmlWriter.close();
        return writer.toString();
    }
}
