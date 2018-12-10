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


import com.github.asherli0103.utils.lang.date.DateComparator;
import com.github.asherli0103.utils.lang.date.DateFormatStyle;
import com.github.asherli0103.utils.lang.date.DateLunarCalendar;
import com.github.asherli0103.utils.lang.date.DateWeek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.github.asherli0103.utils.constant.Constant.NORM_DATETIME_PATTERN;
import static com.github.asherli0103.utils.constant.Constant.NORM_DATE_PATTERN;


/**
 * 日期操作工具类
 *
 * @author Asherli
 * @version 1.0.00
 */
@SuppressWarnings(value = "unused")
public class DateUtil {

    /**
     * 日志记录
     */
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 线程锁对象
     */
    private static final Object object = new Object();

    /**
     * SimpleDateFormat 线程,解决线程安全问题
     */
    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();

    /**
     * 获取SimpleDateFormat对象, 使用synchronized (object)解决线程安全问题
     *
     * @param pattern 日期格式 
     * @return SimpleDateFormat对象
     */
    private static SimpleDateFormat dateFormat(String pattern) {
        SimpleDateFormat dateFormat = threadLocal.get();
        synchronized (object) {
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat(pattern);
                //false 严格解析字符格式
                dateFormat.setLenient(false);
                threadLocal.set(dateFormat);
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    /**
     * 获取精确日期,根据Long型时间戳数组转换精确日期,错误返回null
     *
     * @param timestamps Long型时间数组
     * @return Date类型日期
     */
    private static Date accurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0;
        Map<Long, long[]> map = new HashMap<>();
        List<Long> absoluteValues = new ArrayList<>();
        if ((timestamps != null) && (timestamps.size() > 0)) {
            if (timestamps.size() > 1) {
                for (int i = 0; i < timestamps.size(); i++) {
                    for (int j = i + 1; j < timestamps.size(); j++) {
                        long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = {timestamps.get(i), timestamps.get(j)};
                        map.put(absoluteValue, timestampTmp);
                    }
                }
                // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。
                // 此时minAbsoluteValue为0因此不能将minAbsoluteValue取默认值0
                long minAbsoluteValue = -1;
                if (!absoluteValues.isEmpty()) {
                    minAbsoluteValue = absoluteValues.get(0);
                    for (int i = 1; i < absoluteValues.size(); i++) {
                        if (minAbsoluteValue > absoluteValues.get(i)) {
                            minAbsoluteValue = absoluteValues.get(i);
                        }
                    }
                }
                if (minAbsoluteValue != -1) {
                    long[] timestampsLastTmp = map.get(minAbsoluteValue);
                    long dateOne = timestampsLastTmp[0];
                    long dateTwo = timestampsLastTmp[1];
                    if (absoluteValues.size() > 1) {
                        timestamp = (Math.abs(dateOne) > Math.abs(dateTwo)) ? dateOne : dateTwo;
                    }
                }
            } else {
                timestamp = timestamps.get(0);
            }
        }
        if (timestamp != 0) {
            date = new Date(timestamp);
        }
        return date;
    }

    /**
     * 获取日期风格样式,根据字符串日期,自动判断对应的日期风格样式,错误返回null
     *
     * @param date 字符串日期 
     * @return DateFormatStyle枚举日期风格样式
     */
    private static DateFormatStyle getDateStyle(String date) {
        DateFormatStyle dateStyle = null;
        Map<Long, DateFormatStyle> map = new HashMap<>();
        List<Long> timestamps = new ArrayList<>();
        for (DateFormatStyle style : DateFormatStyle.values()) {
            if (style.isShowOnly()) {
                continue;
            }
            Date dateTmp = null;
            if (date != null) {
                //将日期字符串从第一位开始解析为Date
                ParsePosition pos = new ParsePosition(0);
                dateTmp = dateFormat(style.getValue()).parse(date, pos);
                if (pos.getIndex() != date.length()) {
                    dateTmp = null;
                }
            }
            if (dateTmp != null) {
                timestamps.add(dateTmp.getTime());
                map.put(dateTmp.getTime(), style);
            }
        }
        Date accurateDate = accurateDate(timestamps);
        if (accurateDate != null) {
            dateStyle = map.get(accurateDate.getTime());
        }
        return dateStyle;
    }

    /**
     * 获取日期中的指定数据,年\月\日\时\分\秒,返回int 年\月\日\时\分\秒
     *
     * @param date     Date类型日期 
     * @param dateType int类型日期类型 1-7[年\月\日\时\分\秒] Calendar.DATE
     * @return int类型 年\月\日\时\分\秒
     */
    private static int calenderGet(Date date, int dateType) {
        int num = 0;
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            num = calendar.get(dateType);
        }
        return num;
    }

