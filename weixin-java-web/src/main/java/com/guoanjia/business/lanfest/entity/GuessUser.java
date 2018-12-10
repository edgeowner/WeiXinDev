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
import java.util.Date;


@Entity
@Table(name = "guess_user", schema = "")
public class GuessUser extends IdEntity implements Serializable {
    @Column(name = "NAME", length = 100)
    private String name;
    @Column(name = "PHONE", length = 11)
    private String phone;
    @Column(name = "MAXSCORE", length = 50)
    private String maxscore;
    @Column(name = "FINALLCSORE", length = 50)
    private String finallcsore;
    @Column(name = "COMPANY", length = 100)
    private String company;
    @Column(name = "TOTALTIMES")
    private Integer totaltimes;
    @Column(name = "CREATTIME")
    private Date creattime;
    @Column(name = "UPDATETIME")
    private Date updatetime;

    public String getName() {
        return name;
    }

    public GuessUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public GuessUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getMaxscore() {
        return maxscore;
    }

    public GuessUser setMaxscore(String maxscore) {
        this.maxscore = maxscore;
        return this;
    }

    public String getFinallcsore() {
        return finallcsore;
    }

    public GuessUser setFinallcsore(String finallcsore) {
        this.finallcsore = finallcsore;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public GuessUser setCompany(String company) {
        this.company = company;
        return this;
    }

    public Integer getTotaltimes() {
        return totaltimes;
    }

    public GuessUser setTotaltimes(Integer totaltimes) {
        this.totaltimes = totaltimes;
        return this;
    }

    public Date getCreattime() {
        return creattime;
    }

    public GuessUser setCreattime(Date creattime) {
        this.creattime = creattime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public GuessUser setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
