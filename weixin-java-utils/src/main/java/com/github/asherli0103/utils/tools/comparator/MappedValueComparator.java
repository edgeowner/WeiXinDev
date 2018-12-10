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

import java.util.Comparator;
import java.util.Map;

/**
 * Map比较器实现类
 *
 * @author AsherLi
 * @version V0.0.01
 */
@SuppressWarnings(value = {"unused"})
public class MappedValueComparator<K, V> implements Comparator<K> {

    protected Map<K, V> map = null;
    protected Comparator<V> valueComparator = null;

    public MappedValueComparator(Map<K, V> map, Comparator<V> valueComparator) {
        this.map = map;
        this.valueComparator = valueComparator;
    }

    @Override
    public int compare(K A, K B) {
        if (A == B) return 0;
        if (A == null) return -1;
        if (B == null) return 1;
        if (map == null) return GenericComparator.instance().compare(A, B);
        V resultA = map.get(A);
        V resultB = map.get(B);
        return getValueComparator().compare(resultA, resultB);
    }

    protected Comparator<V> getValueComparator() {
        if (valueComparator == null) {
            valueComparator = GenericComparator.instance();
        }
        return valueComparator;
    }

}