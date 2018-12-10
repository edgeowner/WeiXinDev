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


import com.github.asherli0103.utils.lang.filter.Filter;

import java.lang.reflect.Array;
import java.util.*;


/**
 * 集合相关工具类，包括数组
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
public class ArrayUtil {

    private ArrayUtil() {

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -        isArray      - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
            throw new NullPointerException("检查对象不能是null");
        }
        return obj.getClass().isArray();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -      Empty/Null     - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 检查long型数组是空或 {@code null}
     *
     * @param <T>   泛型
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static <T> boolean isEmpty(final T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查long型数组是空或 {@code null}
     *
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isEmpty(final long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查int型数组是空或 {@code null}
     *
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isEmpty(final int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查short型数组是空或 {@code null}
     *
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isEmpty(final short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查char型数组是空或 {@code null}
     *
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isEmpty(final char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查byte型数组是空或 {@code null}
     *
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isEmpty(final byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查double型数组是空或 {@code null}
     *
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isEmpty(final double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查float型数组是空或 {@code null}
     *
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isEmpty(final float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查boolean型数组是空或 {@code null}
     *
     * @param array 待检查数组
     * @return 如果数组是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isEmpty(final boolean[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查对象数组不为空或{@code null}
     *
     * @param <T>   泛型
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     * @since 2.5
     */
    public static <T> boolean isNotEmpty(final T[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查long数组不为空或{@code null}
     *
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isNotEmpty(final long[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查int数组不为空或{@code null}
     *
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isNotEmpty(final int[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查short数组不为空或{@code null}
     *
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isNotEmpty(final short[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查char数组不为空或{@code null}
     *
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isNotEmpty(final char[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查byte数组不为空或{@code null}
     *
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isNotEmpty(final byte[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查double数组不为空或{@code null}
     *
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isNotEmpty(final double[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查float数组不为空或{@code null}
     *
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isNotEmpty(final float[] array) {
        return !isEmpty(array);
    }

    /**
     * 检查boolean数组不为空或{@code null}
     *
     * @param array 待检查数组
     * @return 如果数组不是 {@code null} 或者空的返回 {@code true}
     */
    public static boolean isNotEmpty(final boolean[] array) {
        return !isEmpty(array);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -        Create       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 新建一个空数组
     *
     * @param <T>           泛型
     * @param componentType 元素类型
     * @param newSize       大小
     * @return 空数组
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T[] newArray(Class<?> componentType, int newSize) {
        return (T[]) Array.newInstance(componentType, newSize);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -       Convert       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 将list转为数组。
     *
     * @param list 源list
     * @param <T>  泛型
     * @return T[]
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T[] toArray(List<T> list) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        T[] objs = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
        int i = 0; //数组下标。
        for (T obj : list) {
            objs[i++] = obj;
        }
        return objs;
    }

    @SuppressWarnings(value = {"unchecked"})
    public static <T> T[] toArray(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        return (T[]) collection.toArray();
    }

    @SuppressWarnings(value = {"unchecked"})
    public static <T> T[] toArray(Enumeration<T> enumeration) {
        if (enumeration == null) {
            return null;
        }
        List<T> list = Collections.list(enumeration);
        return toArray(list);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -        Resize       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 生成一个新的重新设置大小的数组
     *
     * @param <T>           泛型
     * @param array         原数组
     * @param newSize       新的数组大小
     * @param componentType 数组元素类型
     * @return 调整后的新数组
     */
    public static <T> T[] resize(T[] array, int newSize, Class<?> componentType) {
        T[] newArray = newArray(componentType, newSize);
        System.arraycopy(array, 0, newArray, 0, array.length >= newSize ? newSize : array.length);
        return newArray;
    }

    /**
     * 生成一个新的重新设置大小的数组<br>
     * 新数组的类型为原数组的类型
     *
     * @param <T>     泛型
     * @param array   原数组
     * @param newSize 新的数组大小
     * @return 调整后的新数组
     */
    public static <T> T[] resize(T[] array, int newSize) {
        return resize(array, newSize, array.getClass().getComponentType());
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -       Append        - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 将新元素添加到已有数组中<br>
     * 添加新元素会生成一个新的数组，不影响原数组
     *
     * @param <T>        泛型
     * @param array      已有数组
     * @param newElement 新元素
     * @return 新数组
     */
    public static <T> T[] append(T[] array, T newElement) {
        T[] t = resize(array, array.length + 1, newElement.getClass());
        t[array.length] = newElement;
        return t;
    }

    /**
     * 向list中添加数组。
     *
     * @param list  List
     * @param array 数组
     * @param <T>   泛型
     */
    public static <T> void append(List<T> list, T[] array) {
        if (null == list || list.isEmpty()) {
            return;
        }
        Collections.addAll(list, array);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -       Reverse       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 将数组进行反转，倒置。
     *
     * @param objs 源数组
     * @param <T>  泛型
     * @return T[] 反转后的数组
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T[] reverse(T[] objs) {
        if (isEmpty(objs)) {
            return null;
        }
        T[] res = (T[]) Array.newInstance(objs[0].getClass(), objs.length);
        //新序号
        int k = 0;
        for (int i = objs.length - 1; i >= 0; i--) {
            res[k++] = objs[i];
        }
        return res;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -       Contains      - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 数组中是否存在这个元素。
     *
     * @param objArr  数组
     * @param compare 元素
     * @param <T>     泛型
     * @return 存在返回true，否则返回false。
     */
    public static <T> boolean contains(T[] objArr, T compare) {
        if (isEmpty(objArr)) {
            return false;
        }
        for (T obj : objArr) {
            if (obj.equals(compare)) {
                return true;
            }
        }
        return false;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -         Get         - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 从数组中随机取出一个元素。
     *
     * @param objs 源数组
     * @param <T>  泛型
     * @return T 数组的一个元素
     */
    public static <T> T randomOne(T[] objs) {
        if (isEmpty(objs)) {
            return null;
        }
        int subtract = objs.length;
        Double ranDouble = Math.floor(Math.random() * subtract);
        return objs[ranDouble.intValue()];
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -        Merges       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param <T>    泛型
     * @param arrays 数组集合
     * @return 合并后的数组
     */
    @SafeVarargs
    public static <T> T[] merges(T[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }
        int length = 0;
        for (T[] array : arrays) {
            if (array == null) {
                continue;
            }
            length += array.length;
        }
        T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);
        length = 0;
        for (T[] array : arrays) {
            if (array == null) {
                continue;
            }
            System.arraycopy(array, 0, result, length, array.length);
            length += array.length;
        }
        return result;
    }

    /**
     * 将一个字符串数组的内容全部添加到另外一个数组中，并返回一个新数组。
     *
     * @param array1 第一个数组
     * @param array2 第二个数组
     * @param <T>    泛型
     * @return T[] 拼接后的新数组
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T[] merges(T[] array1, T[] array2) {
        if (isEmpty(array1)) {
            return array2;
        }
        if (isEmpty(array2)) {
            return array1;
        }
        T[] resArray = (T[]) Array.newInstance(array1[0].getClass(), array1.length + array2.length);
        System.arraycopy(array1, 0, resArray, 0, array1.length);
        System.arraycopy(array2, 0, resArray, array1.length, array2.length);
        return resArray;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -         Range       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 生成一个数字列表<br>
     * 自动判定正序反序
     *
     * @param includedStart 开始的数字（包含）
     * @param excludedEnd   结束的数字（不包含）
     * @param step          步进
     * @return 数字列表
     */
    public static int[] range(int includedStart, int excludedEnd, int step) {
        if (includedStart > excludedEnd) {
            int tmp = includedStart;
            includedStart = excludedEnd;
            excludedEnd = tmp;
        }

        if (step <= 0) {
            step = 1;
        }

        int deviation = excludedEnd - includedStart;
        int length = deviation / step;
        if (deviation % step != 0) {
            length += 1;
        }
        int[] range = new int[length];
        for (int i = 0; i < length; i++) {
            range[i] = includedStart;
            includedStart += step;
        }
        return range;
    }

    /**
     * 生成一个数字列表<br>
     * 自动判定正序反序
     *
     * @param excludedEnd 结束的数字（不包含）
     * @return 数字列表
     */
    public static int[] range(int excludedEnd) {
        return range(0, excludedEnd, 1);
    }

    /**
     * 生成一个数字列表<br>
     * 自动判定正序反序
     *
     * @param includedStart 开始的数字（包含）
     * @param excludedEnd   结束的数字（不包含）
     * @return 数字列表
     */
    public static int[] range(int includedStart, int excludedEnd) {
        return range(includedStart, excludedEnd, 1);
    }


    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -         Warp        - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 将基本类型数组包装为包装类型
     *
     * @param values 基本类型数组
     * @return 包装类型数组
     */
    public static Integer[] wrap(int... values) {
        final int length = values.length;
        Integer[] array = new Integer[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    /**
     * 将基本类型数组包装为包装类型
     *
     * @param values 基本类型数组
     * @return 包装类型数组
     */
    public static Long[] wrap(long... values) {
        final int length = values.length;
        Long[] array = new Long[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    /**
     * 将基本类型数组包装为包装类型
     *
     * @param values 基本类型数组
     * @return 包装类型数组
     */
    public static Character[] wrap(char... values) {
        final int length = values.length;
        Character[] array = new Character[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    /**
     * 将基本类型数组包装为包装类型
     *
     * @param values 基本类型数组
     * @return 包装类型数组
     */
    public static Byte[] wrap(byte... values) {
        final int length = values.length;
        Byte[] array = new Byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    /**
     * 将基本类型数组包装为包装类型
     *
     * @param values 基本类型数组
     * @return 包装类型数组
     */
    public static Short[] wrap(short... values) {
        final int length = values.length;
        Short[] array = new Short[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    /**
     * 将基本类型数组包装为包装类型
     *
     * @param values 基本类型数组
     * @return 包装类型数组
     */
    public static Float[] wrap(float... values) {
        final int length = values.length;
        Float[] array = new Float[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    /**
     * 将基本类型数组包装为包装类型
     *
     * @param values 基本类型数组
     * @return 包装类型数组
     */
    public static Double[] wrap(double... values) {
        final int length = values.length;
        Double[] array = new Double[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    /**
     * 将基本类型数组包装为包装类型
     *
     * @param values 基本类型数组
     * @return 包装类型数组
     */
    public static Boolean[] wrap(boolean... values) {
        final int length = values.length;
        Boolean[] array = new Boolean[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //- - - - - - - - - - - -        Filter       - - - - - - - - - - - - - -
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 过滤
     *
     * @param <T>    泛型
     * @param array  数组
     * @param filter 过滤器接口
     * @return 过滤后的数组
     */
    public static <T> T[] filter(T[] array, Filter<T> filter) {
        ArrayList<T> list = new ArrayList<>();
        T modified;
        for (T t : array) {
            modified = filter.modify(t);
            if (null != modified) {
                list.add(t);
            }
        }
        return list.toArray(Arrays.copyOf(array, list.size()));
    }

}
