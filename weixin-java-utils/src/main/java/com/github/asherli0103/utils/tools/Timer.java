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

package com.github.asherli0103.utils.tools;


import com.github.asherli0103.utils.DateUtil;

/**
 * 计时器
 * 计算某个过程话费的时间，精确到毫秒
 *
 * @author Asherli
 * @version 1.0.00
 */
public class Timer {

    private long time;
    private boolean isNano;

    public Timer() {
        this(false);
    }

    public Timer(boolean isNano) {
        this.isNano = isNano;
        start();
    }

    /**
     * @return 开始计时并返回当前时间
     */
    public long start() {
        time = DateUtil.current(isNano);
        return time;
    }

    /**
     * @return 重新计时并返回从开始到当前的持续时间
     */
    public long durationRestart() {
        long now = DateUtil.current(isNano);
        long d = now - time;
        time = now;
        return d;
    }

    /**
     * @return 从开始到当前的持续时间
     */
    public long duration() {
        return DateUtil.current(isNano) - time;
    }
}