    /**
     * 修改日期中的指定数据,年\月\日\时\分\秒,返回Date类型日期数据
     *
     * @param date     Date类型日期 
     * @param dateType int类型日期类型 1-7[年\月\日\时\分\秒]  
     * @param amount   int类型增加或减少数据 
     * @return Date类型修改后日期
     */
    private static Date calenderAdd(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }

    /**
     * 修改日期中的指定数据,年\月\日\时\分\秒,返回String类型日期数据
     *
     * @param date     String类型日期
     * @param dateType int类型日期类型 1-7[年\月\日\时\分\秒] 
     * @param amount   int类型增加或减少数据 
     * @return String类型修改后日期
     */
    private static String calenderAdd(String date, int dateType, int amount) {
        String dateString = null;
        if (StringUtil.isEmpty(date)) {
            return null;
        }
        DateFormatStyle dateStyle;
        dateStyle = getDateStyle(date);
        Date myDate;
        if (dateStyle != null) {
            String pat = dateStyle.getValue();
            try {
                myDate = dateFormat(pat).parse(date);
            } catch (ParseException e) {
                logger.error(MessageFormat.format("日期转换错误,{0}:", e.getMessage()), e);
                return null;
            }
            myDate = calenderAdd(myDate, dateType, amount);
            if (myDate != null) {
                dateString = dateFormat(dateStyle.getValue()).format(myDate);
            }
        }
        return dateString;
    }

    /**
     * 检查日期格式,如果日期样式输入null或(""),自动返回日期长格式:"yyyy-MM-dd HH:mm:ss"
     *
     * @param pattern String类型日期格式
     * @return 如果日期样式输入null或"", 返回日期格式:"yyyy-MM-dd HH:mm:ss"
     */
    private static String checkPattern(String pattern) {
        if (StringUtil.isBlank(pattern)) {
            pattern = NORM_DATETIME_PATTERN;
        }
        return pattern;
    }


    //------------------------------------------------------------------------------------------------------------------

    /**
     * 判断String格式是否是日期
     *
     * @param date String类型日期字符串
     * @return true/false
     */
    public static boolean isDate(String date) {
        boolean isDate = false;
        if (date != null) {
            if (getDateStyle(date) != null) {
                isDate = true;
            }
        }
        return isDate;
    }

