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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressWarnings("unused")
public class TimeUtil {
    public static final String TIMESTAMPFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String FILETIMESTAMPFORMAT = "yyyy-MM-dd'T'HH.mm.ss.SSSZ";
    public static final String ASPENTEEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    public static Date dateFromTimestamp(String timestamp) {
        String formatsToTry[] = {
                TIMESTAMPFORMAT, TIMESTAMPFORMAT.replace(".SSS", ""),
                TIMESTAMPFORMAT.replace("Z", ""), TIMESTAMPFORMAT.replace(".SSSZ", ""),
                "EEE MMM dd HH:mm:ss zzz yyyy"
        };

        if (StringUtil.isNullOrEmpty(timestamp)) return null;
        int pos = timestamp.lastIndexOf(':');
        if (pos == timestamp.length() - 3 && timestamp.replaceAll("[^:]", "").length() == 3) {
            timestamp = timestamp.replaceFirst(":([0-9][0-9])$", "$1");
        }

        for (int i = 0; i < formatsToTry.length; ++i) {
            String format = formatsToTry[i];
            DateFormat df = new SimpleDateFormat(format);
            try {
                return df.parse(timestamp);
            } catch (IllegalArgumentException | ParseException e1) {
                if (i == formatsToTry.length - 1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    public static long fromTimestampToMillis(String timestamp) {
        long t = 0;
        DateFormat df = new SimpleDateFormat(TIMESTAMPFORMAT);
        try {
            Date d = df.parse(timestamp);
            assert (d != null);
            t = d.getTime();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return t;
    }

    public static String timestampForFile() {
        return new SimpleDateFormat(FILETIMESTAMPFORMAT).format(System.currentTimeMillis());
    }

    public static String toTimestamp(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return new SimpleDateFormat(TIMESTAMPFORMAT).format(cal.getTime());
    }

    public static String toTimestamp(Date dateTime) {
        return toTimestamp(dateTime.getTime());
    }

    public static String toAspenTimeString(long millis) {
        return toAspenTimeString(millis, ASPENTEEFORMAT);
    }

    public static String toAspenTimeString(Date d) {
        return toAspenTimeString(d, ASPENTEEFORMAT);
    }

    public static String toAspenTimeString(Date d, String format) {
        if (d != null) {
            return toAspenTimeString(d.getTime(), format);
        } else {
            logger.error("Cannot convert null Date");
            return null;
        }
    }

    public static String toAspenTimeString(long millis, String format) {
        if (format == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(millis);
        return new SimpleDateFormat(format).format(cal.getTime());
    }

}
