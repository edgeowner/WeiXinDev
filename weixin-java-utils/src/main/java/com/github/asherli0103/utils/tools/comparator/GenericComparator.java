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

package com.github.asherli0103.utils.tools.comparator;


import com.github.asherli0103.utils.CompareUtil;

import java.util.Comparator;

/**
 * 通用比较器实现类
 *
 * @author AsherLi
 * @version V0.0.01
 */
@SuppressWarnings(value = {"unchecked"})
public class GenericComparator<T> implements Comparator<T> {

    protected static GenericComparator<?> instance = new GenericComparator();

    public static <T> GenericComparator<T> instance() {
        return (GenericComparator<T>) instance;
    }

    @Override
    public int compare(T o1, T o2) {
        return CompareUtil.compare(o1, o2, true);
    }
}
