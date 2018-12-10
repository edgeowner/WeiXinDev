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


import org.apache.oro.text.regex.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class RegexOROUtil {

    private static RegexOROUtil regexOROUtil;

    public static RegexOROUtil getInstance() {
        if (regexOROUtil == null) {
            regexOROUtil = new RegexOROUtil();
        }
        return regexOROUtil;
    }

    /**
     * 大小写敏感的正规表达式批配
     *
     * @param source 批配的源字符串
     * @param regexp 批配的正规表达式
     * @return 如果源字符串符合要求返回真, 否则返回假 如:  Regexp.isHardRegexpValidate("ygj@suncer.com.cn",email_regexp) 返回真
     * @throws MalformedPatternException e
     */
    public static boolean isHardRegexpValidate(String source, String regexp) throws MalformedPatternException {
        try {
            // 用于定义正规表达式对象模板类型
            PatternCompiler compiler = new Perl5Compiler();
            // 正规表达式比较批配对象
            PatternMatcher matcher = new Perl5Matcher();
            // 实例大小大小写敏感的正规表达式模板
            Pattern hardPattern = compiler.compile(regexp);
            // 返回批配结果
            return matcher.contains(source, hardPattern);
        } catch (MalformedPatternException e) {
            throw new MalformedPatternException(e.getMessage());
        }
    }

    /**
     * 不区分大小写的正规表达式批配
     *
     * @param source 批配的源字符串
     * @param regexp 批配的正规表达式
     * @return 如果源字符串符合要求返回真, 否则返回假 Regexp.isHardRegexpValidate("ygj@suncer.com.cn",email_regexp) 返回真
     * @throws MalformedPatternException e
     */
    public static boolean isSoftRegexpValidate(String source, String regexp) throws MalformedPatternException {
        try {
            //用于定义正规表达式对象模板类型
            PatternCompiler compiler = new Perl5Compiler();
            // 正规表达式比较批配对象
            PatternMatcher matcher = new Perl5Matcher();
            // 实例不区分大小写的正规表达式模板
            Pattern softPattern = compiler.compile(regexp, Perl5Compiler.CASE_INSENSITIVE_MASK);
            // 返回批配验证值
            return matcher.contains(source, softPattern);
        } catch (MalformedPatternException e) {
            throw new MalformedPatternException(e.getMessage());
        }
    }

    /**
     * 返回许要的批配结果集(大小写敏感的正规表达式批配)
     *
     * @param source 批配的源字符串
     * @param regexp 批配的正规表达式
     * @return 如果源字符串符合要求返回许要的批配结果集, 否则返回 "null"关键字 <br>
     * 如 : MatchResult matchResult = getHardRegexpMatchResult("http://www.suncer.com:8080/index.html?login=true",Regexp.url_regexp)
     * 得到取出的匹配值为
     * matchResult.group(0)= http://www.suncer.com:8080/index.html?login=true
     * matchResult.group(1) = http
     * matchResult.group(2) = www.suncer.com
     * matchResult.group(3) = :8080
     * matchResult.group(4) = /index.html?login=true
     * @throws MalformedPatternException e
     */
    public static MatchResult getHardRegexpMatchResult(String source, String regexp) throws MalformedPatternException {
        try {
            //用于定义正规表达式对象模板类型
            PatternCompiler compiler = new Perl5Compiler();
            // 正规表达式比较批配对象
            PatternMatcher matcher = new Perl5Matcher();
            // 实例大小大小写敏感的正规表达式模板
            Pattern hardPattern = compiler.compile(regexp);
            // 如果批配结果正确,返回取出的批配结果
            if (matcher.contains(source, hardPattern)) {
                return matcher.getMatch();
            }
        } catch (MalformedPatternException e) {
            throw new MalformedPatternException(e.getMessage());
        }
        return null;
    }

    /**
     * 返回许要的批配结果集(不区分大小写的正规表达式批配)
     *
     * @param source 批配的源字符串
     * @param regexp 批配的正规表达式
     * @return 如果源字符串符合要求返回许要的批配结果集, 否则返回 "null"关键字
     * 如 : MatchResult matchResult = getHardRegexpMatchResult("http://www.suncer.com:8080/index.html?login=true",Regexp.url_regexp)
     * 得到取出的匹配值为
     * matchResult.group(0)= http://www.suncer.com:8080/index.html?login=true
     * matchResult.group(1) = http
     * matchResult.group(2) = www.suncer.com
     * matchResult.group(3) = :8080
     * matchResult.group(4) = /index.html?login=true
     * @throws MalformedPatternException e
     */
    public static MatchResult getSoftRegexpMatchResult(String source, String regexp) throws MalformedPatternException {
        try {
            //用于定义正规表达式对象模板类型
            PatternCompiler compiler = new Perl5Compiler();
            // 正规表达式比较批配对象
            PatternMatcher matcher = new Perl5Matcher();
            // 实例不区分大小写的正规表达式模板
            Pattern softPattern = compiler.compile(regexp,
                    Perl5Compiler.CASE_INSENSITIVE_MASK);
            // 如果批配结果正确,返回取出的批配结果
            if (matcher.contains(source, softPattern)) {
                // MatchResult result=matcher.getMatch();
                return matcher.getMatch();
            }
        } catch (MalformedPatternException e) {
            throw new MalformedPatternException(e.getMessage());
        }
        return null;
    }

    /**
     * 返回许要的批配结果集(大小写敏感的正规表达式批配)
     *
     * @param source 批配的源字符串
     * @param regexp 批配的正规表达式
     * @return 如果源字符串符合要求返回许要的批配结果集,{@link #getHardRegexpMatchResult(String, String) 使用方法参见getHardRegexpMatchResult(String, String)}
     * @throws MalformedPatternException e
     */
    public static ArrayList getHardRegexpArray(String source, String regexp) throws MalformedPatternException {
        List<String> tempList = new ArrayList<>();
        try {
            //用于定义正规表达式对象模板类型
            PatternCompiler compiler = new Perl5Compiler();
            // 实例大小大小写敏感的正规表达式模板
            Pattern hardPattern = compiler.compile(regexp);
            // 正规表达式比较批配对象
            PatternMatcher matcher = new Perl5Matcher();
            // 如果批配结果正确,返回取出的批配结果
            if (matcher.contains(source, hardPattern)) {
                //存放取出值的正规表达式比较批配结果对象
                MatchResult matchResult = matcher.getMatch();
                for (int i = 0; i < matchResult.length() && matchResult.group(i) != null; i++) {
                    tempList.add(i, matchResult.group(i));
                }
            }
        } catch (MalformedPatternException e) {
            throw new MalformedPatternException(e.getMessage());
        }
        return (ArrayList) tempList;
    }

    /**
     * 返回许要的批配结果集(不区分大小写的正规表达式批配)
     *
     * @param source 批配的源字符串
     * @param regexp 批配的正规表达式
     * @return 如果源字符串符合要求返回许要的批配结果集{@link #getHardRegexpMatchResult(String, String) 使用方法参见getHardRegexpMatchResult(String, String)}
     * @throws MalformedPatternException e
     */
    public static ArrayList getSoftRegexpArray(String source, String regexp) throws MalformedPatternException {
        List<String> tempList = new ArrayList<>();
        try {
            //用于定义正规表达式对象模板类型
            PatternCompiler compiler = new Perl5Compiler();
            // 正规表达式比较批配对象
            PatternMatcher matcher = new Perl5Matcher();
            //实例不区分大小写的正规表达式模板
            Pattern softPattern = compiler.compile(regexp, Perl5Compiler.CASE_INSENSITIVE_MASK);
            if (matcher.contains(source, softPattern)) {
                // 如果批配结果正确,返回取出的批配结果
                MatchResult matchResult = matcher.getMatch();
                for (int i = 0; i < matchResult.length() && matchResult.group(i) != null; i++) {
                    tempList.add(i, matchResult.group(i));
                }
            }
        } catch (MalformedPatternException e) {
            throw new MalformedPatternException(e.getMessage());
        }
        return (ArrayList) tempList;
    }

    /**
     * 得到指定分隔符中间的字符串的集合,
     *
     * @param originStr      要提取的源字符串
     * @param leftSeparator  左边的分隔符
     * @param rightSeparator 右边的分隔符
     * @return 指定分隔符中间的字符串的集合
     */
    public static Set<String> getBetweenSeparatorStr(final String originStr, final char leftSeparator, final char rightSeparator) {
        Set<String> variableSet = new TreeSet<>();
        if (originStr == null || originStr.length() == 0) {
            return variableSet;
        }
        String sTempArray[] = originStr.split("(\\" + leftSeparator + ")");
        for (int i = 1; i < sTempArray.length; i++) {
            int endPosition = sTempArray[i].indexOf(rightSeparator);
            String sTempVariable = sTempArray[i].substring(0, endPosition);
            variableSet.add(sTempVariable);
        }
        return variableSet;
    }

}
