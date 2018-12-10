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

package com.github.asherli0103.utils.lang.date;

import java.util.Comparator;
import java.util.Date;


/**
 * 日期对比实现
 *
 * @author Asherli
 * @version 1.0.00
 */
@SuppressWarnings(value = {"unused"})
public class DateComparator implements Comparator<Date> {

    /**
     * 日期排序类型-升序
     */
    private static final int DATE_ORDER_ASC = 0;
    /**
     * 日期排序类型-降序
     */
    private static final int DATE_ORDER_DESC = 1;
    /**
     * 属性 orderType 排序顺序类型
     */
    private int orderType;

    /**
     * 私有化构造函数
     */
    private DateComparator() {
        super();
    }

    /**
     * 有参构造,定义排序类型
     *
     * @param orderType int 排序顺序类型
     */
    public DateComparator(int orderType) {
        this.orderType = orderType;
    }

    /**
     * 实现对比接口,对比日期
     *
     * @param d1 Date 时间参数1
     * @param d2 Date 时间参数2
     * @return a value of int 比对顺序
     */
    @Override
    public int compare(Date d1, Date d2) {
        if (d1.getTime() > d2.getTime()) {
            if (orderType == DATE_ORDER_ASC) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if (d1.getTime() == d2.getTime()) {
                return 0;
            } else {
                if (orderType == DATE_ORDER_DESC) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }
}
