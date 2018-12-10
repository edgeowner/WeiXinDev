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
import com.github.asherli0103.utils.lang.handler.ReaderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class FileUtil {

    /**
     * 当Path为文件形式时, path会加入一个表示文件的前缀
     */
    public static final String PATH_FILE_PRE = "file:";
    public static final String EMPTY = "";
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    private static final char WINDOWS_SEPARATOR = '\\';
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static final char EXTENSION_SEPARATOR = '.';
    /**
     * 在Jar中的路径jar的扩展名形式
     */
    private static final String JAR_PATH_EXT = ".jar!";
    /**
     * Jar文件扩展名
     */
    private static final String JAR_FILE_EXT = ".jar";
    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * Extract the filename from the given path, e.g. "mypath/myfile.txt" -&gt;
     * "myfile.txt".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the extracted filename, or <code>null</code> if none
     */
    public static String getFilename(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(SYSTEM_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    public static String getFilename(File file) {
        if (file.isDirectory()) {
            return "";
        }
        return getFilename(file.getName());
    }

    /**
     * Extract the filename extension from the given path, e.g.
     * "mypath/myfile.txt" -&gt; "txt".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the extracted filename extension, or <code>null</code> if none
     */
    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
    }

    public static String getFilenameExtension(File file) {
        if (file.isDirectory()) {
            return "";
        }
        return getFilenameExtension(file.getName());
    }

    /**
     * Strip the filename extension from the given path, e.g.
     * "mypath/myfile.txt" -&gt; "mypath/myfile".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the path with stripped filename extension, or <code>null</code>
     * if none
     */
    public static String stripFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
    }

    /**
     * Apply the given relative path to the given path, assuming standard Java
     * folder separation (i.e. "/" separators);
     *
     * @param path         the path to start from (usually a full file path)
     * @param relativePath the relative path to apply (relative to the full file path
     *                     above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(SYSTEM_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(String.valueOf(SYSTEM_SEPARATOR))) {
                newPath += SYSTEM_SEPARATOR;
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    /**
     * Determines if Windows file system is in use.
     *
     * @return true if the system is Windows
     */
    public static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }

    /**
     * 目录是否为空
     *
     * @param file 目录
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isEmptyDirectory(File file) {
        if (null == file) {
            return true;
        }
        if (file.isDirectory()) {
            String[] subFiles = file.list();
            if (ArrayUtil.isEmpty(subFiles)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 列出目录文件<br>
     * 给定的绝对路径不能是压缩包中的路径
     *
     * @param path 目录绝对路径或者相对路径
     * @return 文件列表（包含目录）
     */
    public static File[] ls(String path) {
        if (path == null) {
            return null;
        }
        path = getAbsolutePath(path);

        File file = file(path);
        if (file.isDirectory()) {
            return file.listFiles();
        }
        throw new UtilException(MessageFormat.format("Path [{0}] is not directory!", path));
    }

    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param file 当前遍历文件
     * @return list
     */
    public static List<File> loopFiles(File file) {
        return loopFiles(file, null);
    }

    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param file       当前遍历文件
     * @param fileFilter 文件过滤规则对象，选择要保留的文件
     * @return list
     */
    public static List<File> loopFiles(File file, FileFilter fileFilter) {
        List<File> fileList = new ArrayList<>();
        if (file == null) {
            return fileList;
        } else if (!file.exists()) {
            return fileList;
        }

        if (file.isDirectory()) {
            File[] tep = file.listFiles();
            if (tep != null && tep.length > 1) {
                for (File tmp : tep) {
                    fileList.addAll(loopFiles(tmp, fileFilter));
                }
            }
        } else {
            if (fileFilter != null && fileFilter.accept(file)) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    /**
     * 遍历指定文件夹下的文件(可以指定扩展名)
     *
     * @param directory 文件夹
     * @param extName   扩展名
     * @return s
     */
    public static List<File> loopFiles(String directory, String extName) {
        if (StringUtil.isEmpty(directory) || !new File(directory).exists()) {
            LOGGER.error("given no path or not exist!");
            return new ArrayList<>(0);
        }

        List<File> files = new ArrayList<>();
        File file = new File(directory);
        if (file.isDirectory()) {
            //是文件夹
            File[] underFiles = file.listFiles();       //获取文件夹下所有的文件或者文件夹
            for (File uf : underFiles != null ? underFiles : new File[0]) {
                if (!uf.isDirectory()) {
                    LOGGER.debug("scan file '{}'!", uf.getName());
                    addFileToList(files, uf, extName);
                } else {
                    files.addAll(loopFiles(uf.getAbsolutePath(), extName));
                }
            }
        } else {
            LOGGER.debug("scan file '{}'!", file.getName());
            addFileToList(files, file, extName);
        }

        return files;
    }

    /**
     * 获得指定目录下所有文件<br>
     * 不会扫描子目录
     *
     * @param path 相对ClassPath的目录或者绝对路径目录
     * @return 文件路径列表（如果是jar中的文件，则给定类似.jar!/xxx/xxx的路径）
     */
    public static List<String> listFileNames(String path) {
        if (path == null) {
            return null;
        }
        path = getAbsolutePath(path);
        if (!path.endsWith(String.valueOf(SYSTEM_SEPARATOR))) {
            path = path + SYSTEM_SEPARATOR;
        }

        List<String> paths = new ArrayList<>();
        int index = path.lastIndexOf(JAR_PATH_EXT);
        try {
            if (index == -1) {
                // 普通目录路径
                File[] files = ls(path);
                for (File file : files) {
                    if (file.isFile()) {
                        paths.add(file.getName());
                    }
                }
            } else {
                // jar文件中的路径
                index = index + JAR_FILE_EXT.length();
                final String jarPath = path.substring(0, index);
                final String subPath = path.substring(index + 2);
                for (JarEntry entry : Collections.list(new JarFile(jarPath).entries())) {
                    final String name = entry.getName();
                    if (name.startsWith(subPath)) {
                        String nameSuffix = name.substring(subPath.length());
                        if (!nameSuffix.contains(String.valueOf(SYSTEM_SEPARATOR))) {
                            paths.add(nameSuffix);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new UtilException(MessageFormat.format("Can not read file path of [{0}]", path), e);
        }
        return paths;
    }

    /**
     * 创建File对象，自动识别相对或绝对路径，相对路径将自动从ClassPath下寻找
     *
     * @param path 文件路径
     * @return File
     */
    public static File file(String path) {
        if (StringUtil.isBlank(path)) {
            throw new UtilException("File path is blank!");
        }
        return new File(getAbsolutePath(path));
    }

    /**
     * 创建File对象
     *
     * @param parent 父目录
     * @param path   文件路径
     * @return File
     */
    public static File file(String parent, String path) {
        if (StringUtil.isBlank(path)) {
            throw new UtilException("File path is blank!");
        }
        return new File(parent, path);
    }

    /**
     * 创建File对象
     *
     * @param parent 父文件对象
     * @param path   文件路径
     * @return File
     */
    public static File file(File parent, String path) {
        if (StringUtil.isBlank(path)) {
            throw new UtilException("File path is blank!");
        }
        return new File(parent, path);
    }

    /**
     * 创建File对象
     *
     * @param uri 文件URI
     * @return File
     */
    public static File file(URI uri) {
        if (uri == null) {
            throw new UtilException("File uri is null!");
        }
        return new File(uri);
    }

    /**
     * 判断文件是否存在，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果存在返回true
     */
    public static boolean exist(String path) {
        return path != null && file(path).exists();
    }

    /**
     * 判断文件是否存在，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果存在返回true
     */
    public static boolean exist(File file) {
        return file != null && file.exists();
    }

    /**
     * 是否存在匹配文件
     *
     * @param directory 文件夹路径
     * @param regexp    文件夹中所包含文件名的正则表达式
     * @return 如果存在匹配文件返回true
     */
    public static boolean exist(String directory, String regexp) {
        File file = new File(directory);
        if (!file.exists()) {
            return false;
        }

        String[] fileList = file.list();
        if (fileList == null) {
            return false;
        }

        for (String fileName : fileList) {
            if (fileName.matches(regexp)) {
                return true;
            }

        }
        return false;
    }

    /**
     * 指定文件最后修改时间
     *
     * @param file 文件
     * @return 最后修改时间
     */
    public static Date lastModifiedTime(File file) {
        if (!exist(file)) {
            return null;
        }
        return new Date(file.lastModified());
    }

    /**
     * 指定路径文件最后修改时间
     *
     * @param path 路径
     * @return 最后修改时间
     */
    public static Date lastModifiedTime(String path) {
        File file = new File(path);
        if (!exist(file)) {
            return null;
        }
        return new Date(file.lastModified());
    }

    /**
     * 创建文件，如果这个文件存在，直接返回这个文件
     *
     * @param fullFilePath 文件的全路径，使用POSIX风格
     * @return 文件，若路径为null，返回null
     * @throws IOException exception
     */
    public static File touch(String fullFilePath) throws IOException {
        if (fullFilePath == null) {
            return null;
        }
        return touch(file(fullFilePath));
    }

    /**
     * 创建文件，如果这个文件存在，直接返回这个文件
     *
     * @param file 文件对象
     * @return 文件，若路径为null，返回null
     * @throws IOException exception
     */
    public static File touch(File file) throws IOException {
        if (null == file) {
            return null;
        }

        if (!file.exists()) {
            mkParentDirs(file);
            boolean createNewFile = file.createNewFile();
        }
        return file;
    }

    /**
     * 创建所给文件或目录的父目录
     *
     * @param file 文件或目录
     * @return 父目录
     */
    public static File mkParentDirs(File file) {
        final File parentFile = file.getParentFile();
        if (null != parentFile) {
            boolean b = parentFile.mkdirs();
        }
        return parentFile;
    }

    /**
     * 功能：删除文件
     *
     * @param filePath 文件
     * @return exception
     * @throws FileNotFoundException exception
     */
    public static boolean delete(String filePath) throws FileNotFoundException {
        if (StringUtil.isBlank(filePath)) {
            throw new FileNotFoundException("文件不存在");
        }
        if (isDirectory(filePath) || isFile(filePath)) {
            return delete(new File(filePath));
        }
        return false;
    }

    /**
     * 功能：删除文件，内部递归使用
     *
     * @param file 文件
     * @return exception
     */
    public static boolean delete(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        //单文件
        if (!file.isDirectory()) {
            boolean delFlag = file.delete();
            if (!delFlag) {
                throw new RuntimeException(file.getPath() + "删除失败！");
            } else {
                return true;
            }
        }
        //删除子目录
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File child : files) {
                delete(child);
            }
        }
        //删除自己
        return file.delete();
    }

    /**
     * 创建文件夹，如果存在直接返回此文件夹
     *
     * @param dirPath 文件夹路径，使用POSIX格式，无论哪个平台
     * @return 创建的目录
     */
    public static File mkdir(String dirPath) {
        if (dirPath == null) {
            return null;
        }
        File dir = file(dirPath);
        if (!dir.exists()) {
            //该处未判断是否创建成功
            boolean isDirectoryCreated = dir.mkdirs();
        }

        return dir;
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].tmp
     *
     * @param dir 临时文件创建的所在目录
     * @return 临时文件
     * @throws IOException exception
     */
    public static File createTempFile(File dir) throws IOException {
        return createTempFile("hutool", null, dir, true);
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].tmp
     *
     * @param dir       临时文件创建的所在目录
     * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
     * @return 临时文件
     * @throws IOException exception
     */
    public static File createTempFile(File dir, boolean isReCreat) throws IOException {
        return createTempFile("hutool", null, dir, isReCreat);
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].suffix From com.jodd.io.FileUtil
     *
     * @param prefix    前缀，至少3个字符
     * @param suffix    后缀，如果null则使用默认.tmp
     * @param dir       临时文件创建的所在目录
     * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
     * @return 临时文件
     * @throws IOException exception
     */
    public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat) throws IOException {
        int exceptionsCount = 0;
        while (true) {
            try {
                File file = File.createTempFile(prefix, suffix, dir).getCanonicalFile();
                if (isReCreat) {
                    //该处未判断
                    boolean isDelete = file.delete();
                    boolean isCreateNewFile = file.createNewFile();
                }
                return file;
            } catch (IOException ioex) { // fixes java.io.WinNTFileSystem.createFileExclusively access denied
                if (++exceptionsCount >= 50) {
                    throw ioex;
                }
            }
        }
    }

    /**
     * 复制文件或目录<br>
     * 如果目标文件为目录，则将源文件以相同文件名拷贝到目标目录
     *
     * @param srcPath    源文件或目录
     * @param destPath   目标文件或目录
     * @param isOverride 是否覆盖目标文件
     * @return 目标目录或文件
     * @throws IOException exception
     */
    public static File copy(String srcPath, String destPath, boolean isOverride) throws IOException {
        return copy(file(srcPath), file(destPath), isOverride);
    }

    /**
     * 复制文件或目录<br>
     * 情况如下：<br>
     * 1、src和dest都为目录，则讲src下所有文件目录拷贝到dest下<br>
     * 2、src和dest都为文件，直接复制，名字为dest<br>
     * 3、src为文件，dest为目录，将src拷贝到dest目录下<br>
     *
     * @param src        源文件
     * @param dest       目标文件或目录
     * @param isOverride 是否覆盖目标文件
     * @return 目标目录或文件
     * @throws IOException exception
     */
    public static File copy(File src, File dest, boolean isOverride) throws IOException {
        // check
        if (!src.exists()) {
            throw new FileNotFoundException("File not exist: " + src.getName());
        }
        if (equals(src, dest)) {
            throw new IOException("Files '" + src.getName() + "' and '" + dest.getName() + "' are equal");
        }

        // 复制目录
        if (src.isDirectory()) {
            if (dest.isFile()) {
                throw new UtilException(MessageFormat.format("Src [{0}] is a directory but Dest [{1}] is a file!", src.getPath(), dest.getPath()));
            }

            if (!dest.exists()) {
                boolean b = dest.mkdirs();
            }
            String files[] = src.list();
            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(src, file);
                    File destFile = new File(dest, file);
                    // 递归复制
                    copy(srcFile, destFile, isOverride);
                }
            }
            return dest;
        }

        // 检查目标
        if (dest.exists()) {
            if (dest.isDirectory()) {
                dest = new File(dest, src.getName());
            }
            if (!isOverride) {
                // 不覆盖，直接跳过
                LOGGER.debug("File [{}] already exist", dest);
                return dest;
            }
        } else {
            touch(dest);
        }

        // do copy file
        FileInputStream input = new FileInputStream(src);
        FileOutputStream output = new FileOutputStream(dest);
        try {
            IOUtil.copy(input, output);
        } finally {
            IOUtil.close(output);
            IOUtil.close(input);
        }

        if (src.length() != dest.length()) {
            throw new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes");
        }

        return dest;
    }

    /**
     * 功能：保存文件。
     *
     * @param content 字节
     * @param file    保存到的文件
     * @throws IOException e
     */
    public static void save(byte[] content, File file) throws IOException {
        if (file == null) {
            throw new RuntimeException("保存文件不能为空");
        }
        if (content == null) {
            throw new RuntimeException("文件流不能为空");
        }
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(content);
            save(is, file);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 功能：保存文件
     *
     * @param streamIn 文件流
     * @param file     保存到的文件
     * @throws IOException e
     */
    public static void save(InputStream streamIn, File file) throws IOException {
        if (file == null) {
            throw new RuntimeException("保存文件不能为空");
        }
        if (streamIn == null) {
            throw new RuntimeException("文件流不能为空");
        }
        //输出流
        OutputStream streamOut;
        //文件夹不存在就创建。
        if (!file.getParentFile().exists()) {
            boolean b = file.getParentFile().mkdirs();
        }
        streamOut = new FileOutputStream(file);
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = streamIn.read(buffer, 0, 8192)) != -1) {
            streamOut.write(buffer, 0, bytesRead);
        }
        streamOut.close();
        streamIn.close();
    }

    /**
     * 检查两个文件是否是同一个文件
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 是否相同
     */
    public static boolean equals(File file1, File file2) {
        try {
            file1 = file1.getCanonicalFile();
            file2 = file2.getCanonicalFile();
        } catch (IOException ignore) {
            return false;
        }
        return file1.equals(file2);
    }

    /**
     * 移动文件或者目录
     *
     * @param src        源文件或者目录
     * @param dest       目标文件或者目录
     * @param isOverride 是否覆盖目标
     * @throws IOException exception
     */
    public static void move(File src, File dest, boolean isOverride) throws IOException {
        // check
        if (!src.exists()) {
            throw new FileNotFoundException("File already exist: " + src);
        }
        if (dest.exists()) {
            if (isOverride) {
                boolean b = dest.delete();
            } else {
                LOGGER.debug("File [{}] already exist", dest);
            }
        }

        // 来源为文件夹，目标为文件
        if (src.isDirectory() && dest.isFile()) {
            throw new IOException(MessageFormat.format("Can not move directory [{0}] to file [{1}]", src, dest));
        }

        // 来源为文件，目标为文件夹
        if (src.isFile() && dest.isDirectory()) {
            dest = new File(dest, src.getName());
        }

        if (!src.renameTo(dest)) {
            // 在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
            try {
                copy(src, dest, isOverride);
                boolean b = src.delete();
            } catch (Exception e) {
                throw new IOException(MessageFormat.format("Move [{0}] to [{1}] failed!", src, dest), e);
            }

        }
    }

    /**
     * 获取绝对路径<br>
     * 此方法不会判定给定路径是否有效（文件或目录存在）
     *
     * @param path      相对路径
     * @param baseClass 相对路径所相对的类
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path, Class<?> baseClass) {
        if (path == null) {
            path = EMPTY;
        }
        if (baseClass == null) {
            return getAbsolutePath(path);
        }
        return baseClass.getResource(path).getPath().substring(PATH_FILE_PRE.length());
    }

    /**
     * 获取绝对路径，相对于classes的根目录<br>
     * 如果给定就是绝对路径，则返回原路径，原路径把所有\替换为/
     *
     * @param path 相对路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path) {
        if (path == null) {
            path = EMPTY;
        } else {
            path = normalize(path);

            if (path.startsWith("/") || path.matches("^[a-zA-Z]:/.*")) {
                // 给定的路径已经是绝对路径了
                return path;
            }
        }

        // 相对路径
        ClassLoader classLoader = ClassUtil.getClassLoader();
        URL url = classLoader.getResource(path);
        return url != null ? url.getPath() : ClassUtil.getClassPath() + path;
    }

    /**
     * 获取标准的绝对路径
     *
     * @param file 文件
     * @return 绝对路径
     */
    public static String getAbsolutePath(File file) {
        if (file == null) {
            return null;
        }

        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    /**
     * 判断是否为目录，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果为目录true
     */
    public static boolean isDirectory(String path) {
        return path != null && file(path).isDirectory();
    }

    /**
     * 判断是否为目录，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果为目录true
     */
    public static boolean isDirectory(File file) {
        return file != null && file.isDirectory();
    }

    /**
     * 判断是否为文件，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果为文件true
     */
    public static boolean isFile(String path) {
        return path != null && !file(path).isDirectory();
    }

    /**
     * 判断是否为文件，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果为文件true
     */
    public static boolean isFile(File file) {
        return file != null && file.isDirectory();
    }

    /**
     * 获得最后一个文件路径分隔符的位置
     *
     * @param filePath 文件路径
     * @return 最后一个文件路径分隔符的位置
     */
    public static int indexOfLastSeparator(String filePath) {
        if (filePath == null) {
            return -1;
        }
        int lastUnixPos = filePath.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filePath.lastIndexOf(WINDOWS_SEPARATOR);
        return (lastUnixPos >= lastWindowsPos) ? lastUnixPos : lastWindowsPos;
    }

    /**
     * 判断文件是否被改动<br>
     * 如果文件对象为 null 或者文件不存在，被视为改动
     *
     * @param file           文件对象
     * @param lastModifyTime 上次的改动时间
     * @return 是否被改动
     */
    public static boolean isModifed(File file, long lastModifyTime) {
        return null == file || !file.exists() || file.lastModified() != lastModifyTime;
    }

    /**
     * 修复路径<br>
     * 1. 统一用 / <br>
     * 2. 多个 / 转换为一个
     *
     * @param path 原路径
     * @return 修复后的路径
     */
    public static String normalize(String path) {
        return path.replaceAll("[/\\\\]{1,}", "/");
    }

    /**
     * 获得相对子路径
     *
     * @param rootDir  绝对父路径
     * @param filePath 文件路径
     * @return 相对子路径
     */
    public static String subPath(String rootDir, String filePath) {
        return subPath(rootDir, file(filePath));
    }

    /**
     * 获得相对子路径
     *
     * @param rootDir 绝对父路径
     * @param file    文件
     * @return 相对子路径
     */
    public static String subPath(String rootDir, File file) {
        String subPath;
        try {
            subPath = file.getCanonicalPath();
        } catch (IOException e) {
            throw new UtilException(e);
        }

        if (StringUtil.isNotEmpty(rootDir) && StringUtil.isNotEmpty(subPath)) {
            rootDir = normalize(rootDir);
            subPath = normalize(subPath);

            if (subPath != null && subPath.toLowerCase().startsWith(subPath.toLowerCase())) {
                subPath = subPath.substring(rootDir.length() + 1);
            }
        }
        return subPath;
    }

    /**
     * 获得输入流
     *
     * @param file 文件
     * @return 输入流
     * @throws FileNotFoundException exception
     */
    public static BufferedInputStream getInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * 获得输入流
     *
     * @param path 文件路径
     * @return 输入流
     * @throws FileNotFoundException exception
     */
    public static BufferedInputStream getInputStream(String path) throws FileNotFoundException {
        return getInputStream(file(path));
    }

    /**
     * 获得一个文件读取器
     *
     * @param file 文件
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedReader getUtf8Reader(File file) throws IOException {
        return getReader(file, Charset.forName("UTF-8"));
    }

    /**
     * 获得一个文件读取器
     *
     * @param path 文件路径
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedReader getUtf8Reader(String path) throws IOException {
        return getReader(path, Charset.forName("UTF-8"));
    }

    /**
     * 获得一个文件读取器
     *
     * @param file        文件
     * @param charsetName 字符集
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedReader getReader(File file, String charsetName) throws IOException {
        return IOUtil.getReader(getInputStream(file), charsetName);
    }

    /**
     * 获得一个文件读取器
     *
     * @param file    文件
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedReader getReader(File file, Charset charset) throws IOException {
        return IOUtil.getReader(getInputStream(file), charset);
    }

    /**
     * 获得一个文件读取器
     *
     * @param path        绝对路径
     * @param charsetName 字符集
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedReader getReader(String path, String charsetName) throws IOException {
        return getReader(file(path), charsetName);
    }

    /**
     * 获得一个文件读取器
     *
     * @param path    绝对路径
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedReader getReader(String path, Charset charset) throws IOException {
        return getReader(file(path), charset);
    }

    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过Integer.MAX_VALUE
     *
     * @param file 文件
     * @return 字节码
     * @throws IOException exception
     */
    public static byte[] readBytes(File file) throws IOException {
        // check
        if (!file.exists()) {
            throw new FileNotFoundException("File not exist: " + file);
        }
        if (!file.isFile()) {
            throw new IOException("Not a file:" + file);
        }

        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new IOException("File is larger then max array size");
        }

        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            int i = in.read(bytes);
        } finally {
            IOUtil.close(in);
        }

        return bytes;
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return 内容
     * @throws IOException exception
     */
    public static String readUtf8String(File file) throws IOException {
        return readString(file, Charset.forName("UTF-8"));
    }

    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @return 内容
     * @throws IOException exception
     */
    public static String readUtf8String(String path) throws IOException {
        return readString(path, Charset.forName("UTF-8"));
    }

    /**
     * 读取文件内容
     *
     * @param file        文件
     * @param charsetName 字符集
     * @return 内容
     * @throws IOException exception
     */
    public static String readString(File file, String charsetName) throws IOException {
        return new String(readBytes(file), charsetName);
    }

    /**
     * 读取文件内容
     *
     * @param file    文件
     * @param charset 字符集
     * @return 内容
     * @throws IOException exception
     */
    public static String readString(File file, Charset charset) throws IOException {
        return new String(readBytes(file), charset);
    }

    /**
     * 读取文件内容
     *
     * @param path        文件路径
     * @param charsetName 字符集
     * @return 内容
     * @throws IOException exception
     */
    public static String readString(String path, String charsetName) throws IOException {
        return readString(file(path), charsetName);
    }

    /**
     * 读取文件内容
     *
     * @param path    文件路径
     * @param charset 字符集
     * @return 内容
     * @throws IOException exception
     */
    public static String readString(String path, Charset charset) throws IOException {
        return readString(file(path), charset);
    }

    /**
     * 读取文件内容
     *
     * @param url     文件URL
     * @param charset 字符集
     * @return 内容
     * @throws IOException exception
     */
    public static String readString(URL url, String charset) throws IOException {
        if (url == null) {
            throw new RuntimeException("Empty url provided!");
        }

        InputStream in = null;
        try {
            in = url.openStream();
            return IOUtil.read(in, charset);
        } finally {
            IOUtil.close(in);
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param <T>        t
     * @param path       文件路径
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IOException exception
     */
    public static <T extends Collection<String>> T readLines(String path, String charset, T collection) throws IOException {
        return readLines(file(path), charset, collection);
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param <T>        t
     * @param file       文件路径
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IOException exception
     */
    public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IOException {
        BufferedReader reader = null;
        try {
            reader = getReader(file, charset);
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) break;
                collection.add(line);
            }
            return collection;
        } finally {
            IOUtil.close(reader);
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param <T>        t
     * @param url        文件的URL
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IOException exception
     */
    public static <T extends Collection<String>> T readLines(URL url, String charset, T collection) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            return IOUtil.readLines(in, charset, collection);
        } finally {
            IOUtil.close(in);
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param url     文件的URL
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IOException exception
     */
    public static List<String> readLines(URL url, String charset) throws IOException {
        return readLines(url, charset, new ArrayList<>());
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param path    文件路径
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IOException exception
     */
    public static List<String> readLines(String path, String charset) throws IOException {
        return readLines(path, charset, new ArrayList<>());
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param file    文件
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IOException exception
     */
    public static List<String> readLines(File file, String charset) throws IOException {
        return readLines(file, charset, new ArrayList<>());
    }

    public static List<String> readLinesApache(File file, Charset encoding) throws IOException {
        InputStream in = null;
        try {
            in = inputStream(file);
            return IOUtil.readLines(in, EncodeUtil.toCharset(encoding));
        } finally {
            IOUtil.closeQuietly(in);
        }
    }

    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * An exception is thrown if the file does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be read.
     *
     * @param file the file to open for input, must not be {@code null}
     * @return a new {@link FileInputStream} for the specified file
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException           if the file object is a directory
     * @throws IOException           if the file cannot be read
     * @since 1.3
     */
    public static FileInputStream inputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    public static List<String> readLinesApache(File file, String encoding) throws IOException {
        return readLinesApache(file, EncodeUtil.toCharset(encoding));
    }

    public static List<String> readLines(File file) throws IOException {
        return readLinesApache(file, Charset.defaultCharset());
    }

    /**
     * 按照给定的readerHandler读取文件中的数据
     *
     * @param <T>           t
     * @param readerHandler Reader处理类
     * @param path          文件的绝对路径
     * @param charset       字符集
     * @return 从文件中load出的数据
     * @throws IOException exception
     */
    public static <T> T load(ReaderHandler<T> readerHandler, String path, String charset) throws IOException {
        BufferedReader reader = null;
        T result = null;
        try {
            reader = getReader(path, charset);
            result = readerHandler.handle(reader);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            IOUtil.close(reader);
        }
        return result;
    }

    /**
     * 获得一个输出流对象
     *
     * @param file 文件
     * @return 输出流对象
     * @throws IOException exception
     */
    public static BufferedOutputStream getOutputStream(File file) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    /**
     * 获得一个输出流对象
     *
     * @param path 输出到的文件路径，绝对路径
     * @return 输出流对象
     * @throws IOException exception
     */
    public static BufferedOutputStream getOutputStream(String path) throws IOException {
        return getOutputStream(touch(path));
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param path        输出路径，绝对路径
     * @param charsetName 字符集
     * @param isAppend    是否追加
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedWriter getWriter(String path, String charsetName, boolean isAppend) throws IOException {
        return getWriter(touch(path), Charset.forName(charsetName), isAppend);
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param path     输出路径，绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedWriter getWriter(String path, Charset charset, boolean isAppend) throws IOException {
        return getWriter(touch(path), charset, isAppend);
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param file        输出文件
     * @param charsetName 字符集
     * @param isAppend    是否追加
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedWriter getWriter(File file, String charsetName, boolean isAppend) throws IOException {
        return getWriter(file, Charset.forName(charsetName), isAppend);
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param file     输出文件
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IOException exception
     */
    public static BufferedWriter getWriter(File file, Charset charset, boolean isAppend) throws IOException {
        if (!file.exists()) {
            boolean b = file.createNewFile();
        }
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), charset));
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param path     输出路径，绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return 打印对象
     * @throws IOException exception
     */
    public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend) throws IOException {
        return new PrintWriter(getWriter(path, charset, isAppend));
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param file     文件
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return 打印对象
     * @throws IOException exception
     */
    public static PrintWriter getPrintWriter(File file, String charset, boolean isAppend) throws IOException {
        return new PrintWriter(getWriter(file, charset, isAppend));
    }

    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @return 写入的文件
     * @throws IOException exception
     */
    public static File writeUtf8String(String content, String path) throws IOException {
        return writeString(content, path, "UTF-8");
    }

    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content 写入的内容
     * @param file    文件
     * @return 写入的文件
     * @throws IOException exception
     */
    public static File writeUtf8String(String content, File file) throws IOException {
        return writeString(content, file, "UTF-8");
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IOException exception
     */
    public static File writeString(String content, String path, String charset) throws IOException {
        return writeString(content, touch(path), charset);
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param file    文件
     * @param charset 字符集
     * @return d
     * @throws IOException exception
     */
    public static File writeString(String content, File file, String charset) throws IOException {
        PrintWriter writer = null;
        try {
            writer = getPrintWriter(file, charset, false);
            writer.print(content);
            writer.flush();
        } finally {
            IOUtil.close(writer);
        }
        return file;
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IOException exception
     */
    public static File appendString(String content, String path, String charset) throws IOException {
        return appendString(content, touch(path), charset);
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param file    文件
     * @param charset 字符集
     * @return 写入的文件
     * @throws IOException exception
     */
    public static File appendString(String content, File file, String charset) throws IOException {
        PrintWriter writer = null;
        try {
            writer = getPrintWriter(file, charset, true);
            writer.print(content);
            writer.flush();
        } finally {
            IOUtil.close(writer);
        }
        return file;
    }

    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T>     t
     * @param list    列表
     * @param path    绝对路径
     * @param charset 字符集
     * @throws IOException exception
     */
    public static <T> void writeLines(Collection<T> list, String path, String charset) throws IOException {
        writeLines(list, path, charset, false);
    }

    /**
     * 将列表写入文件，追加模式
     *
     * @param <T>     t
     * @param list    列表
     * @param path    绝对路径
     * @param charset 字符集
     * @throws IOException exception
     */
    public static <T> void appendLines(Collection<T> list, String path, String charset) throws IOException {
        writeLines(list, path, charset, true);
    }

    /**
     * 将列表写入文件
     *
     * @param <T>      t
     * @param list     列表
     * @param path     绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @throws IOException exception
     */
    public static <T> void writeLines(Collection<T> list, String path, String charset, boolean isAppend) throws IOException {
        PrintWriter writer = null;
        try {
            writer = getPrintWriter(path, charset, isAppend);
            for (T t : list) {
                if (t != null) {
                    writer.println(t.toString());
                    writer.flush();
                }
            }
        } finally {
            IOUtil.close(writer);
        }
    }

    /**
     * 写数据到文件中
     *
     * @param data 数据
     * @param path 目标文件
     * @return exception
     * @throws IOException exception
     */
    public static File writeBytes(byte[] data, String path) throws IOException {
        return writeBytes(data, touch(path));
    }

    /**
     * 写数据到文件中
     *
     * @param dest 目标文件
     * @param data 数据
     * @return dest
     * @throws IOException exception
     */
    public static File writeBytes(byte[] data, File dest) throws IOException {
        return writeBytes(data, dest, 0, data.length, false);
    }

    /**
     * 写入数据到文件
     *
     * @param data   数据
     * @param dest   目标文件
     * @param off    exception
     * @param len    exception
     * @param append exception
     * @return dest
     * @throws IOException exception
     */
    public static File writeBytes(byte[] data, File dest, int off, int len, boolean append) throws IOException {
        if (dest.exists()) {
            if (!dest.isFile()) {
                throw new IOException("Not a file: " + dest);
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest, append);
            out.write(data, off, len);
            out.flush();
        } finally {
            IOUtil.close(out);
        }
        return dest;
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param dest 目标文件
     * @param in   输入流
     * @return dest
     * @throws IOException exception
     */
    public static File writeFromStream(InputStream in, File dest) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest);
            IOUtil.copy(in, out);
        } finally {
            IOUtil.close(out);
        }
        return dest;
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param in           输入流
     * @param fullFilePath 文件绝对路径
     * @return dest
     * @throws IOException exception
     */
    public static File writeFromStream(InputStream in, String fullFilePath) throws IOException {
        return writeFromStream(in, touch(fullFilePath));
    }

    /**
     * 将文件写入流中
     *
     * @param file 文件
     * @param out  流
     * @throws IOException exception
     */
    public static void writeToStream(File file, OutputStream out) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            IOUtil.copy(in, out);
        } finally {
            IOUtil.close(in);
        }
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param fullFilePath 文件绝对路径
     * @param out          输出流
     * @throws IOException exception
     */
    public static void writeToStream(String fullFilePath, OutputStream out) throws IOException {
        writeToStream(touch(fullFilePath), out);
    }

    /**
     * 可读的文件大小
     *
     * @param file 文件
     * @return 大小
     */
    public static String readableFileSize(File file) {
        return readableFileSize(file.length());
    }

    /**
     * 可读的文件大小<br>
     * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
     *
     * @param size Long类型大小
     * @return 大小
     */
    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB", "EB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 重命名文件夹
     *
     * @param oldName 旧文件夹名
     * @param newName 新文件夹名
     * @param path    文件夹路径
     * @return boolean
     */
    public static boolean renameFolder(String oldName, String newName, String path) {
        File oldFile = new File(path + SYSTEM_SEPARATOR + oldName);
        return oldFile.renameTo(new File(path + SYSTEM_SEPARATOR + newName));
    }

    /**
     * 获取文件夹的大小
     *
     * @param file 文件夹
     * @return s
     */
    public static Long getDirectorySize(File file) {
        if (!file.exists()) {
            LOGGER.warn("given file '{}' is null!", file);
            return 0L;
        }
        Long size = 0L;
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (File file1 : files != null ? files : new File[0]) {
                if (file1.isDirectory()) {
                    size = size + getDirectorySize(file1);
                } else {
                    size += getFileSize(file1);
                }
            }
        } else {
            size += file.length();
        }

        return size;
    }

    /**
     * 获取文件夹的大小
     *
     * @param filePath 文件夹路径
     * @return s
     */
    public static Long getDirectorySize(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            LOGGER.error("given file path is null");
            return (long) -1;
        }
        File file = new File(filePath);

        return getDirectorySize(file);
    }

    /**
     * 获取单个文件的大小
     *
     * @param file 要取得文件的大小
     * @return 文件大小，单位字节
     */
    public static Long getFileSize(File file) {
        if (!file.exists() || file.isDirectory()) {
            LOGGER.warn("this file is not exists or it is not a file! file name is '{}'", file.getName());
            return 0L;
        }
        return file.length();
    }

    /**
     * 将给定的文件放入list中(按扩展名)
     *
     * @param files   放文件的list
     * @param file    指定的文件
     * @param extName 扩展名
     */
    private static void addFileToList(List<File> files, File file, String extName) {
        if (StringUtil.isEmpty(extName)) {
            files.add(file);
        } else if (StringUtil.isNotEmpty(extName) && file.getName().endsWith(extName)) {
            files.add(file);
        }
    }

    /**
     * 功能：将图片文件转化为字节数组字符串，并对其进行Base64编码处理。
     *
     * @param imgFilePath 图片在磁盘的路径
     * @return String 如果读取或者编码文件异常，则返回null。
     */
    public static String base64Encode(String imgFilePath) {
        byte[] data;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        // 对字节数组Base64编码
        return new BASE64Encoder().encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * 功能：对字节数组字符串进行Base64解码并生成图片.
     *
     * @param byteStr  被编码后的base64字符串。
     * @param filePath 解码后并生成文件路径
     * @return boolean 是否成功解码并生成图片，成功true，否则false。
     */
    public static boolean base64Decode(String byteStr, String filePath) {
        if (byteStr == null) {
            // 图像数据为空
            return false;
        }
        try {
            // Base64解码
            byte[] bytes = new BASE64Decoder().decodeBuffer(byteStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(filePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 功能：对字节数组字符串进行Base64解码并生成图片.
     *
     * @param imgStr 被编码后的base64字符串。
     * @return byte[] 图片二进制数据,转换异常返回null。
     */
    public static byte[] base64Decode(String imgStr) {
        if (imgStr == null) {// 图像数据为空
            return null;
        }
        try {
            // Base64解码
            byte[] bytes = new BASE64Decoder().decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            return bytes;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

}
