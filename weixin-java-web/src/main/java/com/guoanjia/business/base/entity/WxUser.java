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

package com.guoanjia.business.base.entity;

import com.github.asherli0103.core.entity.IdEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 微信用户表
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
@Entity
@Table(name = "wx_user")
public class WxUser extends IdEntity implements Serializable {

    /**
     * 用户OpenId
     */
    @Column(name = "U_OPENID", length = 50)
    private String uOpenid;
    /**
     * 用户昵称
     */
    @Column(name = "U_NICKNAME", length = 50)
    private String uNickname;
    /**
     * 用户性别
     */
    @Column(name = "U_SEX", length = 2)
    private String uSex;
    /**
     * 用户省份
     */
    @Column(name = "U_PROVINCE", length = 50)
    private String uProvince;
    /**
     * 用户城市
     */
    @Column(name = "U_CITY", length = 50)
    private String uCity;
    /**
     * 用户区县
     */
    @Column(name = "U_COUNTRY", length = 50)
    private String uCountry;
    /**
     * 用户头像地址
     */
    @Column(name = "U_HEADIMG", length = 255)
    private String uHeadimg;
    /**
     * 用户具有权限
     */
    @Column(name = "U_PRIVILEGE", length = 2000)
    private String uPrivilege;
    /**
     * 用户Unionid
     */
    @Column(name = "U_UNIONID", length = 1000)
    private String uUnionid;
    /**
     * 用户手机号
     */
    @Column(name = "U_USERPHONE", length = 50)
    private String uUserphone;
    /**
     * 用户自定义名称
     */
    @Column(name = "U_NAME_CUSTOM", length = 50)
    private String uNameCustom;
    /**
     * 用户自定义头像地址
     */
    @Column(name = "U_HEADIMG_CUSTOM", length = 255)
    private String uHeadimgCustom;
    /**
     * 用户头像本地地址
     */
    @Column(name = "U_HEADIMG_LOCAL", length = 255)
    private String uHeadimgLocal;
    /**
     * 用户创建时间
     */
    @Column(name = "U_CREATE_TIME")
    private Date uCreateTime;

    public WxUser() {
    }

    public String getuOpenid() {
        return uOpenid;
    }

    public WxUser setuOpenid(String uOpenid) {
        this.uOpenid = uOpenid == null ? "" : uOpenid;
        return this;
    }

    public String getuUserphone() {
        return uUserphone;
    }

    public WxUser setuUserphone(String uUserphone) {
        this.uUserphone = uUserphone == null ? "" : uUserphone;
        return this;
    }

    public String getuNickname() {
        return uNickname;
    }

    public WxUser setuNickname(String uNickname) {
        this.uNickname = uNickname == null ? "" : uNickname;
        return this;
    }

    public String getuCity() {
        return uCity;
    }

    public WxUser setuCity(String uCity) {
        this.uCity = uCity == null ? "" : uCity;
        return this;
    }

    public String getuProvince() {
        return uProvince;
    }

    public WxUser setuProvince(String uProvince) {
        this.uProvince = uProvince == null ? "" : uProvince;
        return this;
    }

    public String getuCountry() {
        return uCountry;
    }

    public WxUser setuCountry(String uCountry) {
        this.uCountry = uCountry == null ? "" : uCountry;
        return this;
    }

    public String getuHeadimg() {
        return uHeadimg;
    }

    public WxUser setuHeadimg(String uHeadimg) {
        this.uHeadimg = uHeadimg == null ? "" : uHeadimg;
        return this;
    }

    public String getuNameCustom() {
        return uNameCustom;
    }

    public WxUser setuNameCustom(String uNameCustom) {
        this.uNameCustom = uNameCustom == null ? "" : uNameCustom;
        return this;
    }

    public String getuHeadimgCustom() {
        return uHeadimgCustom;
    }

    public WxUser setuHeadimgCustom(String uHeadimgCustom) {
        this.uHeadimgCustom = uHeadimgCustom == null ? "" : uHeadimgCustom;
        return this;
    }

    public String getuHeadimgLocal() {
        return uHeadimgLocal;
    }

    public WxUser setuHeadimgLocal(String uHeadimgLocal) {
        this.uHeadimgLocal = uHeadimgLocal == null ? "" : uHeadimgLocal;
        return this;
    }

    public Date getuCreateTime() {
        return uCreateTime;
    }

    public WxUser setuCreateTime(Date uCreateTime) {
        this.uCreateTime = uCreateTime;
        return this;
    }

    public String getuSex() {
        return uSex;
    }

    public WxUser setuSex(String uSex) {
        this.uSex = uSex == null ? "" : uSex;
        return this;
    }

    public String getuPrivilege() {
        return uPrivilege;
    }

    public WxUser setuPrivilege(String uPrivilege) {
        this.uPrivilege = uPrivilege == null ? "" : uPrivilege;
        return this;
    }

    public String getuUnionid() {
        return uUnionid;
    }

    public WxUser setuUnionid(String uUnionid) {
        this.uUnionid = uUnionid == null ? "" : uUnionid;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