    /**
     * String日期转Date日期,自动解析日期格式转换
     *
     * @param date String类型日期
     * @return Date日期
     */
    public static Date stringToDate(String date) {
        Date myDate = null;
        if (StringUtil.isBlank(date)) {
            return null;
        }
        DateFormatStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            String pat = dateStyle.getValue();
            myDate = stringToDate(date, pat);
        }
        return myDate;
    }


    /**
     * 根据指定日期格式转换String日期为Date日期
     *
     * @param date    String日期字符串
     * @param pattern 日期格式
     * @return Date日期
     */
    public static Date stringToDate(String date, String pattern) {
        Date myDate = null;
        if (StringUtil.isBlank(date)) {
            return null;
        }
        String pat = checkPattern(pattern);
        try {
            myDate = dateFormat(pat).parse(date);
        } catch (ParseException e) {
            logger.error(MessageFormat.format("日期转换出错,{0}", e.getMessage()), e);
        }
        return myDate;
    }

    /**
     * Date格式日期根据指定日期样式转String格式日期
     *
     * @param date    date日期
     * @param pattern 日期格式
     * @return String日期
     */
    public static String dateToString(Date date, String pattern) {
        String dateString = null;
        if (date != null) {
            String pat = checkPattern(pattern);
            dateString = dateFormat(pat).format(date);
        }
        return dateString;
    }

    /**
     * String日期转换为指定格式String日期
     *
     * @param date       String日期
     * @param newPattern 新日期格式
     * @return 新的String日期
     */
    public static String stringToString(String date, String newPattern) {
        String dateString = null;
        if (StringUtil.isBlank(date) || StringUtil.isBlank(newPattern)) {
            return null;
        }
        DateFormatStyle oldDateStyle = getDateStyle(date);
        if (oldDateStyle != null) {
            dateString = dateToString(stringToDate(date, oldDateStyle.getValue()), newPattern);
        }
        return dateString;
    }

    /**
     * String格式日期增加或减少年份
     *
     * @param date       String日期
     * @param yearAmount 年份
     * @return 新的String日期
     */
    public static String addYear(String date, int yearAmount) {
        return calenderAdd(date, Calendar.YEAR, yearAmount);
    }

    /**
     * Date类型日期增加或减少年份
     *
     * @param date       Date日期
     * @param yearAmount 年份
     * @return 新Date日期
     */
    public static Date addYear(Date date, int yearAmount) {
        return calenderAdd(date, Calendar.YEAR, yearAmount);
    }

    /**
     * String类型日期增加或减少月份
     *
     * @param date        String日期
     * @param monthAmount 月份
     * @return 新String日期
     */
    public static String addMonth(String date, int monthAmount) {
        return calenderAdd(date, Calendar.MONTH, monthAmount);
    }

    /**
     * Date类型日期增加或减少月份
     *
     * @param date        Date日期
     * @param monthAmount 月份
     * @return 新Date日期
     */
    public static Date addMonth(Date date, int monthAmount) {
        return calenderAdd(date, Calendar.MONTH, monthAmount);
    }

    /**
     * String类型日期增加或减少天
     *
     * @param date      String日期
     * @param dayAmount 天数
     * @return 新String日期
     */
    public static String addDay(String date, int dayAmount) {
        return calenderAdd(date, Calendar.DATE, dayAmount);
    }

    /**
     * Date类型日期增加或减少日期
     *
     * @param date      Date日期
     * @param dayAmount 天数
     * @return 新Date日期
     */
    public static Date addDay(Date date, int dayAmount) {
        return calenderAdd(date, Calendar.DATE, dayAmount);
    }

    /**
     * String类型日期增加或减少小时
     *
     * @param date       String类型时期
     * @param hourAmount 增加或减少小时
     * @return 新String类型日期
     */
    public static String addHour(String date, int hourAmount) {
        return calenderAdd(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * Date类型日期增加或减少小时
     *
     * @param date       Date类型日期
     * @param hourAmount 增加或减少小时
     * @return 新Date类型日期
     */
    public static Date addHour(Date date, int hourAmount) {
        return calenderAdd(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * String类型日期增加或减少分钟
     *
     * @param date         String类型日期
     * @param minuteAmount 增加或减少分钟
     * @return 新String类型日期
     */
    public static String addMinute(String date, int minuteAmount) {
        return calenderAdd(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * Date类型日期增加或减少分钟
     *
     * @param date         Date类型时期
     * @param minuteAmount 增加或减少分钟
     * @return 新Date类型日期
     */
    public static Date addMinute(Date date, int minuteAmount) {
        return calenderAdd(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * String类型日期增加或减少秒
     *
     * @param date         String类型日期
     * @param secondAmount 增加或减少秒
     * @return 新String类型日期
     */
    public static String addSecond(String date, int secondAmount) {
        return calenderAdd(date, Calendar.SECOND, secondAmount);
    }

    /**
     * Date类型日期增加或减少秒
     *
     * @param date         Date类型日期
     * @param secondAmount 增加或减少秒
     * @return 新Date类型日期
     */
    public static Date addSecond(Date date, int secondAmount) {
        return calenderAdd(date, Calendar.SECOND, secondAmount);
    }

    /**
     * 获取String类型日期获取年份
     *
     * @param date String类型日期
     * @return 年份
     */
    public static int getYear(String date) {
        int year = 0;
        if (StringUtil.isBlank(date)) {
            return year;
        }
        year = getYear(stringToDate(date));
        return year;
    }

    /**
     * Date类型日期获取年份
     *
     * @param date Date类型日期
     * @return 年份
     */
    public static int getYear(Date date) {
        return calenderGet(date, Calendar.YEAR);
    }

    /**
     * 获取输入日期中的月份
     *
     * @param date String类型日期
     * @return 月份
     */
    public static int getMonth(String date) {
        int month = 0;
        if (StringUtil.isBlank(date)) {
            return month;
        }
        month = getMonth(stringToDate(date));
        return month;
    }

    /**
     * Date类型日期获取月份
     *
     * @param date Date类型日期
     * @return 月份
     */
    public static int getMonth(Date date) {
        return calenderGet(date, Calendar.MONTH) + 1;
    }

    /**
     * 获取输入日期中的天
     *
     * @param date String类型日期
     * @return 天
     */
    public static int getDay(String date) {
        int day = 0;
        if (StringUtil.isBlank(date)) {
            return day;
        }
        day = getDay(stringToDate(date));
        return day;
    }

    /**
     * Date类型日期获取天
     *
     * @param date Date类型日期
     * @return 天
     */
    public static int getDay(Date date) {
        return calenderGet(date, Calendar.DATE);
    }

    /**
     * 获取输入日期中的小时
     *
     * @param date String类型时间
     * @return 小时
     */
    public static int getHour(String date) {
        int hour = 0;
        if (StringUtil.isBlank(date)) {
            return hour;
        }
        hour = getHour(stringToDate(date));
        return hour;
    }

    /**
     * Date类型日期获取小时
     *
     * @param date Date类型时间
     * @return 小时
     */
    public static int getHour(Date date) {
        return calenderGet(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取输入日期中的分钟
     *
     * @param date String类型时间
     * @return 分钟
     */
    public static int getMinute(String date) {
        int minute = 0;
        if (StringUtil.isBlank(date)) {
            return minute;
        }
        minute = getMinute(stringToDate(date));
        return minute;
    }

    /**
     * Date类型时间获取分钟
     *
     * @param date Date类型时间
     * @return 分钟
     */
    public static int getMinute(Date date) {
        return calenderGet(date, Calendar.MINUTE);
    }

    /**
     * 获取输入日期中的秒
     *
     * @param date String类型时间
     * @return 秒
     */
    public static int getSecond(String date) {
        int second = 0;
        if (StringUtil.isBlank(date)) {
            return second;
        }
        second = getSecond(stringToDate(date));
        return second;
    }

    /**
     * Date类型时间获取秒
     *
     * @param date Date类型时间
     * @return 秒
     */
    public static int getSecond(Date date) {
        return calenderGet(date, Calendar.SECOND);
    }

    /**
     * String类型日期获取当时星期
     *
     * @param date String类型日期
     * @return 星期信息
     */
    public static DateWeek getWeek(String date) {
        DateWeek dateWeek = null;
        DateFormatStyle dateStyle = getDateStyle(date);
        Date myDate;
        if (StringUtil.isBlank(date)) {
            return null;
        }
        if (dateStyle != null) {
            myDate = stringToDate(date, dateStyle.getValue());
            dateWeek = getWeek(myDate);
        }
        return dateWeek;
    }

    /**
     * Date类型获取星期
     *
     * @param date Date类型日期
     * @return 星期信息
     */
    public static DateWeek getWeek(Date date) {
        DateWeek dateWeek = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (weekNumber) {
            case 0: {
                dateWeek = DateWeek.SUNDAY;
                break;
            }
            case 1: {
                dateWeek = DateWeek.MONDAY;
                break;
            }
            case 2: {
                dateWeek = DateWeek.TUESDAY;
                break;
            }
            case 3: {
                dateWeek = DateWeek.WEDNESDAY;
                break;
            }
            case 4: {
                dateWeek = DateWeek.THURSDAY;
                break;
            }
            case 5: {
                dateWeek = DateWeek.FRIDAY;
                break;
            }
            case 6: {
                dateWeek = DateWeek.SATURDAY;
                break;
            }
        }
        return dateWeek;
    }

    /**
     * 判断String格式日期是否是周末
     *
     * @param strDate String类型日期
     * @return true/false
     */
    public static boolean isWeekend(String strDate) {
        int weekDay = 0;
        if (StringUtil.isBlank(strDate)) {
            return false;
        }
        DateWeek dateWeek = getWeek(strDate);
        if (dateWeek != null) {
            weekDay = dateWeek.getNumber();
        }
        return (weekDay == 6) || (weekDay == 7);
    }

    /**
     * 判断Date类型日期是否是周末
     *
     * @param date Date类型日期
     * @return true/false
     */
    public static boolean isWeekend(Date date) {
        return isWeekend(dateToString(date, NORM_DATE_PATTERN));
    }

    /**
     * 获取两个String类型日期之间相差天数
     *
     * @param date      String 类型日期
     * @param otherDate String类型日期
     * @return 相差天数
     */
    public static int getIntervalDays(String date, String otherDate) {
        return getIntervalDays(stringToDate(date), stringToDate(otherDate));
    }

    /**
     * 获取两个Date类型日期之间相差天数
     *
     * @param date      Date类型日期
     * @param otherDate Date类型日期
     * @return 相差天数
     */
    public static int getIntervalDays(Date date, Date otherDate) {
        int num = -1;
        String dt = dateToString(date, NORM_DATE_PATTERN);
        Date dateTmp = stringToDate(dt, NORM_DATE_PATTERN);
        dt = dateToString(otherDate, NORM_DATE_PATTERN);
        Date otherDateTmp = stringToDate(dt, NORM_DATE_PATTERN);
        if ((dateTmp != null) && (otherDateTmp != null)) {
            long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());
            num = (int) (time / (24 * 60 * 60 * 1000));
        }
        return num;
    }

    /**
     * 获取两个String类型日期月份相差天数
     *
     * @param date1 String类型日期
     * @param date2 String类型日期
     * @return 相差天数
     */
    public static int getIntervalMonth(String date1, String date2) {
        int margin;
        Integer year1 = getYear(date1);
        Integer year2 = getYear(date2);
        if (year1 > year2) {
            margin = (year1 - year2) * 12;
            Integer month1 = getMonth(date1);
            Integer month2 = getMonth(date2);
            margin += month1 - month2;
        } else {
            margin = (year2 - year1) * 12;
            Integer month1 = getMonth(date1);
            Integer month2 = getMonth(date2);
            margin += month2 - month1;
        }
        return margin;
    }

    /**
     * 返回某年某月中最大天数
     *
     * @param year  int类型分年份[2016]
     * @param month int类型月份
     * @return 最大的月份
     */
    public static int getMaxDay(int year, int month) {
        int day;
        if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10)
                || (month == 12)) {
            day = 31;
        } else if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
            day = 30;
        } else if ((0 == (year % 4)) && (0 != (year % 100)) || (0 == (year % 400))) {
            day = 29;
        } else {
            day = 28;
        }
        return day;
    }

    /**
     * 根据String类型日期获取农历
     *
     * @param date String类型日期
     * @return 农历 DateLunarCalendar
     */
    public static DateLunarCalendar getSimpleLunarCalendar(String date) {
        return new DateLunarCalendar(stringToDate(date));
    }

    /**
     * 根据Date类型日期获取农历
     *
     * @param date Date类型日期
     * @return 农历 DateLunarCalendar
     */
    public static DateLunarCalendar getSimpleLunarCalendar(Date date) {
        return new DateLunarCalendar(date);
    }

    /**
     * 获取Date类型日期一天结束时间,如果传入null则返回当天结束时间
     *
     * @param day Date类型日期
     * @return 输入日期当天结束时间
     */
    public static Date getEndOfDay(Date day) {
        if (day == null) {
            day = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    /**
     * 获取Date类型日期一天开始时间,如果传入null则返回当天开始时间
     *
     * @param day Date类型日期
     * @return 输入日期当天开始时间
     */
    public static Date getStartOfDay(Date day) {
        if (day == null) {
            day = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    /**
     * 获取Date类型日期月份结束时间,如果传入null则返回当月结束时间
     *
     * @param day Date类型日期
     * @return 输入日期当月结束时间
     */
    public static Date getEndOfMonth(Date day) {
        if (day == null) {
            day = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取Date类型日期月份开始时间,如果传入null则返回当月开始时间
     *
     * @param day Date类型日期
     * @return 输入日期当月开始时间
     */
    public static Date getStartOfMonth(Date day) {
        if (day == null) {
            day = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取Date类型日期12时,如果传入null则返回当天12点
     *
     * @param day Date类型日期
     * @return 输入日期当天12点
     */
    public static Date getNoonOfDay(Date day) {
        if (day == null) {
            day = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    /**
     * 根据指定格式,将指定字符串与当前时间进行比较
     * <p>
     * int code
     *
     * @param strDate String类型日期
     * @param format  输入日期的格式
     * @return 大于0 当前时间大于输入时间 等于0 输入时间与当前时间是同一时间 小于0 当前时间小于输入时间
     */
    public static int compareToCurTime(String strDate, String format) {
        if (StringUtil.isBlank(strDate) || StringUtil.isBlank(format)) {
            return -1;
        }
        Date curTime = Calendar.getInstance().getTime();
        String strCurTime = dateToString(curTime, format);
        if (StringUtil.isNotBlank(strCurTime)) {
            return strCurTime.compareTo(strDate);
        }
        return -1;
    }

    /**
     * 返回系统现在年份中指定月份的天数
     *
     * @param month 月份
     * @return 指定月的总天数
     */
    public static String getMonthLastDay(int month) {
        Calendar calendar = Calendar.getInstance();
        int[][] day = {{0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
                {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}};
        int year = calendar.get(Calendar.YEAR) + 1900;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return day[1][month] + "";
        } else {
            return day[0][month] + "";
        }
    }

    /**
     * 返回指定年份中指定月份的天数
     *
     * @param year  年份
     * @param month 月份
     * @return 指定月的总天数
     */
    public static String getMonthLastDay(int year, int month) {
        int[][] day = {{0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
                {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}};
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return day[1][month] + "";
        } else {
            return day[0][month] + "";
        }
    }

    /**
     * 判断是平年还是闰年
     *
     * @param year int类型年份
     * @return true/false
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400) == 0;
    }

    /**
     * 判断一个Date类型日期是否在一段时间内
     *
     * @param fromDate 开始时间
     * @param toDate   结束时间
     * @param testDate 判断时间
     * @return 真/假
     */
    public static boolean betweenDays(Date fromDate, Date toDate, Date testDate) {
        if ((fromDate == null) || (toDate == null) || (testDate == null)) {
            return false;
        }    // 1、 交换开始和结束日期
        if (fromDate.getTime() > toDate.getTime()) {
            Date tempDate = fromDate;
            fromDate = toDate;
            toDate = tempDate;
        }    // 2、缩小范围
        long testDateTime = testDate.getTime();
        return !(((testDateTime > fromDate.getTime()) && (testDateTime > toDate.getTime()))
                || ((testDateTime < fromDate.getTime()) && (testDateTime < toDate.getTime())));
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  年 例如:[1991]
     * @param month 月 例如:[12]
     * @return String类型日期, yyyy-MM-dd
     */
    public static String getLastDateDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        // 某年某月的最后一天
        int lastDate = cal.getActualMaximum(Calendar.DATE);
        return year + "-" + (month + 1) + "-" + lastDate;
    }

    /**
     * 判断两个日期是否相同
     *
     * @param d1 Date类型时间
     * @param d2 Date类型时间
     * @return 真/假
     */
    public static boolean isSameDate(Date d1, Date d2) {
        boolean result = false;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        if ((c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))) {
            result = true;
        }
        return result;
    }

    /**
     * 日期时间集合排序
     *
     * @param dates     Date类型时间集合
     * @param orderType 排序类型
     * @return 排序后集合
     */
    public static List<? extends Date> orderDate(List<? extends Date> dates, int orderType) {
        Collections.sort(dates, new DateComparator(orderType));
        return dates;
    }

    /**
     * 日期时间集合分组
     *
     * @param dates     Date类型时间集合
     * @param orderType 排序类型
     * @return 分组排序后集合
     */
    public static List<List<? extends Date>> groupDates(List<? extends Date> dates, int orderType) {
        List<List<? extends Date>> result = new ArrayList<>();
        // 按照升序排序
        orderDate(dates, orderType);
        // 临时结果
        List<Date> tempDates = null;
        // 上一组最后一个日期
        Date lastDate;
        // 当前读取日期
        Date cdate;
        for (Date date : dates) {
            cdate = date;    // 第一次增加
            if (tempDates == null) {
                tempDates = new ArrayList<>();
                tempDates.add(cdate);
                result.add(tempDates);
            } else { /* * 差距为1是继续在原有的列表中添加，大于1就是用新的列表 */
                lastDate = tempDates.get(tempDates.size() - 1);
                int betweenDays;
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c1.setTime(lastDate);
                c2.setTime(cdate);
                // 保证第二个时间一定大于第一个时间
                if (c1.after(c2)) {
                    c2.setTime(lastDate);
                    c1.setTime(cdate);
                }
                int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
                betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
                for (int i = 0; i < betweenYears; i++) {
                    c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
                    betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
                }
                int days = betweenDays;
                if (days == 1) {
                    tempDates.add(cdate);
                } else {
                    tempDates = new ArrayList<>();
                    tempDates.add(cdate);
                    result.add(tempDates);
                }
            }
        }
        return result;
    }

    /**
     * 根据生日获取年龄
     *
     * @param birthdayDate Date类型时候生日
     * @return 年龄
     */
    public static int getAge(Date birthdayDate) {
        Date currentDate = new Date();
        int currentYear = getYear(currentDate);
        int currentMonth = getMonth(currentDate);
        int currentDay = getDay(currentDate);

        int birthYear = getYear(birthdayDate);
        int birthMonth = getMonth(birthdayDate);
        int birthDay = getDay(birthdayDate);

        if (currentMonth > birthMonth) {
            return currentYear - birthYear;
        } else if (currentMonth == birthMonth) {
            if (currentDay >= birthDay) {
                return currentYear - birthYear;
            } else {
                return currentYear - birthYear - 1;
            }
        } else {
            return currentYear - birthYear - 1;
        }
    }

    /**
     * 获取两个时间点之间的所有日期
     *
     * @param startDate Date 时间点1 
     * @param endDate   Date 时间点2 
     * @return a value of List 日期集合
     */
    public static List<Date> getBetweenDates(Date startDate, Date endDate) {
        List<Date> result = new ArrayList<>();
        // 如果开始日期大于结束日期交换
        if (startDate.getTime() < endDate.getTime()) {
            Date tempDate = endDate;
            endDate = startDate;
            startDate = tempDate;
        }
        Calendar ca = Calendar.getInstance();
        while (endDate.getTime() <= startDate.getTime()) {
            ca.setTime(endDate);
            Date tempDate = new Date(ca.getTime().getTime());
            result.add(tempDate);
            ca.add(Calendar.DATE, 1);
            endDate = new Date(ca.getTime().getTime());
        }
        result.stream().filter(date -> !result.contains(date)).forEach(result::add);
        return result;
    }

    /**
     * 当前季度的开始时间，即2012-01-1 00:00:00
     *
     * @return 开始时间点
     */
    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        if (currentMonth >= 1 && currentMonth <= 3)
            c.set(Calendar.MONTH, 0);
        else if (currentMonth >= 4 && currentMonth <= 6)
            c.set(Calendar.MONTH, 3);
        else if (currentMonth >= 7 && currentMonth <= 9)
            c.set(Calendar.MONTH, 4);
        else if (currentMonth >= 10 && currentMonth <= 12)
            c.set(Calendar.MONTH, 9);
        c.set(Calendar.DATE, 1);
        try {
            now = dateFormat(NORM_DATETIME_PATTERN).parse(dateFormat(NORM_DATE_PATTERN).format(c.getTime()) + " 00:00:00");
        } catch (ParseException e) {
            logger.error(MessageFormat.format("日期转换出错,{0}:", e.getMessage()), e);
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return 结束时间点
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        if (currentMonth >= 1 && currentMonth <= 3) {
            c.set(Calendar.MONTH, 2);
            c.set(Calendar.DATE, 31);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 5);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            c.set(Calendar.MONTH, 8);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
        }
        try {
            now = dateFormat(NORM_DATETIME_PATTERN).parse(dateFormat(NORM_DATE_PATTERN).format(c.getTime()) + " 23:59:59");
        } catch (ParseException e) {
            logger.error(MessageFormat.format("日期转换出错,{0}:", e.getMessage()), e);
        }
        return now;
    }

    /**
     * 获取前/后半年的开始时间
     *
     * @return 开始时间点
     */
    public static Date getHalfYearStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        if (currentMonth >= 1 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 0);
        } else if (currentMonth >= 7 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 6);
        }
        c.set(Calendar.DATE, 1);
        try {
            now = dateFormat(NORM_DATETIME_PATTERN).parse(dateFormat(NORM_DATE_PATTERN).format(c.getTime()) + " 00:00:00");
        } catch (ParseException e) {
            logger.error(MessageFormat.format("日期转换出错,{0}:", e.getMessage()), e);
        }
        return now;

    }

    /**
     * 获取前/后半年的结束时间
     *
     * @return 结束时间点
     */
    public static Date getHalfYearEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        if (currentMonth >= 1 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 5);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 7 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
        }
        try {
            now = dateFormat(NORM_DATETIME_PATTERN).parse(dateFormat(NORM_DATE_PATTERN).format(c.getTime()) + " 23:59:59");
        } catch (ParseException e) {
            logger.error(MessageFormat.format("日期转换出错,{0}:", e.getMessage()), e);
        }
        return now;
    }

    /**
     * 将指定的日期转换成Unix时间戳
     *
     * @param date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        try {
            return dateFormat(NORM_DATETIME_PATTERN).parse(date).getTime();
        } catch (ParseException e) {
            logger.error(MessageFormat.format("日期转换出错,{0}:", e.getMessage()), e);
        }
        return 0;
    }

    /**
     * 将指定的日期转换成Unix时间戳
     *
     * @param date       需要转换的日期 yyyy-MM-dd
     * @param dateFormat 日期格式
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        try {
            return dateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            logger.error(MessageFormat.format("日期转换出错,{0}:", e.getMessage()), e);
        }
        return 0;
    }

    /**
     * 将当前日期转换成Unix时间戳
     *
     * @return long 时间戳
     */
    public static long getCurrentDateToUnixTimestamp() {
        return new Date().getTime();
    }

    /**
     * 将Unix时间戳转换成日期
     *
     * @param timestamp 时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = dateFormat(NORM_DATETIME_PATTERN);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 当前时间long
     *
     * @param isNano 是否为高精度时间
     * @return 时间
     */
    public static long current(boolean isNano) {
        return isNano ? System.nanoTime() : System.currentTimeMillis();
    }

    /**
     * 获得指定日期区间内的年份和季节<br>
     *
     * @param startDate 其实日期（包含）
     * @param endDate   结束日期（包含）
     * @return Season列表 ，元素类似于 20132
     */
    public static LinkedHashSet<String> yearAndSeasons(Date startDate, Date endDate) {
        final LinkedHashSet<String> seasons = new LinkedHashSet<>();
        if (startDate == null || endDate == null) {
            return seasons;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        while (true) {
            // 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
            if (startDate.after(endDate)) {
                startDate = endDate;
            }
            seasons.add(yearAndSeason(cal));
            if (startDate.equals(endDate)) {
                break;
            }
            cal.add(Calendar.MONTH, 3);
            startDate = cal.getTime();
        }
        return seasons;
    }

    /**
     * 判断两个日期相差的时长<br>
     * 返回 minuend - subtrahend 的差
     *
     * @param subtrahend 减数日期
     * @param minuend    被减数日期
     * @param diffField  相差的选项：相差的天、小时
     * @return 日期差
     */
    public static long diff(Date subtrahend, Date minuend, long diffField) {
        long diff = minuend.getTime() - subtrahend.getTime();
        return diff / diffField;
    }

    /**
     * 计时，常用于记录某段代码的执行时间，单位：纳秒
     *
     * @param preTime 之前记录的时间
     * @return 时间差，纳秒
     */
    public static long spendNt(long preTime) {
        return System.nanoTime() - preTime;
    }

    /**
     * 计时，常用于记录某段代码的执行时间，单位：毫秒
     *
     * @param preTime 之前记录的时间
     * @return 时间差，毫秒
     */
    public static long spendMs(long preTime) {
        return System.currentTimeMillis() - preTime;
    }


    /**
     * 计算指定指定时间区间内的周数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 周数
     */
    public static int weekCount(Date start, Date end) {
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        final Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        final int startWeekofYear = startCalendar.get(Calendar.WEEK_OF_YEAR);
        final int endWeekofYear = endCalendar.get(Calendar.WEEK_OF_YEAR);
        int count = endWeekofYear - startWeekofYear + 1;
        if (Calendar.SUNDAY != startCalendar.get(Calendar.DAY_OF_WEEK)) {
            count--;
        }
        return count;
    }

    /**
     * 计时器<br>
     * 计算某个过程话费的时间，精确到毫秒
     *
     * @return Timer
     */
    public static com.github.asherli0103.utils.tools.Timer timer() {
        return new com.github.asherli0103.utils.tools.Timer();

    }

    /**
     * 获得指定日期年份和季节<br>
     * 格式：[20131]表示2013年第一季度
     *
     * @param cal 日期
     * @return 年+季度  例:20162 --&gt;2016年第二季度
     */
    private static String yearAndSeason(Calendar cal) {
        return String.valueOf(cal.get(Calendar.YEAR)) + (cal.get(Calendar.MONTH) / 3 + 1);
    }

}
