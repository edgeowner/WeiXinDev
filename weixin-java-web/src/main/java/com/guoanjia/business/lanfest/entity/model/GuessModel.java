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

package com.guoanjia.business.lanfest.entity.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;


public class GuessModel implements Serializable {
    private Integer id;
    private String title;
    private String guess;
    private String result;
    private String keyboard;

    public Integer getId() {
        return id;
    }

    public GuessModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public GuessModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getGuess() {
        return guess;
    }

    public GuessModel setGuess(String guess) {
        this.guess = guess;
        return this;
    }

    public String getResult() {
        return result;
    }

    public GuessModel setResult(String result) {
        this.result = result;
        return this;
    }

    public String getKeyboard() {
        return keyboard;
    }

    public GuessModel setKeyboard(String keyboard) {
        this.keyboard = keyboard;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
