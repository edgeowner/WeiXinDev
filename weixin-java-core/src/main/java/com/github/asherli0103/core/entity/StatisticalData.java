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

package com.github.asherli0103.core.entity;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class StatisticalData {

    private Double max;

    private Long count;

    private Double min;

    private Double avg;

    private Double sum;


    public StatisticalData(Double max, Long count, Double min, Double avg, Double sum) {
        this.max = max;
        this.count = count;
        this.min = min;
        this.avg = avg;
        this.sum = sum;
    }

    public Double getMax() {
        return max;
    }

    public StatisticalData setMax(Double max) {
        this.max = max;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public StatisticalData setCount(Long count) {
        this.count = count;
        return this;
    }

    public Double getMin() {
        return min;
    }

    public StatisticalData setMin(Double min) {
        this.min = min;
        return this;
    }

    public Double getAvg() {
        return avg;
    }

    public StatisticalData setAvg(Double avg) {
        this.avg = avg;
        return this;
    }

    public Double getSum() {
        return sum;
    }

    public StatisticalData setSum(Double sum) {
        this.sum = sum;
        return this;
    }

    @Override
    public String toString() {
        return "StatisticalData{" +
                "max=" + max +
                ", count=" + count +
                ", min=" + min +
                ", avg=" + avg +
                ", sum=" + sum +
                '}';
    }
}
