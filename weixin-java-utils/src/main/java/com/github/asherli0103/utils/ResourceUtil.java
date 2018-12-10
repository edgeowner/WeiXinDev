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


import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;


/**
 * 项目参数工具类
 */
public class ResourceUtil {
    public static final String LOCAL_CLINET_USER = "LOCAL_CLINET_USER";

    private static final ResourceBundle bundle = ResourceBundle.getBundle("sysConfig");

    public final static boolean fuzzySearch = ResourceUtil.isFuzzySearch();


    /**
     * 获取session定义名称
     *
     * @param sessionName session
     * @return 值
     */
    public static String getSessionattachmenttitle(String sessionName) {
        return bundle.getString(sessionName);
    }

    /**
     * 获得请求路径
     *
     * @param request 请求
     * @return 请求路径
     */
    public static String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI() + "?" + request.getQueryString();
        if (requestPath.contains("&")) {// 去掉其他参数
            requestPath = requestPath.substring(0, requestPath.indexOf("&"));
        }
        requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
        return requestPath;
    }

    /**
     * 获取配置文件参数
     *
     * @param name 参数名
     * @return 参数值
     */
    public static String getConfigByName(String name) {
        return bundle.getString(name);
    }


    @SuppressWarnings(value = {"unchecked"})
    public static Map<Object, Object> getConfigMap(String path) {
        ResourceBundle bundle = ResourceBundle.getBundle(path);
        Set set = bundle.keySet();
        return CollectionUtil.toMap(set);
    }


    public static String getSysPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp = path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
        String separator = System.getProperty("file.separator");
        return temp.replaceAll("/", separator + separator).replaceAll("%20", " ");
    }

    /**
     * 获取项目根目录
     *
     * @return 项目绝对路径
     */
    public static String getPorjectPath() {
        String nowpath; // 当前tomcat的bin目录的路径 如
        // D:\java\software\apache-tomcat-6.0.14\bin
        String tempdir;
        nowpath = System.getProperty("user.dir");
        tempdir = nowpath.replace("bin", "webapps"); // 把bin 文件夹变到 webapps文件里面
        tempdir += "\\"; // 拼成D:\java\software\apache-tomcat-6.0.14\webapps\sz_pro
        return tempdir;
    }

    public static String getClassPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp = path.replaceFirst("file:/", "");
        String separator = System.getProperty("file.separator");
        return temp.replaceAll("/", separator + separator);
    }

    public static String getSystempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * 获取随机码的长度
     *
     * @return 随机码的长度
     */
    public static String getRandCodeLength() {
        return bundle.getString("randCodeLength");
    }

    /**
     * 获取随机码的类型
     *
     * @return 随机码的类型
     */
    public static String getRandCodeType() {
        return bundle.getString("randCodeType");
    }


    /**
     * 获取组织机构编码长度的类型
     *
     * @return 组织机构编码长度的类型
     */
    public static String getOrgCodeLengthType() {
        return bundle.getString("orgCodeLengthType");
    }

    public static boolean isFuzzySearch() {
        return "1".equals(bundle.getString("fuzzySearch"));
    }

}
