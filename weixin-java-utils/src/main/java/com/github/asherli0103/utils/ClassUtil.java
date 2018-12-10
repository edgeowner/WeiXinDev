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
import com.github.asherli0103.utils.lang.filter.ClassFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.*;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static com.github.asherli0103.utils.enums.BasicType.primitiveWrapperMap;
import static com.github.asherli0103.utils.enums.BasicType.wrapperPrimitiveMap;


/**
 * 类操作工具类
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
public class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    private static final String DOT = ".";

    private static final String SLASH = "/";

    /**
     * Jar文件扩展名
     */
    private static final String JAR_FILE_EXT = ".jar";
    /**
     * 在Jar中的路径jar的扩展名形式
     */
    private static final String JAR_PATH_EXT = ".jar!";

    /**
     * 当Path为文件形式时, path会加入一个表示文件的前缀
     */
    private static final String PATH_FILE_PRE = "file:";

    /**
     * Class文件扩展名
     */
    private static final String CLASS_EXT = ".class";

    /**
     * 空字符串
     */
    private static final String EMPTY = "";

    /**
     * 文件过滤器，过滤掉不需要的文件<br>
     * 只保留Class文件、目录和Jar
     */
    private static FileFilter fileFilter = pathname -> isClass(pathname.getName()) || pathname.isDirectory() || isJarFile(pathname);

    /**
     * @param file 文件
     * @return 是否为类文件
     */
    private static boolean isClassFile(File file) {
        return isClass(file.getName());
    }

    /**
     * @param fileName 文件名
     * @return 是否为类文件
     */
    private static boolean isClass(String fileName) {
        return fileName.endsWith(CLASS_EXT);
    }

    /**
     * @param file 文件
     * @return 是否为Jar文件
     */
    private static boolean isJarFile(File file) {
        return file.getName().endsWith(JAR_FILE_EXT);
    }

    /**
     * 是否为静态方法
     *
     * @param method 方法
     * @return 是否为静态方法
     */
    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 指定类是否为非public
     *
     * @param clazz 类
     * @return 是否为非public
     */
    public static boolean isNotPublic(Class<?> clazz) {
        return !isPublic(clazz);
    }

    /**
     * 指定方法是否为非public
     *
     * @param method 方法
     * @return 是否为非public
     */
    public static boolean isNotPublic(Method method) {
        return !isPublic(method);
    }

    /**
     * 是否为包装类型
     *
     * @param clazz 类
     * @return 是否为包装类型
     */
    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return null != clazz && wrapperPrimitiveMap.containsKey(clazz);
    }

    /**
     * 是否为基本类型（包括包装类和原始类）
     *
     * @param clazz 类
     * @return 是否为基本类型
     */
    public static boolean isBasicType(Class<?> clazz) {
        return null != clazz && (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * 是否简单值类型或简单值类型的数组<br>
     * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class及其数组
     *
     * @param clazz 属性类
     * @return 是否简单值类型或简单值类型的数组
     */
    public static boolean isSimpleTypeOrArray(Class<?> clazz) {
        return null != clazz && (isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType())));
    }

    /**
     * 是否为简单值类型<br>
     * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class.
     *
     * @param clazz 类
     * @return 是否为简单值类型
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        return isBasicType(clazz) || clazz.isEnum() || CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz) || clazz.equals(URI.class) ||
                clazz.equals(URL.class) || clazz.equals(Locale.class) || clazz.equals(Class.class);
    }

    /**
     * 获得对象数组的类数组
     *
     * @param objects 对象数组
     * @return 类数组
     */
    public static Class<?>[] getClasses(Object[] objects) {
        Class<?> argTypes[] = null;
        if (objects != null) {
            argTypes = new Class<?>[objects.length];
            for (int i = 0; i < objects.length; ++i) {
                if (objects[i] == null) {
                    argTypes[i] = null;
                } else {
                    argTypes[i] = objects[i].getClass();
                }
            }
        }
        return argTypes;
    }

    /**
     * 获得ClassPath
     *
     * @return ClassPath集合
     */
    public static Set<String> getClassPathResources() {
        return getClassPaths(EMPTY);
    }

    /**
     * 获得ClassPath
     *
     * @return ClassPath
     */
    public static String getClassPath() {
        return getClassPathURL().getPath();
    }

    /**
     * 获得ClassPath URL
     *
     * @return ClassPath URL
     */
    public static URL getClassPathURL() {
        return getURL(EMPTY);
    }

    /**
     * 获得资源的URL
     *
     * @param resource 资源（相对Classpath的路径）
     * @return 资源URL
     */
    public static URL getURL(String resource) {
        return ClassUtil.getClassLoader().getResource(resource);
    }

    /**
     * 检查目标类是否可以从原类转化<br>
     * 转化包括：<br>
     * 1、原类是对象，目标类型是原类型实现的接口<br>
     * 2、目标类型是原类型的父类<br>
     * 3、两者是原始类型或者包装类型（相互转换）
     *
     * @param targetType 目标类型
     * @param sourceType 原类型
     * @return 是否可转化
     */
    public static boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
        if (null == targetType || null == sourceType) {
            return false;
        }

        // 对象类型
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }

        // 基本类型
        if (targetType.isPrimitive()) {
            // 原始类型
            Class<?> resolvedPrimitive = wrapperPrimitiveMap.get(sourceType);
            if (resolvedPrimitive != null && targetType.equals(resolvedPrimitive)) {
                return true;
            }
        } else {
            // 包装类型
            Class<?> resolvedWrapper = primitiveWrapperMap.get(sourceType);
            if (resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定方法是否为Public
     *
     * @param method 方法
     * @return 是否为public
     */
    public static boolean isPublic(Method method) {
        if (null == method) {
            throw new NullPointerException("Method to provided is null.");
        }
        return isPublic(method.getDeclaringClass());
    }

    /**
     * 指定类是否为Public
     *
     * @param clazz 类
     * @return 是否为public
     */
    public static boolean isPublic(Class<?> clazz) {
        if (null == clazz) {
            throw new NullPointerException("Class to provided is null.");
        }
        return Modifier.isPublic(clazz.getModifiers());
    }

    /**
     * 获取类路径集合
     *
     * @return 路径集合
     */
    public static Collection<URL> getPathUrls() {
        final Collection<URL> urls = new ArrayList<>(32);
        final String javaClassPath = System.getProperty("java.class.path");
        if (javaClassPath != null) {
            final String[] pathItems = javaClassPath.split(File.pathSeparator);
            for (final String path : pathItems) {
                try {
                    final URL url = new File(path).toURI().toURL();
                    urls.add(normalize(url));
                } catch (final MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        mergePathUrls(Thread.currentThread().getContextClassLoader(), urls);
        mergePathUrls(System.class.getClassLoader(), urls);

        return urls;
    }

    /**
     * 合并路径
     *
     * @param classLoader 类ClassLoader
     * @param urls        路径集合
     */
    public static void mergePathUrls(final ClassLoader classLoader, final Collection<URL> urls) {
        if (!(classLoader instanceof URLClassLoader)) {
            return;
        }
        final URLClassLoader ucl = (URLClassLoader) classLoader;
        final URL urlList[] = ucl.getURLs();
        for (final URL url : urlList) {
            if (urls.contains(url)) {
                continue;
            }
            urls.add(url);
        }
    }

    /**
     * 标准化路径
     *
     * @param url 路径
     * @return 标准路径
     * @throws MalformedURLException 创建异常时熬出
     */
    public static URL normalize(URL url) throws MalformedURLException {
        String spec = url.getFile();
        // get url base - remove everything after ".jar!/??" , if exists
        final int i = spec.indexOf("!/");
        if (i != -1) {
            spec = spec.substring(0, spec.indexOf("!/"));
        }
        // uppercase windows drive
        url = new URL(url, spec);
        final String file = url.getFile();
        final int i1 = file.indexOf(":");
        if (i1 != -1) {
            final String drive = file.substring(i1 - 1, 2).toUpperCase();
            url = new URL(url, file.substring(0, i1 - 1) + drive + file.substring(i1));
        }
        return url;
    }


    public static List<Class<?>> listClassOfPackage(final Class<?> cType, final String extenion) {
        final List<Class<?>> result = new ArrayList<Class<?>>();
        final List<String> cPath = listClassCanonicalNameOfPackage(cType, extenion);
        for (final String path : cPath) {
            try {
                result.add(Class.forName(path, false, Thread.currentThread().getContextClassLoader()));
            } catch (final Exception e) {
                // ignore


            }
        }
        return result;
    }

    public static List<String> listClassCanonicalNameOfPackage(final Class<?> clazz, final String extenion) {
        return listNameOfPackage(clazz, extenion, true);
    }

    public static List<String> listClassNameOfPackage(final Class<?> clazz, final String extenion) {
        return listNameOfPackage(clazz, extenion, false);
    }

    public static List<String> listNameOfPackage(final Class<?> clazz, final String extenion, final boolean fullPkgName) {
        return listNameOfPackage(clazz.getName().replace('.', '/') + ".class", extenion, fullPkgName);
    }

    public static List<String> listNameOfPackage(final String clazzPkg, final String extenion, final boolean fullPkgName) {
        final List<String> result = new ArrayList<>();

        final StringBuilder pkgBuf = new StringBuilder(clazzPkg);

        if (pkgBuf.charAt(0) != '/')
            pkgBuf.insert(0, '/');

        final URL urlPath = ClassUtil.class.getResource(pkgBuf.toString());

        if (null == urlPath)
            return result;

        String checkedExtenion = extenion;
        if (!extenion.endsWith(".class"))
            checkedExtenion = extenion + ".class";

        if (pkgBuf.toString().endsWith(".class"))
            pkgBuf.delete(pkgBuf.lastIndexOf("/"), pkgBuf.length());

        pkgBuf.deleteCharAt(0);

        final StringBuilder fileUrl = new StringBuilder();
        try {
            fileUrl.append(URLDecoder.decode(urlPath.toExternalForm(), "UTF-8"));
        } catch (final UnsupportedEncodingException e1) {
            fileUrl.append(urlPath.toExternalForm());
        }

        if (fileUrl.toString().startsWith("file:")) {
            fileUrl.delete(0, 5);// delete file: flag

            if (fileUrl.indexOf(":") != -1)
                fileUrl.deleteCharAt(0);// delete flag

            final String baseDir = fileUrl.substring(0, fileUrl.lastIndexOf("classes") + 8);
            doListNameOfPackageInDirectory(new File(baseDir), new File(baseDir), result, pkgBuf.toString(), checkedExtenion, fullPkgName);
        } else {
            doListNameOfPackageInJar(urlPath, urlPath, result, pkgBuf.toString(), checkedExtenion, fullPkgName);
        }

        return result;
    }

    /**

     */
    private static void doListNameOfPackageInJar(final URL baseUrl, final URL urlPath, final List<String> result, final String clazzPkg, final String extenion, final boolean fullPkgName) {
        try {
            final JarURLConnection conn = (JarURLConnection) urlPath.openConnection();
            final JarFile jfile = conn.getJarFile();
            final Enumeration<JarEntry> e = jfile.entries();
            ZipEntry entry;
            String entryname;
            while (e.hasMoreElements()) {
                entry = e.nextElement();
                entryname = entry.getName();
                if (entryname.startsWith(clazzPkg) && entryname.endsWith(extenion)) {
                    if (fullPkgName) {
                        result.add(entryname.substring(0, entryname.lastIndexOf('.')).replace('/', '.'));
                    } else {
                        result.add(entryname.substring(entryname.lastIndexOf('/') + 1, entryname.lastIndexOf('.')));
                    }
                }
            }
        } catch (final IOException ignored) {
        }
    }

    private static void doListNameOfPackageInDirectory(final File baseDirectory, final File directory, final List<String> result, final String clazzPkg, final String extenion,
                                                       final boolean fullPkgName) {
        File[] files = directory.listFiles();
        if (null == files)
            files = new File[]{};
        String clazzPath;
        final int baseDirLen = baseDirectory.getAbsolutePath().length() + 1;
        for (final File file : files) {
            if (file.isDirectory()) {
                doListNameOfPackageInDirectory(baseDirectory, file, result, clazzPkg, extenion, fullPkgName);
            } else {
                if (!file.getName().endsWith(extenion))
                    continue;

                if (fullPkgName) {
                    clazzPath = file.getAbsolutePath().substring(baseDirLen);
                    clazzPath = clazzPath.substring(0, clazzPath.length() - 6);
                    result.add(clazzPath.replace(File.separatorChar, '.'));
                } else {
                    result.add(file.getName().substring(0, file.getName().length() - 6));
                }
            }
        }
    }

    public static List<Class<?>> scanPackage(String packageName, boolean isRecursive) {
        List<Class<?>> classList = new ArrayList<>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath();
                        addClass(classList, packagePath, packageName, isRecursive);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            if (jarEntryName.endsWith(".class")) {
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                if (isRecursive || className.substring(0, className.lastIndexOf(".")).equals(packageName)) {
                                    classList.add(Class.forName(className));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    // 获取指定包名下的所有类（可根据注解进行过滤）
    public static List<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> classList = new ArrayList<>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath();
                        addClassByAnnotation(classList, packagePath, packageName, annotationClass);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            if (jarEntryName.endsWith(".class")) {
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                Class<?> cls = Class.forName(className);
                                if (cls.isAnnotationPresent(annotationClass)) {
                                    classList.add(cls);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    private static void addClass(List<Class<?>> classList, String packagePath, String packageName, boolean isRecursive) {
        try {
            File[] files = getClassFiles(packagePath);
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = getClassName(packageName, fileName);
                        classList.add(Class.forName(className));
                    } else {
                        if (isRecursive) {
                            String subPackagePath = getSubPackagePath(packagePath, fileName);
                            String subPackageName = getSubPackageName(packageName, fileName);
                            addClass(classList, subPackagePath, subPackageName, true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File[] getClassFiles(String packagePath) {
        return new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
    }

    private static String getClassName(String packageName, String fileName) {
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        if (!packageName.equals("")) {
            className = packageName + "." + className;
        }
        return className;
    }

    private static String getSubPackagePath(String packagePath, String filePath) {
        String subPackagePath = filePath;
        if (!packagePath.equals("")) {
            subPackagePath = packagePath + "/" + subPackagePath;
        }
        return subPackagePath;
    }

    private static String getSubPackageName(String packageName, String filePath) {
        String subPackageName = filePath;
        if (!packageName.equals("")) {
            subPackageName = packageName + "." + subPackageName;
        }
        return subPackageName;
    }


    private static void addClassByAnnotation(List<Class<?>> classList, String packagePath, String packageName, Class<? extends Annotation> annotationClass) {
        try {
            File[] files = getClassFiles(packagePath);
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = getClassName(packageName, fileName);
                        Class<?> cls = Class.forName(className);
                        if (cls.isAnnotationPresent(annotationClass)) {
                            classList.add(cls);
                        }
                        Field[] fields = cls.getFields();
                        for (Field field : fields) {
                            if (field.isAnnotationPresent(annotationClass)) {
                                classList.add(cls);
                            }
                        }
                    } else {
                        String subPackagePath = getSubPackagePath(packagePath, fileName);
                        String subPackageName = getSubPackageName(packageName, fileName);
                        addClassByAnnotation(classList, subPackagePath, subPackageName, annotationClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 扫面该包路径下所有class文件
     *
     * @return 类集合
     * @throws IOException            exception
     * @throws ClassNotFoundException exception
     */
    public static Set<Class<?>> scanPackage() throws IOException, ClassNotFoundException {
        return scanPackage(EMPTY, null);
    }

    /**
     * 扫面该包路径下所有class文件
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @return 类集合
     * @throws IOException            exception
     * @throws ClassNotFoundException exception
     */
    public static Set<Class<?>> scanPackage(String packageName) throws IOException, ClassNotFoundException {
        return scanPackage(packageName, null);
    }

    /**
     * 扫描指定包路径下所有包含指定注解的类
     *
     * @param packageName     包路径
     * @param annotationClass 注解类
     * @return 类集合
     * @throws IOException            exception
     * @throws ClassNotFoundException exception
     */
    public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) throws IOException, ClassNotFoundException {
        return scanPackage(packageName, clazz -> clazz.isAnnotationPresent(annotationClass));
    }

    /**
     * 扫描指定包路径下所有指定类的子类
     *
     * @param packageName 包路径
     * @param superClass  父类
     * @return 类集合
     * @throws IOException            exception
     * @throws ClassNotFoundException exception
     */
    public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) throws IOException, ClassNotFoundException {
        return scanPackage(packageName, clazz -> superClass.isAssignableFrom(clazz) && !superClass.equals(clazz));
    }

    /**
     * 扫面包路径下满足class过滤器条件的所有class文件，<br>
     * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException<br>
     * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改<br>
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @param classFilter class过滤器，过滤掉不需要的class
     * @return 类集合
     * @throws IOException            exception
     * @throws ClassNotFoundException exception
     */
    public static Set<Class<?>> scanPackage(String packageName, ClassFilter classFilter) throws IOException, ClassNotFoundException {
        if (StringUtil.isBlank(packageName)) {
            packageName = EMPTY;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Scan classes from package [{}]...", packageName);
        }
        packageName = getWellFormedPackageName(packageName);

        final Set<Class<?>> classes = new HashSet<>();
        for (String classPath : getClassPaths(packageName)) {
            // bug修复，由于路径中空格和中文导致的Jar找不到
            classPath = URLDecoder.decode(classPath, Charset.defaultCharset().name());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Scan classpath: [{}]", classPath);
            }
            // 填充 classes
            fillClasses(classPath, packageName, classFilter, classes);
        }

        // 如果在项目的ClassPath中未找到，去系统定义的ClassPath里找
        if (classes.isEmpty()) {
            for (String classPath : getJavaClassPaths()) {
                // bug修复，由于路径中空格和中文导致的Jar找不到
                classPath = URLDecoder.decode(classPath, Charset.defaultCharset().name());

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Scan java classpath: [{}]", classPath);
                }
                // 填充 classes
                fillClasses(classPath, new File(classPath), packageName, classFilter, classes);
            }
        }
        return classes;
    }

    /**
     * 获得ClassPath
     *
     * @param packageName 包名称
     * @return ClassPath路径字符串集合
     */
    public static Set<String> getClassPaths(String packageName) {
        String packagePath = packageName.replace(DOT, SLASH);
        Enumeration<URL> resources;
        try {
            resources = getClassLoader().getResources(packagePath);
        } catch (IOException e) {
            throw new UtilException(MessageFormat.format("Loading classPath [{0}] error!", packagePath), e);
        }
        Set<String> paths = new HashSet<>();
        while (resources.hasMoreElements()) {
            paths.add(resources.nextElement().getPath());
        }
        return paths;
    }

    /**
     * 类路径分割
     *
     * @return 获得Java ClassPath路径，不包括 jre
     */
    public static String[] getJavaClassPaths() {
        return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
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
            classLoader = ClassUtil.class.getClassLoader();
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

    /**
     * 改变 com  →  com. 避免在比较的时候把比如 completeTestSuite.class类扫描进去，如果没有"."<br>
     * 那class里面com开头的class类也会被扫描进去,其实名称后面或前面需要一个 ".",来添加包的特征
     *
     * @param packageName bai
     * @return 格式化后的包名
     */
    private static String getWellFormedPackageName(String packageName) {
        return packageName.lastIndexOf(DOT) != packageName.length() - 1 ? packageName + DOT : packageName;
    }

    /**
     * 填充满足条件的class 填充到 classes<br>
     * 同时会判断给定的路径是否为Jar包内的路径，如果是，则扫描此Jar包
     *
     * @param path        Class文件路径或者所在目录Jar包路径
     * @param packageName 需要扫面的包名
     * @param classFilter class过滤器
     * @param classes     List 集合
     */
    private static void fillClasses(String path, String packageName, ClassFilter classFilter, Set<Class<?>> classes) throws IOException, ClassNotFoundException {
        // 判定给定的路径是否为Jar
        int index = path.lastIndexOf(JAR_PATH_EXT);
        if (index != -1) {
            // Jar文件
            path = path.substring(0, index + JAR_FILE_EXT.length()); // 截取jar路径
            path = path.substring(PATH_FILE_PRE.length()); // 去掉文件前缀
            processJarFile(new File(path), packageName, classFilter, classes);
        } else {
            fillClasses(path, new File(path), packageName, classFilter, classes);
        }
    }

    /**
     * 填充满足条件的class 填充到 classes
     *
     * @param classPath   类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
     * @param file        Class文件或者所在目录Jar包文件
     * @param packageName 需要扫面的包名
     * @param classFilter class过滤器
     * @param classes     List 集合
     */
    private static void fillClasses(String classPath, File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) throws IOException, ClassNotFoundException {
        if (file.isDirectory()) {
            processDirectory(classPath, file, packageName, classFilter, classes);
        } else if (isClassFile(file)) {
            processClassFile(classPath, file, packageName, classFilter, classes);
        } else if (isJarFile(file)) {
            processJarFile(file, packageName, classFilter, classes);
        }
    }

    /**
     * 处理如果为目录的情况,需要递归调用 fillClasses方法
     *
     * @param directory   目录
     * @param packageName 包名
     * @param classFilter 类过滤器
     * @param classes     类集合
     * @param classPath   类目录
     */
    private static void processDirectory(String classPath, File directory, String packageName, ClassFilter classFilter, Set<Class<?>> classes) throws IOException, ClassNotFoundException {
        File[] files = directory.listFiles(fileFilter);
        if (files != null) {
            for (File file : files) {
                fillClasses(classPath, file, packageName, classFilter, classes);
            }
        }
    }

    /**
     * 处理为class文件的情况,填充满足条件的class 到 classes
     *
     * @param classPath   类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
     * @param file        class文件
     * @param packageName 包名
     * @param classFilter 类过滤器
     * @param classes     类集合
     */
    private static void processClassFile(String classPath, File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) throws ClassNotFoundException {
        if (!classPath.endsWith(File.separator)) {
            classPath += File.separator;
        }
        String path = file.getAbsolutePath();
        if (StringUtil.isEmpty(packageName)) {
            path = path.substring(classPath.length());
        }
        final String filePathWithDot = path.replace(File.separator, DOT);

        int subIndex;
        if ((subIndex = filePathWithDot.indexOf(packageName)) != -1) {
            final int endIndex = filePathWithDot.lastIndexOf(CLASS_EXT);
            final String className = filePathWithDot.substring(subIndex, endIndex);
            fillClass(className, packageName, classes, classFilter);
        }
    }

    /**
     * 处理为jar文件的情况，填充满足条件的class 到 classes
     *
     * @param file        jar文件
     * @param packageName 包名
     * @param classFilter 类过滤器
     * @param classes     类集合
     */
    private static void processJarFile(File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) throws IOException {
        Collections.list(new JarFile(file).entries()).stream().filter(entry -> isClass(entry.getName()))
                .forEach(entry -> {
                    final String className = entry.getName().replace(SLASH, DOT).replace(CLASS_EXT, EMPTY);
                    try {
                        fillClass(className, packageName, classes, classFilter);
                    } catch (ClassNotFoundException e) {
                        //TODO 是否应该封装该异常
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 填充class 到 classes
     *
     * @param className   类名
     * @param packageName 包名
     * @param classes     类集合
     * @param classFilter 类过滤器
     */
    private static void fillClass(String className, String packageName, Set<Class<?>> classes, ClassFilter classFilter) throws ClassNotFoundException {
        if (className.startsWith(packageName)) {
            final Class<?> clazz = Class.forName(className, false, getClassLoader());
            if (classFilter == null || classFilter.accept(clazz)) {
                classes.add(clazz);
            }
        }
    }

}
