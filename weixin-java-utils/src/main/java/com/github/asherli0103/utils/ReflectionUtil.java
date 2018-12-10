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

import com.github.asherli0103.utils.enums.BasicType;
import com.github.asherli0103.utils.exception.UtilException;
import com.github.asherli0103.utils.lang.Singleton;
import com.github.asherli0103.utils.lang.filter.Filter;
import com.github.asherli0103.utils.tools.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);


    /**
     * 调用Getter方法.
     *
     * @param obj          exception
     * @param propertyName exception
     * @return exception
     */
    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtil.toUpperCaseAt(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[]{}, new Object[]{});
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     *
     * @param obj          exception
     * @param propertyName exception
     * @param value        exception
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
        invokeSetterMethod(obj, propertyName, value, null);
    }

    /**
     * 调用Setter方法.
     *
     * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
     * @param obj          exception
     * @param propertyName exception
     * @param value        exception
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtil.toUpperCaseAt(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     *
     * @param obj       exception
     * @param fieldName exception
     * @return exception
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            LOGGER.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     *
     * @param obj       exception
     * @param fieldName exception
     * @param value     exception
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            LOGGER.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField,   并强制设置为可访问.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param obj       exception
     * @param fieldName exception
     * @return exception
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        Assert.notNull(obj, "object不能为空");
        Assert.hasText(fieldName, "fieldName");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {//NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况.
     *
     * @param obj            exception
     * @param methodName     exception
     * @param parameterTypes exception
     * @param args           exception
     * @return exception
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     *
     * @param obj            对象
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @return 方法
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        Assert.notNull(obj, "object不能为空");

        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

                method.setAccessible(true);

                return method;

            } catch (NoSuchMethodException e) {//NOSONAR
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao&lt;User&gt;
     *
     * @param clazz The class to introspect
     * @param <T>   泛型
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao&lt;User,Long&gt;
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            LOGGER.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            LOGGER.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LOGGER.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     *
     * @param e 异常
     * @return 原始异常
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException("Reflection Exception.", e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }


    /**
     * Make the given method accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     *
     * @param method the method to make accessible
     * @see Method#setAccessible
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 获取JavaBean的非空属性的名-值映射
     *
     * @param javaBean      bean
     * @param excludeFields 排除属性
     * @param <T>           泛型
     * @return map
     */
    public static <T> Map<String, Object> getJavaBeanNotNullPropName2ValueMap(T javaBean, String... excludeFields) {
        Map<String, Object> ret = new HashMap<>();
        List<String> excludeFieldList = Arrays.asList(excludeFields);
        Method[] methods = javaBean.getClass().getMethods();
        for (Method method : methods) {
            if (method.getModifiers() == Modifier.PUBLIC && method.getName().startsWith("get")) {
                // 提取getXxx中的Xxx部分
                String _Xxx = method.getName().substring(3);
                // 判断是否有对象setXxx方法
                String setXxx = "set" + _Xxx;
                try {
                    Method m = javaBean.getClass().getMethod(setXxx, method.getReturnType());
                    if (m == null) {
                        continue;// 忽略后面的代码，继续下一次循环
                    }
                    String name = StringUtil.toLowerCaseAt(_Xxx);
                    // 判断是否不是被排序的字段
                    if (!excludeFieldList.contains(name)) {
                        Object value = method.invoke(javaBean);
                        if (value != null) {// 非空
                            ret.put(name, value);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return ret;
    }

    // 提供对应实体对象（符合JavaBean规范）属性的类型
    public static Class<?> getJavaBeanFieldType(Class<?> clazz, String fieldName) {
        Method m = null;
        String getXxx = "get" + StringUtil.toUpperCaseAt(fieldName);
        try {
            m = clazz.getMethod(getXxx);
            return m.getReturnType();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    // 提供对应属性的set方法
    public static Method getJavaBeanSetMethod(Class<?> clazz, String fieldName) {
        Method m = null;
        fieldName = StringUtil.toUpperCaseAt(fieldName);
        String getXxx = "get" + fieldName;
        String setXxx = "set" + fieldName;
        try {
            m = clazz.getMethod(getXxx);
            // 判断是否有对象setXxx方法
            m = clazz.getMethod(setXxx, m.getReturnType());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return m;
    }

    // 提供对应属性的get方法
    public static Method getJavaBeanGetMethod(Class<?> clazz, String fieldName) {
        Method m = null;
        fieldName = StringUtil.toUpperCaseAt(fieldName);
        String getXxx = "get" + fieldName;
        try {
            m = clazz.getMethod(getXxx);
            return m;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public static void setJavaBeanValue(Object javaBean, String name, Object value) {
        Method m = getJavaBeanSetMethod(javaBean.getClass(), name);
        if (m != null) {
            try {
                m.invoke(javaBean, value);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public static Object getJavaBeanValue(Object javaBean, String name) {
        Method m = getJavaBeanGetMethod(javaBean.getClass(), name);
        if (m != null) {
            try {
                return m.invoke(javaBean);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return null;
    }

    // 将Object数组值转按指定属性顺序转换成一个T对象
    public static <T> T setValue(Class<T> clazz, Map<String, Object> field2ValueMap) {
        T javaBean = null;
        try {
            javaBean = clazz.newInstance();
            for (Map.Entry<String, Object> entry : field2ValueMap.entrySet()) {
                Method m = getJavaBeanSetMethod(clazz, entry.getKey());
                if (m != null) {
                    m.invoke(javaBean, entry.getValue());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return javaBean;
    }

    /**
     * 查找方法
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 方法
     */
    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            return findDeclaredMethod(clazz, methodName, paramTypes);
        }
    }

    /**
     * 查找所有方法
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return Method
     */
    public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            if (clazz.getSuperclass() != null) {
                return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
            }
            return null;
        }
    }

    /**
     * 获得指定类过滤后的方法列表
     *
     * @param clazz 查找方法的类
     * @return 过滤后的方法列表
     */
    public static List<Method> getMethods(Class<?> clazz) {
        return getMethods(clazz, (Filter<Method>) null);
    }

    /**
     * 获得指定类过滤后的方法列表
     *
     * @param clazz  查找方法的类
     * @param filter 过滤器
     * @return 过滤后的方法列表
     */
    public static List<Method> getMethods(Class<?> clazz, Filter<Method> filter) {
        if (null == clazz) {
            return null;
        }

        Method[] methods = clazz.getMethods();
        List<Method> methodList;
        if (null != filter) {
            methodList = new ArrayList<>();
            Method filteredMethod;
            for (Method method : methods) {
                filteredMethod = filter.modify(method);
                if (null != filteredMethod) {
                    methodList.add(method);
                }
            }
        } else {
            methodList = CollectionUtil.toArrayList(methods);
        }
        return methodList;
    }

    /**
     * 获得指定类过滤后的方法列表
     *
     * @param clazz          查找方法的类
     * @param excludeMethods 不包括的方法
     * @return 过滤后的方法列表
     */
    public static List<Method> getMethods(Class<?> clazz, Method... excludeMethods) {
        final HashSet<Method> excludeMethodSet = CollectionUtil.toHashSet(excludeMethods);
        Filter<Method> filter = method -> excludeMethodSet.contains(method) ? null : method;
        return getMethods(clazz, filter);
    }

    /**
     * 获得指定类过滤后的方法列表
     *
     * @param clazz              查找方法的类
     * @param excludeMethodNames 不包括的方法名列表
     * @return 过滤后的方法列表
     */
    public static List<Method> getMethods(Class<?> clazz, String... excludeMethodNames) {
        final HashSet<String> excludeMethodNameSet = CollectionUtil.toHashSet(excludeMethodNames);
        Filter<Method> filter = method -> excludeMethodNameSet.contains(method.getName()) ? null : method;
        return getMethods(clazz, filter);
    }

    /**
     * 转换为原始类型
     *
     * @param clazz 被转换为原始类型的类，必须为包装类型的类
     * @return 基本类型类
     */
    public static Class<?> castToPrimitive(Class<?> clazz) {
        if (null == clazz || clazz.isPrimitive()) {
            return clazz;
        }

        BasicType basicType;
        try {
            basicType = BasicType.valueOf(clazz.getSimpleName().toUpperCase());
        } catch (Exception e) {
            return clazz;
        }

        // 基本类型
        switch (basicType) {
            case BYTE:
                return byte.class;
            case SHORT:
                return short.class;
            case INTEGER:
                return int.class;
            case LONG:
                return long.class;
            case DOUBLE:
                return double.class;
            case FLOAT:
                return float.class;
            case BOOLEAN:
                return boolean.class;
            case CHAR:
                return char.class;
            default:
                return clazz;
        }
    }


    /**
     * 用于对象的类型转换
     *
     * @param type 类型
     * @param var  数据
     * @return 根据格式转换数据
     * @throws Exception exception
     */
    public static Object convert(Class type, String var) throws Exception {
        if (type.getName().equalsIgnoreCase("java.lang.String")) {
            return var;
        } else if (type.getName().equalsIgnoreCase("java.math.BigDecimal")) {
            return new BigDecimal(var);
        } else if (type.getName().equalsIgnoreCase("java.sql.Date")
                || type.getName().equalsIgnoreCase("java.util.Date")) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(var);
            } catch (ParseException e) {
                throw new Exception(e);
            }
        } else if (type.getName().equalsIgnoreCase("java.lang.Long")
                || type.getName().equalsIgnoreCase("long")) {
            return new Long(var);
        } else if (type.getName().equalsIgnoreCase("java.lang.Integer")
                || type.getName().equalsIgnoreCase("int")) {
            return new Integer(var);
        } else if (type.getName().equalsIgnoreCase("java.lang.Boolean")
                || type.getName().equalsIgnoreCase("boolean")) {
            return Boolean.valueOf(var);
        } else {
            return null;
        }
    }

    /**
     * 新建代理对象<br>
     * 动态代理类对象用于动态创建一个代理对象，可以在调用接口方法的时候动态执行相应逻辑
     *
     * @param <T>               泛型
     * @param interfaceClass    被代理接口
     * @param invocationHandler 代理执行类，此类用于实现具体的接口方法
     * @return 被代理接口
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T newProxyInstance(Class<T> interfaceClass, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, invocationHandler);
    }

    /**
     * 设置方法为可访问
     *
     * @param method 方法
     * @return 方法
     */
    public static Method setAccessible(Method method) {
        if (null != method && ClassUtil.isNotPublic(method)) {
            method.setAccessible(true);
        }
        return method;
    }

    /**
     * 获得指定类中的Public方法名<br>
     * 去重重载的方法
     *
     * @param clazz 类
     * @return set
     */
    public static Set<String> getMethodNames(Class<?> clazz) {
        HashSet<String> methodSet = new HashSet<>();
        Method[] methodArray = clazz.getMethods();
        for (Method method : methodArray) {
            String methodName = method.getName();
            methodSet.add(methodName);
        }
        return methodSet;
    }

    /**
     * 实例化对象
     *
     * @param <T>       泛型
     * @param className 类名
     * @return 对象
     * @throws ClassNotFoundException 未找到类抛出异常
     * @throws IllegalAccessException 数据格式错误抛出异常
     * @throws InstantiationException 实例化错误抛出异常
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T newInstance(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (T) Class.forName(className).newInstance();
    }

    /**
     * 实例化对象
     *
     * @param <T>   泛型
     * @param clazz 类
     * @return 对象
     * @throws IllegalAccessException 数据格式错误抛出异常
     * @throws InstantiationException 实例化错误抛出异常
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T newInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }

    /**
     * 实例化对象
     *
     * @param <T>    泛型
     * @param clazz  类
     * @param params 参数
     * @return 对象
     * @throws InstantiationException    实例化错误抛出异常
     * @throws IllegalAccessException    数据格式错误抛出异常
     * @throws NoSuchMethodException     未找到方法时抛出异常
     * @throws InvocationTargetException 反射目标异常时抛出
     */
    public static <T> T newInstance(Class<T> clazz, Object... params) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (ArrayUtil.isEmpty(params)) {
            return newInstance(clazz);
        }
        return clazz.getDeclaredConstructor(ClassUtil.getClasses(params)).newInstance(params);
    }

    public static <T> T newInstance(final String implClass, final Class<T> tType) {
        return newInstance(implClass, tType, true);
    }

    public static <T> T newInstance(final String implClass, final Class<T> tType, final boolean doInit) {
        try {
            final Object object = Class.forName(implClass, doInit, Thread.currentThread().getContextClassLoader()).newInstance();
            return tType.cast(object);
        } catch (final Throwable e) {
            return null;
        }
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     * 非单例模式，如果是非静态方法，每次创建一个新对象
     *
     * @param <T>                    泛型
     * @param classNameDotMethodName 类名和方法名表达式，例如：com.xiaoleilu.hutool.StrUtil.isEmpty
     * @param args                   参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws NoSuchMethodException     未找到方法时抛出异常
     * @throws InstantiationException    exception
     * @throws IllegalAccessException    exception
     * @throws InvocationTargetException 反射目标异常时抛出
     * @throws ClassNotFoundException    exception
     */
    public static <T> T invoke(String classNameDotMethodName, Object... args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return invoke(classNameDotMethodName, false, args);
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     *
     * @param <T>                    泛型
     * @param classNameDotMethodName 类名和方法名表达式，例如：com.xiaoleilu.hutool.StrUtil.isEmpty
     * @param isSingleton            是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
     * @param args                   参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws InvocationTargetException 反射目标异常时抛出n
     * @throws NoSuchMethodException     未找到方法时抛出异常
     * @throws InstantiationException    exception
     * @throws IllegalAccessException    exception
     * @throws ClassNotFoundException    exception
     */
    public static <T> T invoke(String classNameDotMethodName, boolean isSingleton, Object... args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (StringUtil.isBlank(classNameDotMethodName)) {
            throw new UtilException("Blank classNameDotMethodName!");
        }
        final int dotIndex = classNameDotMethodName.lastIndexOf('.');
        if (dotIndex <= 0) {
            throw new UtilException(MessageFormat.format("Invalid classNameDotMethodName [{0}]!", classNameDotMethodName));
        }

        final String className = classNameDotMethodName.substring(0, dotIndex);
        final String methodName = classNameDotMethodName.substring(dotIndex + 1);

        return invoke(className, methodName, isSingleton, args);
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     * 非单例模式，如果是非静态方法，每次创建一个新对象
     *
     * @param <T>        泛型
     * @param className  类名，完整类路径
     * @param methodName 方法名
     * @param args       参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws InstantiationException    exception
     * @throws IllegalAccessException    exception
     * @throws NoSuchMethodException     exception
     * @throws InvocationTargetException exception
     * @throws ClassNotFoundException    exception
     */
    public static <T> T invoke(String className, String methodName, Object... args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        return invoke(className, methodName, false, args);
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     *
     * @param <T>         泛型
     * @param className   类名，完整类路径
     * @param methodName  方法名
     * @param isSingleton 是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
     * @param args        参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws IllegalAccessException    exception
     * @throws InstantiationException    exception
     * @throws NoSuchMethodException     exception
     * @throws InvocationTargetException exception
     * @throws ClassNotFoundException    exception
     */
    public static <T> T invoke(String className, String methodName, boolean isSingleton, Object... args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        Class<Object> clazz = loadClass(className);
        return invoke(isSingleton ? Singleton.get(clazz) : clazz.newInstance(), methodName, args);
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     *
     * @param <T>        泛型
     * @param obj        对象
     * @param methodName 方法名
     * @param args       参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws NoSuchMethodException     exception
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException    exception
     */
    public static <T> T invoke(Object obj, String methodName, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method method = getDeclaredMethod(obj, methodName, args);
        return invoke(obj, method, args);
    }

    /**
     * 执行方法
     *
     * @param <T>    泛型
     * @param obj    对象
     * @param method 方法（对象方法或static方法都可）
     * @param args   参数对象
     * @return 结果
     * @throws IllegalAccessException    exception
     * @throws IllegalArgumentException  exception
     * @throws InvocationTargetException 反射目标异常时抛出
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T invoke(Object obj, Method method, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, args);
    }

    /**
     * 加载类
     *
     * @param <T>           泛型
     * @param className     类名
     * @param isInitialized 是否初始化
     * @return 类
     * @throws ClassNotFoundException exception
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> Class<T> loadClass(String className, boolean isInitialized) throws ClassNotFoundException {
        return (Class<T>) Class.forName(className, isInitialized, getClassLoader());
    }

    /**
     * 加载类并初始化
     *
     * @param <T>       泛型
     * @param className 类名
     * @return 类
     * @throws ClassNotFoundException exception
     */
    public static <T> Class<T> loadClass(String className) throws ClassNotFoundException {
        return loadClass(className, true);
    }

    /**
     * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
     *
     * @param obj        被查找的对象
     * @param methodName 方法名
     * @param args       参数
     * @return 方法
     * @throws NoSuchMethodException 无此方法
     * @throws SecurityException     exception
     */
    public static Method getDeclaredMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException, SecurityException {
        return getDeclaredMethod(obj.getClass(), methodName, ClassUtil.getClasses(args));
    }

    /**
     * 查找指定类中的所有方法（包括非public方法），也包括父类和Object类的方法
     *
     * @param clazz          被查找的类
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @return 方法
     * @throws NoSuchMethodException 无此方法
     * @throws SecurityException     exception
     */
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        Method method;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
        }
        return Object.class.getDeclaredMethod(methodName, parameterTypes);
    }

    /**
     * 获得class loader<br>
     * 若当前线程class loader不存在，取当前类的class loader
     *
     * @return 类加载器
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            classLoader = ReflectionUtil.class.getClassLoader();
        }
        return classLoader;
    }

    /**
     * 获取当前线程的ClassLoader
     *
     * @return 当前线程的class loader
     */
    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
