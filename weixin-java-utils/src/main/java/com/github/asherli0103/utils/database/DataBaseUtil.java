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

package com.github.asherli0103.utils.database;

import com.github.asherli0103.utils.ReflectionUtil;
import com.github.asherli0103.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class DataBaseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseUtil.class);

    public static String getTableName(Class<?> clazz) {
        return StringUtil.toLowerCaseAt(clazz.getSimpleName());
    }

    public static List<String> getTableFieldList(Class<?> clazz, String... excludeFields) {
        List<String> excludeFieldList = Arrays.asList(excludeFields);
        // 获取表的各个字段名称
        List<String> fieldList = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getModifiers() == Modifier.PUBLIC && method.getName().startsWith("get")) {
                // 提取getXxx中的Xxx部分
                String _Xxx = method.getName().substring(3);
                // 判断是否有对象setXxx方法
                String setXxx = "set" + _Xxx;
                Method m = null;
                try {
                    m = clazz.getMethod(setXxx, method.getReturnType());
                    if (m == null) {
                        continue;// 忽略后面的代码，继续下一次循环
                    }
                } catch (NoSuchMethodException e) {
                    LOGGER.error(e.getMessage());
                    continue;// 忽略后面的代码，继续下一次循环
                } catch (SecurityException e) {
                    LOGGER.error(e.getMessage());
                    continue;// 忽略后面的代码，继续下一次循环
                }
                String field = StringUtil.toLowerCaseAt(_Xxx);
                // 判断是否不是被排序的字段
                if (!excludeFieldList.contains(field)) {
                    fieldList.add(field);
                }
            }
        }
        return fieldList;
    }

    public static List<String> getJavaBeanFieldList(Class<?> clazz, String... excludeFields) {
        return getTableFieldList(clazz, excludeFields);
    }


    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean smallCamel) {
        if (StringUtil.isBlank(line)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }


    /**
     * 驼峰法转下划线
     *
     * @param line       源字符串
     * @param smallCamel 是否小写
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line, boolean smallCamel) {
        if (StringUtil.isBlank(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            if (smallCamel) {
                sb.append(word.toLowerCase());
            } else {
                sb.append(word.toUpperCase());
            }
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static <T> void setProperty(T entity, String fieldName, Object object) {
        Method m = ReflectionUtil.getJavaBeanSetMethod(entity.getClass(), fieldName);
        try {
            m.invoke(entity, object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符串处理，写SQL语句
     *
     * @param strSource sql
     * @return 转译后SQL
     */
    public static String toSqlString(String strSource) {
        String strResult;
        if (strSource == null) {
            strResult = "null";
        } else {
            strResult = strSource.replaceAll("'", "''");
            strResult = strSource.replaceAll("&lt;", "<");
            strResult = strSource.replaceAll("&gt;", ">");
            strResult = "'" + strResult + "'";
        }
        return strResult;
    }

    /*
 * 使用方法：主要为替换sql语句中的?. s:要替换的原sql splitchar:要替换的字符中，在此为"?"
 * parms[]:长度必需和?个数一样，
 */
    public static String replaceSqlByArray(String sql, String splitchar,
                                           Object parms[]) {
        String afterSplits[] = StringUtil.split(sql, splitchar);
        String retStr = "";
        if (parms.length == afterSplits.length - 1) {
            for (int i = 0; i < parms.length; i++) {
                retStr = retStr + afterSplits[i] + parms[i].toString();
            }
        } else {
            // logger.debug("sql中的?和参数数据中的个数不相符");
        }
        return retStr;
    }
}
