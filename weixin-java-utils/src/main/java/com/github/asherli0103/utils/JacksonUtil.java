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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Jackson2 通用工具类
 *
 * @author AsherLi
 * @version V1.0.00
 */
public class JacksonUtil {

    public static final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    static {
        objectMapper = new ObjectMapper();
        //去掉默认的时间戳格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //设置为中国上海时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        //空值不序列化
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //反序列化时，属性不存在的兼容处理
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //序列化时，日期的统一格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE,true);

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //属性排序
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        //单引号处理
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List,如List&lt;Student&gt;,将第二个参数传递为Student
     * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     *
     * @param <T>   exception
     * @param json  exception
     * @param clazz exception
     * @return exception
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * json数组转List
     *
     * @param <T>           exception
     * @param json          exception
     * @param typeReference exception
     * @return exception
     */
    public static <T> T parseArray(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        List<Map<String, Object>> list = null;
        try {
            list = objectMapper.readValue(json,
                    new TypeReference<List<T>>() {
                    });
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        List<T> result = new ArrayList<>();
        if (list != null) {
            for (Map<String, Object> map : list) {
                result.add(mapToObject(map, clazz));
            }
        }
        return result;
    }


    @SuppressWarnings(value = {"unchecked"})
    public static <T> Map<String, T> parseMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> Map<String, T> parseMap(String json, Class<T> clazz) {
        Map<String, Map<String, Object>> map = null;
        try {
            map = objectMapper.readValue(json, new TypeReference<T>() {
            });
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        Map<String, T> result = new HashMap<>();
        if (map != null) {
            for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), mapToObject(entry.getValue(), clazz));
            }
        }
        return result;
    }

    /**
     * 把JavaBean转换为json字符串
     *
     * @param <T>    exception
     * @param object exception
     * @return exception
     */
    public static <T> String toJSONString(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "{}";
    }

    public static <T> T mapToObject(Map map, Class<T> clazz) {
        try {
            return objectMapper.convertValue(map, clazz);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void toJSONString(OutputStream out, Object value) {
        try {

            objectMapper.writeValue(out, value);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }


}
