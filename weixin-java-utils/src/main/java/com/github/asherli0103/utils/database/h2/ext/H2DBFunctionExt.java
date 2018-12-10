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

package com.github.asherli0103.utils.database.h2.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 针对H2数据库函数的扩展
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
public class H2DBFunctionExt {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2DBFunctionExt.class);

    /**
     * 实现MySQL数据库UUID()函数,用户生成原生UUID
     * 用法：SELECT UUID();
     * H2数据库注册uuid函数：CREATE ALIAS UUID FOR "com.asherli.core.db.ext.H2DBFunctionExt.uuid";
     *
     * @return UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }


    /**
     * 实现MySQL数据库的NOW()函数，用于生成当前系统时间
     * 用法: SELECT NOW();
     * H2数据库注册currentTime函数：CREATE ALIAS IF NOT EXISTS NOW FOR "com.asherli.core.db.ext.H2DBFunctionExt.now";
     *
     * @return 当前时间
     */
    public static String now() {
        return new Date().toString();
    }

    /**
     * H2数据库注册IP函数：CREATE ALIAS IF NOT EXISTS IP FOR "com.asherli.core.db.ext.H2DBFunctionExt.getIp";
     *
     * @return ip
     */
    public static String getIp() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("获取IP地址失败", e);
            return "未知的IP地址";
        }
    }

    /**
     * 实现MySQL数据库的DATE_FORMAT()函数，用于格式化日期
     * H2数据库注册DATE_FORMAT函数：CREATE ALIAS IF NOT EXISTS DATE_FORMAT FOR "com.asherli.core.db.ext.H2DBFunctionExt.date_format";
     *
     * @param date    时间
     * @param pattern 格式化格式
     * @return 日期
     */
    public static String date_format(String date, String pattern) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                Date temp = sdf.parse(date);
                return sdf.format(temp);
            } catch (ParseException e) {
                LOGGER.error("指定时间格式化失败", e);
            }
        }
        return "";
    }

}
