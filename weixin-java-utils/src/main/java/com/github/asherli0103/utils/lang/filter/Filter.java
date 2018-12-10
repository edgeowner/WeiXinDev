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

package com.github.asherli0103.utils.lang.filter;

/**
 * 过滤器接口<br>
 * 此过滤器两个作用：<br>
 * 1、使用返回值是否为<code>null</code>判定此对象被过滤与否<br>
 * 2、在过滤过程中，实现对对象的修改
 *
 * @param <T> 泛型参数
 * @author Asherli0103
 * @version 1.0.00
 */
public interface Filter<T> {
    /**
     * 修改过滤后的结果
     *
     * @param t 被过滤的对象
     * @return 修改后的对象，如果被过滤返回<code>null</code>
     */
    T modify(T t);
}
