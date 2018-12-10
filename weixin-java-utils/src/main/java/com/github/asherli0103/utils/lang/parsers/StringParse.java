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


package com.github.asherli0103.utils.lang.parsers;

/**
 * 类型转换器的接口.
 *
 * @param <V> 转换的泛型类型
 * @author Asherli
 * @version 1.0.00
 */
@SuppressWarnings(value = {"unused"})
public interface StringParse<V> {
    /**
     * 转换
     *
     * @param str 待转换的数据
     * @return 转换后的数据
     */
    V parse(String str);
}
