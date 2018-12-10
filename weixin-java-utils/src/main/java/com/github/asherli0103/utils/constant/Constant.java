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

package com.github.asherli0103.utils.constant;

import java.awt.*;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class Constant {

    /**
     * 程序加载中显示文字
     */
    public static final String PROGRESS_MSG = "加载程序中,请稍候......";
    public static final String WELCOME_IMG_URL = "/img/welcome.jpg";
    /**
     * 标准日期格式
     */
    public final static String NORM_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 标准日期时间格式，精确到秒
     */
    public final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 屏幕宽度
     */
    public static int SCREEN_WIDTH;
    /**
     * 屏幕高度
     */
    public static int SCREEN_HEIGHT;
    /**
     * 获取屏幕的分辨率大小
     */
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static {
        SCREEN_WIDTH = (int) screenSize.getWidth();
        SCREEN_HEIGHT = (int) screenSize.getHeight();
    }
}
