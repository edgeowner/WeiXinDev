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


import com.github.asherli0103.utils.exception.UtilException;
import com.github.asherli0103.utils.lang.filter.Filter;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class CollectionUtil {


    /**
     * 空集合常量
     */
    private static final List<?> EMPTY_LIST = Collections.EMPTY_LIST;

    /**
     * 空ArrayList集合常量
     */
    private static final ArrayList<?> EMPTY_ARRAYLIST = new ArrayList(0);

    /**
     * 空Map常量
     */
    private static final Map<?, ?> EMPTY_MAP = Collections.EMPTY_MAP;

    /**
     * 空Set常量
     */
    private static final Set<?> EMPTY_SET = Collections.EMPTY_SET;

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -        Create       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 泛型方式创建空List,该方法不允许修稿数据
     *
     * @param <T> 泛型
     * @return 指定类型空List
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> List<T> emptyList() {
        return (List<T>) EMPTY_LIST;
    }

    /**
     * 泛型方式创建空ArrayList,该方法不允许修稿数据
     *
     * @param <T> 泛型
     * @return 指定类型空ArrayList
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> ArrayList<T> emptyArrayList() {
        return (ArrayList<T>) EMPTY_ARRAYLIST;
    }

    /**
     * 泛型方式创建空Set,该方法不允许修稿数据
     *
     * @param <T> 泛型
     * @return 指定类型空Set
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> Set<T> emptySet() {
        return (Set<T>) EMPTY_SET;
    }

    /**
     * 指定类型创建空List,该方法不允许修稿数据
     *
     * @param cls 指定类型
     * @param <T> 泛型
     * @return 指定类型空List
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> List<T> emptyList(Class<T> cls) {
        return (List<T>) EMPTY_LIST;
    }

    /**
     * 指定类型创建空ArrayList,该方法不允许修稿数据
     *
     * @param cls 指定类型
     * @param <T> 泛型
     * @return 指定类型空ArrayList
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> ArrayList<T> emptyArrayList(Class<T> cls) {
        return (ArrayList<T>) EMPTY_ARRAYLIST;
    }

    /**
     * 指定类型创建空Set,该方法不允许修稿数据
     *
     * @param cls 指定类型
     * @param <T> 泛型
     * @return 指定类型空Set
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> Set<T> emptySet(Class<T> cls) {
        return (Set<T>) EMPTY_SET;
    }

    /**
     * 泛型方式创建空Map,该方法不允许修稿数据
     *
     * @param <T1> key泛型
     * @param <T2> value泛型
     * @return 指定泛型空Map
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T1, T2> Map<T1, T2> emptyMap() {
        return (Map<T1, T2>) EMPTY_MAP;
    }

    /**
     * 泛型方式创建空Map,该方法不允许修稿数据
     *
     * @param clazz1 key类型
     * @param clazz2 value类型
     * @param <T1>   key泛型
     * @param <T2>   value泛型
     * @return 指定泛型空Map
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T1, T2> Map<T1, T2> emptyMap(Class<T1> clazz1, Class<T2> clazz2) {
        return (Map<T1, T2>) EMPTY_MAP;
    }

    /**
     * 泛型数组转换泛型ArrayList
     *
     * @param ts  泛型数组
     * @param <T> 泛型
     * @return 新ArrayList
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... ts) {
        ArrayList<T> newList = new ArrayList<>();
        if (ts != null && ts.length > 0) {
            newList.addAll(Arrays.asList(ts));
        }
        return newList;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -       Convert       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 泛型方式将集合转换为ArrayLiat
     *
     * @param c   集合
     * @param <V> 泛型
     * @param <T> 泛型
     * @return ArrayList
     */
    public static <V, T extends V> List<V> toList(Collection<T> c) {
        if (c instanceof List) {
            return (List<V>) c;
        }
        return new ArrayList<>(c);
    }

    /**
     * 对象转换为指定类型的ArrayLiat
     *
     * @param o   对象
     * @param cls 指定类型
     * @param <V> 泛型
     * @param <T> 泛型
     * @return ArrayList
     */
    public static <V, T> List<V> toList(Object o, Class<V> cls) {
        if (o == null) {
            return null;
        }
        if (o instanceof Collection) {
            return toList(o, cls);
        }
        return toList(newArrayList(o), cls);
    }


    /**
     * 集合转换为指定类型的ArrayLiat
     *
     * @param c   集合
     * @param cls 指定类型
     * @param <V> 泛型
     * @param <T> 泛型
     * @return ArrayList
     */
    public static <V, T> ArrayList<V> toList(Collection<T> c, Class<V> cls) {
        return toList(c, cls, true);
    }

    /**
     * 集合转换为指定类型的ArrayLiat
     *
     * @param c         集合
     * @param cls       指定类型
     * @param allowNull 是否允许null
     * @param <V>       泛型
     * @param <T>       泛型
     * @return ArrayList
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <V, T> ArrayList<V> toList(Collection<T> c, Class<V> cls, boolean allowNull) {
        ArrayList<V> list = new ArrayList<>();
        for (T t : c) {
            if (t == null || cls == null || cls.isAssignableFrom(t.getClass())) {
                if (allowNull || t != null) {
                    try {
                        V v;
                        if (cls != null) {
                            v = cls.cast(t);
                        } else {
                            v = (V) t;
                        }
                        list.add(v);
                    } catch (ClassCastException ignored) {
                    }
                }
            }
        }
        return list;
    }

    /**
     * 泛型方式转换数组为List
     *
     * @param objs 数组
     * @param <V>  泛型
     * @param <T>  泛型
     * @return 指定类型的list
     */
    public static <V, T extends V> List<V> toList(T[] objs) {
        if (ArrayUtil.isEmpty(objs)) {
            return null;
        }
        return Arrays.asList(objs);
    }

    /**
     * 数组转换为指定类型List
     *
     * @param c   数组
     * @param cls 指定类型
     * @param <V> 泛型
     * @param <T> 泛型
     * @return 指定类型的list
     */
    public static <V, T extends V> List<V> toList(T[] c, Class<V> cls) {
        List<V> list = new ArrayList<>();
        for (T co : c) {
            try {
                V v = cls.cast(co);
                list.add(v);
            } catch (ClassCastException e) {
                throw new UtilException(MessageFormat.format("错误! 不能转换 {0} 到 {1}!", co, cls.getSimpleName()));
            }
        }
        return list;
    }

    /**
     * 数组转Set
     *
     * @param tArray 数组
     * @param <T>    泛型
     * @return Set
     */
    public static <T> Set<T> toSet(T[] tArray) {
        return new LinkedHashSet<>(newArrayList(tArray));
    }

    /**
     * 集合转Set
     *
     * @param c   集合
     * @param <T> 泛型
     * @return Set
     */
    public static <T> Set<T> toSet(Collection<T> c) {
        if (c instanceof Set) {
            return (Set<T>) c;
        }
        return new LinkedHashSet<>(c);
    }

    /**
     * 转换为HashSet
     *
     * @param <T> 泛型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> toHashSet(T... ts) {
        HashSet<T> set = new HashSet<>();
        Collections.addAll(set, ts);
        return set;
    }

    /**
     * 转换为HashSet
     *
     * @param <T>        泛型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> toHashSet(Collection<T> collection) {
        HashSet<T> set = new HashSet<>();
        set.addAll(collection);
        return set;
    }

    /**
     * SET转换MAP
     *
     * @param setobj Set数据
     * @return Map
     */
    @SuppressWarnings(value = {"unchecked"})
    public static Map<Object, Object> toMap(Set<Object> setobj) {
        Map<Object, Object> map = new HashMap<>();
        for (Object aSetobj : setobj) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) aSetobj;
            map.put(entry.getKey().toString(), entry.getValue() == null ? "" : entry.getValue().toString().trim());
        }
        return map;

    }

    /**
     * 将Entry集合转换为HashMap
     *
     * @param <T>             泛型
     * @param <K>             泛型
     * @param entryCollection entry集合
     * @return Map
     */
    public static <T, K> HashMap<T, K> toHashMap(Collection<Map.Entry<T, K>> entryCollection) {
        HashMap<T, K> map = new HashMap<>();
        for (Map.Entry<T, K> entry : entryCollection) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * 将集合转换为排序后的TreeSet
     *
     * @param <T>        泛型
     * @param collection 集合
     * @param comparator 比较器
     * @return treeSet
     */
    public static <T> TreeSet<T> toTreeSet(Collection<T> collection, Comparator<T> comparator) {
        final TreeSet<T> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(collection.stream().collect(Collectors.toList()));
        return treeSet;
    }

    /**
     * Iterator转换为Enumeration
     *
     * @param <E>  泛型
     * @param iter Iterator
     * @return Enumeration
     */
    public static <E> Enumeration<E> toEnumeration(final Iterator<E> iter) {
        return new Enumeration<E>() {
            @Override
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            @Override
            public E nextElement() {
                return iter.next();
            }
        };
    }

    /**
     * Enumeration转换为Iterator<br>
     *
     * @param <E> 泛型
     * @param e   Enumeration
     * @return Iterator
     */
    public static <E> Iterator<E> toIterator(final Enumeration<E> e) {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return e.hasMoreElements();
            }

            @Override
            public E next() {
                return e.nextElement();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("不支持该操作");
            }
        };
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>    泛型
     * @param values 数组
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> toArrayList(T... values) {
        ArrayList<T> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, values);
        return arrayList;
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>        泛型
     * @param collection 集合
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toArrayList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -      Truncation     - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 截断
     *
     * @param coll        集合
     * @param maximumSize 截断数量
     * @param <T>         泛型
     * @return 集合
     */
    public static <T> Collection<T> truncation(Collection<T> coll, int maximumSize) {
        if (coll.size() > maximumSize) {
            Iterator<T> iter = coll.iterator();
            for (int i = 0; i < maximumSize; ++i) {
                iter.next();
            }
            while (iter.hasNext()) {
                iter.next();
                iter.remove();
            }
        }
        return coll;
    }

    /**
     * 截取集合的部分
     *
     * @param <T>   泛型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @return 截取后的数组，当开始位置超过最大时，返回null
     */
    public static <T> List<T> truncation(Collection<T> list, int start, int end) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return truncation(new ArrayList<>(list), start, end);
    }

    /**
     * 截取数组的部分
     *
     * @param <T>   泛型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @return 截取后的数组，当开始位置超过最大时，返回null
     */
    public static <T> List<T> truncation(List<T> list, int start, int end) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        final int size = list.size();
        if (end > size) {
            if (start >= size) {
                return null;
            }
            end = size;
        }
        return list.subList(start, end);
    }

    /**
     * 切取部分数据
     *
     * @param <T>             集合元素类型
     * @param surplusAlaDatas 原数据
     * @param partSize        每部分数据的长度
     * @return 切取出的数据或null
     */
    public static <T> List<T> truncation(Stack<T> surplusAlaDatas, int partSize) {
        if (surplusAlaDatas == null || surplusAlaDatas.size() <= 0) {
            return null;
        }
        final List<T> currentAlaDatas = new ArrayList<>();
        int size = surplusAlaDatas.size();
        if (size > partSize) {
            for (int i = 0; i < partSize; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        } else {
            for (int i = 0; i < size; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        }
        return currentAlaDatas;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -      Intersect      - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 判断两个集合是否相交
     *
     * @param <T>   泛型
     * @param coll1 集合1
     * @param coll2 集合2
     * @return true:相交 false:不想交
     */
    public static <T> boolean intersect(Collection<T> coll1, Collection<T> coll2) {
        if (null == coll1 || coll1.isEmpty()) {
            return false;
        }
        if (null == coll2 || coll2.isEmpty()) {
            return false;
        }
        Collection<T> deleteList = new ArrayList<>();
        boolean anySame = false;
        for (T o : coll1) {
            if (!coll2.contains(o)) {
                deleteList.add(o);
            } else {
                anySame = true;
            }
        }
        coll1.removeAll(deleteList);
        return anySame;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -         Zip         - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 映射键值（参考Python的zip()函数）<br>
     * 例如：<br>
     * keys = [a,b,c,d]<br>
     * values = [1,2,3,4]<br>
     * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param <T>    泛型
     * @param <K>    泛型
     * @param keys   键列表
     * @param values 值列表
     * @return Map
     */
    public static <T, K> Map<T, K> zip(T[] keys, K[] values) {
        if (ArrayUtil.isEmpty(keys) || ArrayUtil.isEmpty(values)) {
            return null;
        }
        final int size = Math.min(keys.length, values.length);
        final Map<T, K> map = new HashMap<>((int) (size / 0.75));
        for (int i = 0; i < size; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    /**
     * 映射键值（参考Python的zip()函数）<br>
     * 例如：<br>
     * keys = a,b,c,d<br>
     * values = 1,2,3,4<br>
     * delimiter = , 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param delimiter 分隔符
     * @param keys      键列表
     * @param values    值列表
     * @return Map
     */
    public static Map<String, String> zip(String keys, String values, String delimiter) {
        return zip(keys.split(delimiter), values.split(delimiter));
    }

    /**
     * 映射键值（参考Python的zip()函数）<br>
     * 例如：<br>
     * keys = [a,b,c,d]<br>
     * values = [1,2,3,4]<br>
     * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param <T>    泛型
     * @param <K>    泛型
     * @param keys   键列表
     * @param values 值列表
     * @return Map
     */
    public static <T, K> Map<T, K> zip(Collection<T> keys, Collection<K> values) {
        if (null == keys || keys.isEmpty() || null == values || values.isEmpty()) {
            return null;
        }
        final List<T> keyList = new ArrayList<>(keys);
        final List<K> valueList = new ArrayList<>(values);
        final int size = Math.min(keys.size(), values.size());
        final Map<T, K> map = new HashMap<>((int) (size / 0.75));
        for (int i = 0; i < size; i++) {
            map.put(keyList.get(i), valueList.get(i));
        }
        return map;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -         Sort        - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 排序集合
     *
     * @param <T>        泛型
     * @param collection 集合
     * @param comparator 比较器
     * @return treeSet
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        List<T> list = new ArrayList<>(collection);
        list.sort(comparator);
        return list;
    }

    /**
     * 将Set排序（根据Entry的值）
     *
     * @param set 被排序的Set
     * @return 排序后的Set
     */
    public static List<Map.Entry<Long, Long>> sortEntrySetToList(Set<Map.Entry<Long, Long>> set) {
        List<Map.Entry<Long, Long>> list = new LinkedList<>(set);
        list.sort((o1, o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return 1;
            }
            if (o1.getValue() < o2.getValue()) {
                return -1;
            }
            return 0;
        });
        return list;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -        Filter       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 过滤
     * 过滤会改变原集合的内容
     *
     * @param <T>        泛型
     * @param collection 集合
     * @param filter     过滤器接口
     * @return 过滤后的数组
     */
    public static <T> Collection<T> filter(Collection<T> collection, Filter<T> filter) {
        Collection<T> collection2 = ObjectUtil.clone(collection);
        collection2.clear();

        T modified;
        for (T t : collection) {
            modified = filter.modify(t);
            if (null != modified) {
                collection2.add(t);
            }
        }
        return collection2;
    }

    /**
     * 过滤
     *
     * @param <K>    泛型
     * @param <V>    泛型
     * @param map    Map
     * @param filter 过滤器
     * @return 过滤后的Map
     */
    public static <K, V> Map<K, V> filter(Map<K, V> map, Filter<Map.Entry<K, V>> filter) {
        Map<K, V> map2 = ObjectUtil.clone(map);
        map2.clear();

        Map.Entry<K, V> modified;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            modified = filter.modify(entry);
            if (null != modified) {
                map2.put(entry.getKey(), entry.getValue());
            }
        }
        return map2;
    }

    /**
     * 从List中随机取出一个元素。
     *
     * @param list 源List
     * @param <T>  泛型
     * @return T List的一个元素
     */
    public static <T> T randomOne(List<T> list) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        int subtract = list.size();
        Double ranDouble = Math.floor(Math.random() * subtract);
        return list.get(ranDouble.intValue());
    }
}
