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

package com.github.asherli0103.utils.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 基本变量类型的枚举
 * 基本类型枚举包括原始类型和包装类型
 *
 * @author Asherli0103
 * @version 1.0.00
 */
@SuppressWarnings(value = {"unused"})
public enum BasicType {
    BYTE, SHORT, INT, INTEGER, LONG, DOUBLE, FLOAT, BOOLEAN, CHAR, CHARACTER, STRING;

    /**
     * 原始类型为Key，包装类型为Value，例如： int.class → Integer.class.
     */
    public static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<>(8);
    /**
     * 包装类型为Key，原始类型为Value，例如： Integer.class → int.class.
     */
    public static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<>(8);

    static {
        wrapperPrimitiveMap.put(Boolean.class, boolean.class);
        wrapperPrimitiveMap.put(Byte.class, byte.class);
        wrapperPrimitiveMap.put(Character.class, char.class);
        wrapperPrimitiveMap.put(Double.class, double.class);
        wrapperPrimitiveMap.put(Float.class, float.class);
        wrapperPrimitiveMap.put(Integer.class, int.class);
        wrapperPrimitiveMap.put(Long.class, long.class);
        wrapperPrimitiveMap.put(Short.class, short.class);

        for (Map.Entry<Class<?>, Class<?>> entry : wrapperPrimitiveMap.entrySet()) {
            primitiveWrapperMap.put(entry.getValue(), entry.getKey());
        }
    }
}
