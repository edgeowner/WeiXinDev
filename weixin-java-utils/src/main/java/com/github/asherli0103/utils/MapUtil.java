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

import java.util.*;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class MapUtil {

    /**
     * 将List中的Key转换为小写
     *
     * @param list listMap集合
     * @return 更改后的集合
     */
    public static List<Map<String, Object>> convertKeyList2LowerCase(List<Map<String, Object>> list) {
        if (null == list) {
            return null;
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Map<String, Object> result = convertKey2LowerCase(map);
            if (null != result) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    /**
     * 转换单个map,将key转换为小写.
     *
     * @param map 返回新对象
     * @return 更改后的Map
     */
    public static Map<String, Object> convertKey2LowerCase(Map<String, Object> map) {
        if (null == map) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            Object value = map.get(key);
            if (null == key) {
                continue;
            }
            String keyL = key.toLowerCase();
            result.put(keyL, value);
        }
        return result;
    }

    /**
     * 将List中Map将key前后去掉空白符
     *
     * @param list listMap集合
     * @return 更改后的集合
     */
    public static List<Map<String, Object>> trimListKeyValue(List<Map<String, Object>> list) {
        if (null == list) {
            return null;
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Map<String, Object> result = trimKeyValue(map);
            if (null != result) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    /**
     * 转换单个map,将key前后去掉空白符
     *
     * @param map 返回新对象
     * @return 更改后的Map
     */
    public static Map<String, Object> trimKeyValue(Map<String, Object> map) {
        if (null == map) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            Object value = map.get(key);
            if (null == key) {
                continue;
            }
            String keyT = key.trim();
            if (value instanceof String) {
                String valueT = String.valueOf(value).trim();
                result.put(keyT, valueT);
            } else {
                result.put(keyT, value);
            }
        }
        return result;
    }

}
