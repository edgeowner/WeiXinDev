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

package com.guoanjia.business.lanfest.entity;

import com.github.asherli0103.core.entity.IdEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "guess", schema = "")
public class Guess extends IdEntity implements Serializable {
    @Column(name = "VERSION", nullable = false, length = 19)
    private Integer version;
    @Column(name = "TITLE", nullable = false, length = 255)
    private String title;
    @Column(name = "GUESS", nullable = false, length = 1024)
    private String guess;
    @Column(name = "RESULT", nullable = false, length = 255)
    private String result;
    @Column(name = "MEMO", nullable = true, length = 255)
    private String memo;

    public Integer getVersion() {
        return version;
    }

    public Guess setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Guess setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getGuess() {
        return guess;
    }

    public Guess setGuess(String guess) {
        this.guess = guess;
        return this;
    }

    public String getResult() {
        return result;
    }

    public Guess setResult(String result) {
        this.result = result;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public Guess setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
