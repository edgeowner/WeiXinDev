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


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings(value = {"unused", "unchecked"})
public class CompareUtil {


    public static <T1, T2> int compare(T1 o1, T2 o2, boolean checkComparable) {
        if (o1 == o2) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;

        int compare = o1.getClass().getName().compareTo(o2.getClass().getName());
        if (compare != 0) return compare;
        if (checkComparable) {
            if (o1 instanceof Comparable) {
                return ((Comparable<T2>) o1).compareTo(o2);
            }
        }
        if (o1 instanceof Collection && o2 instanceof Collection) {
            return compareCollections((Collection) o1, (Collection) o2, checkComparable);
        }
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return compareCollections((Object[]) o1, (Object[]) o2, checkComparable);
        }
        if (o1 instanceof Map && o2 instanceof Map) {
            return compareCollections((Map) o1, (Map) o2, checkComparable);
        }
        compare = compareToStringNoHash(o1, o2);
        if (compare != 0) return compare;
        return compare;
    }

    public static int compare(Object o1, Object o2) {
        return compare(o1, o2, false);  // default false to avoid infinite recursion
    }

    public static <T> int compareCollections(Collection<T> coll1, Collection<T> coll2, boolean checkComparable) {
        if (coll1 == coll2) return 0;
        if (coll1 == null) return -1;
        if (coll2 == null) return 1;
        Iterator<T> i1 = coll1.iterator();
        Iterator<T> i2 = coll2.iterator();
        int compare;
        while (i1.hasNext() && i2.hasNext()) {
            T t1 = i1.next();
            T t2 = i2.next();
            compare = compare(t1, t2, checkComparable);
            if (compare != 0) return compare;
        }
        if (i1.hasNext()) return 1;
        if (i2.hasNext()) return -1;
        return 0;
    }

    public static <T> int compareCollections(T[] arr1, T[] arr2, boolean checkComparable) {
        if (arr1 == arr2) return 0;
        if (arr1 == null) return -1;
        if (arr2 == null) return 1;
        int i;
        int compare;
        for (i = 0; i < Math.min(arr1.length, arr2.length); ++i) {
            T t1 = arr1[i];
            T t2 = arr2[i];
            compare = compare(t1, t2, checkComparable);
            if (compare != 0) return compare;
        }
        if (i < arr1.length) return 1;
        if (i < arr2.length) return -1;
        return 0;
    }

    public static <K, V> int compareCollections(Map<K, V> m1, Map<K, V> m2, boolean checkComparable) {
        if (m1 == m2) return 0;
        if (m1 == null) return -1;
        if (m2 == null) return 1;
        return compareCollections(m1.entrySet(), m2.entrySet(), checkComparable);
    }

    public static int compareToStringNoHash(Object o1, Object o2) {
        assert o1 != null;
        assert o2 != null;
        int pos;
        String s1 = o1.toString();
        String s2 = o2.toString();
        if (Objects.equals(s1, s2)) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        boolean gotAmp = false;
        for (pos = 0; pos < Math.min(s1.length(), s2.length()); ++pos) {
            char c1 = s1.charAt(pos);
            char c2 = s2.charAt(pos);
            if (gotAmp) {
                if (Character.isDigit(c1) || Character.isDigit(c2)) {
                    System.err.println("Warning! Assumed comparing hash codes!");
                    return 0;
                } else {
                    gotAmp = false;
                }
            }
            if (c1 < c2) return -1;
            if (c1 > c2) return 1;
            if (c1 == '@') gotAmp = true;
        }
        if (pos < s1.length()) return 1;
        if (pos < s2.length()) return -1;
        return 0;
    }

    public static int compare(int i1, int i2) {
        if (i1 < i2) return -1;
        if (i1 > i2) return 1;
        return 0;
    }

    public static int compare(String s1, String s2) {
        if (Objects.equals(s1, s2)) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        return s1.compareTo(s2);
    }

}
