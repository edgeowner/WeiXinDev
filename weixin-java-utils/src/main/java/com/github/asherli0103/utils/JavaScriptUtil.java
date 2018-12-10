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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.URL;

/**
 * 用java操作 javascript 工具类。<br>
 * 可以加载网络上的js，或者文件路径下的js，可以调用js文件中的函数。
 *
 * @author AsherLi
 * @version 1.0.00
 */
@SuppressWarnings("unused")
public class JavaScriptUtil {
    //js 文件操作对象。
    private Invocable inv = null;

    /**
     * 功能：构造函数。(文件路径)
     *
     * @param jsFilePaths 文件路径下的js文件全路径，可以同时传入很多js路径。
     * @throws ScriptException       读取js文件异常。
     * @throws FileNotFoundException js文件没有找到。
     */
    public JavaScriptUtil(String... jsFilePaths) throws FileNotFoundException, ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByMimeType("text/javascript");
        for (String filePath : jsFilePaths) {
            engine.eval(new FileReader(filePath));
        }
        inv = (Invocable) engine;
    }

    /**
     * 功能：构造函数。(网络地址)
     *
     * @param jsUrls js文件在网络上的全路径,可以同时传入多个JS的URL。
     * @throws ScriptException 读取js文件异常。
     * @throws IOException     从网路上加载js文件异常。
     */
    public JavaScriptUtil(URL... jsUrls) throws ScriptException, IOException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByMimeType("text/javascript");
        //从网络读取js文件流
        for (URL url : jsUrls) {
            InputStreamReader isr = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(isr);
            engine.eval(br);
        }
        inv = (Invocable) engine;
    }

    /**
     * 功能：将当前页面进行重新加载。（刷新）
     *
     * @return String
     */
    public static String reload() {
        return "<script language=\"javascript\">\nwindow.location.href=window.location.href;\n</script>";
    }

    /**
     * 功能：弹出alert对话框，并在点击确定后跳转到某个url。
     *
     * @param msgInfo alert的内容。
     * @param url     点击alert的确定后跳转到的url。
     * @return String
     */
    public static String showAlert(String msgInfo, String url) {
        StringBuilder str = new StringBuilder();
        str.append("<script language='javascript'>");
        str.append("alert('").append(msgInfo).append("')").append(";");
        if (!"".equals(url) && url != null) {
            str.append("window.location.href='").append(url).append("';");
        }
        str.append("</script>");
        return str.toString();
    }

    /**
     * 功能：弹出alert对话框，并在点击确定后返回上个页面。
     *
     * @param msgInfo alert的内容。
     * @return String
     */
    public static String showAlertAndBack(String msgInfo) {
        return "<script language='javascript'>alert('" + msgInfo + "');history.go(-1);</script>";
    }

    /**
     * 功能：调用js中的顶层程序和函数。
     *
     * @param functionName js顶层程序和函数名。
     * @param args         参数
     * @return 程序或函数所返回的值
     * @throws NoSuchMethodException 如果不存在具有给定名称或匹配参数类型的方法。
     * @throws ScriptException       如果在调用方法期间发生错误。
     */
    public Object invokeFunction(String functionName, Object... args) throws ScriptException, NoSuchMethodException {
        return inv.invokeFunction(functionName, args);
    }

    /**
     * 功能：弹出confirm，当用户选择是时跳转到指定url，否则返回。
     *
     * @param msgInfo confirm信息
     * @param url     点击确定后跳转到的url。
     * @return String
     */
    public String showConfirm(String msgInfo, String url) {
        StringBuilder str = new StringBuilder();
        str.append("<script language='javascript'>");
        str.append("if(confirm('").append(msgInfo).append("'))");
        if (!"".equals(url) && url != null) {
            str.append("window.location.href='").append(url).append("';");
        }
        str.append("else history.go(-1);</script>");
        return str.toString();
    }

    /**
     * 功能：弹出confirm，当用户选择是时跳转到指定url，点击否时跳转到另一个url。
     *
     * @param msgInfo   confirm信息
     * @param urlOK     点击确定后跳转到的url。
     * @param urlCancle 点击取消时跳转到的url。
     * @return String
     */
    public String showConfirm(String msgInfo, String urlOK, String urlCancle) {
        StringBuilder str = new StringBuilder();
        str.append("<script language='javascript'>");
        str.append("if(confirm('").append(msgInfo).append("'))");
        if (!"".equals(urlOK) && urlOK != null) {
            str.append("window.location.href='").append(urlOK).append("';");
        }
        str.append("else window.location.href='").append(urlCancle).append("';</script>");
        return str.toString();
    }

}
