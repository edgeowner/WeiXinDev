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

/**
 * Escape编码工具
 *
 * @author AsherLi
 * @version 1.0.00
 */
@SuppressWarnings(value = {"unused"})
public class EscapeUtil {


    /**
     * Escape编码（Unicode）
     *
     * @param content dd
     * @return 编码后的字符串
     */
    public static String escape(String content) {
        if (StringUtil.isBlank(content)) {
            return content;
        }

        int i;
        char j;
        StringBuilder tmp = new StringBuilder();
        tmp.ensureCapacity(content.length() * 6);

        for (i = 0; i < content.length(); i++) {

            j = content.charAt(i);

            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16) tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * Escape解码
     *
     * @param content dd
     * @return 解码后的字符串
     */
    public static String unescape(String content) {
        if (StringUtil.isBlank(content)) {
            return content;
        }

        StringBuilder tmp = new StringBuilder(content.length());
        int lastPos = 0, pos;
        char ch;
        while (lastPos < content.length()) {
            pos = content.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (content.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(content.substring(lastPos));
                    lastPos = content.length();
                } else {
                    tmp.append(content.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * 安全的unescape文本，当文本不是被escape的时候，返回原文。
     *
     * @param content 内容
     * @return 解码后的字符串，如果解码失败返回原字符串
     */
    public static String safeUnescape(String content) {
        try {
            return unescape(content);
        } catch (Exception ignored) {
        }
        return content;
    }
}
