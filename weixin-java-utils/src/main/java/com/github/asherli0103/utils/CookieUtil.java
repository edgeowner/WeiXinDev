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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Java Cookie操作
 * 中文值使用 URLEncoder.encode("中文", "UTF-8")进行编码
 * 取出中文 URLDecoder.decode(cookies[i].getValue(), "UTF-8")
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, String path, int maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        if (StringUtil.isNotBlank(path)) {
            cookie.setPath(path);
        }
        if (StringUtil.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String name, String path, String domain) {
        Cookie cookie = new Cookie(name, "");
        if (StringUtil.isNotBlank(path)) {
            cookie.setPath(path);
        }
        if (StringUtil.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void updateCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, String path, int maxAge, String domain) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), name)) {
                    if (StringUtil.isNotBlank(path)) {
                        cookie.setPath(path);
                    }
                    if (StringUtil.isNotBlank(domain)) {
                        cookie.setDomain(domain);
                    }
                    cookie.setValue(value);
                    if (maxAge > 0) {
                        cookie.setMaxAge(maxAge);
                    }
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name);
        } else {
            return null;
        }
    }

    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

}
