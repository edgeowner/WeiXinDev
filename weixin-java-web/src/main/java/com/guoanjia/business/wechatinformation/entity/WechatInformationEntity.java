package com.guoanjia.business.wechatinformation.entity;

import com.github.asherli0103.core.entity.IdEntity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/3/10 0010.
 */
@Entity
@Table(name = "wx_n_user_information")
public class WechatInformationEntity extends IdEntity implements Serializable {
    @Column(name = "username")
    private String username;
    @Column(name = "openid")
    private String openid;
    @Column(name = "parentopenid")
    private String parentopenid;
    @Column(name = "phoneversion")
    private String phoneversion;
    @Column(name = "accesstime")
    private String accesstime;
    @Column(name = "intopagetime")
    private Date intopagetime;
    @Column(name = "leavepagetime")
    private Date leavepagetime;
    @Column(name = "leavepagetype")
    private Integer leavepagetype;
    @Column(name = "pageid")
    private String pageid;
    @Column(name = "userip")
     private String userip;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "precisionaddress")
    private String precisionaddress;
    @Column(name = "createtime")
    private Date createtime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getParentopenid() {
        return parentopenid;
    }

    public void setParentopenid(String parentopenid) {
        this.parentopenid = parentopenid;
    }

    public String getPhoneversion() {
        return phoneversion;
    }

    public void setPhoneversion(String phoneversion) {
        this.phoneversion = phoneversion;
    }

    public String getAccesstime() {
        return accesstime;
    }

    public void setAccesstime(String accesstime) {
        this.accesstime = accesstime;
    }

    public Date getIntopagetime() {
        return intopagetime;
    }

    public void setIntopagetime(Date intopagetime) {
        this.intopagetime = intopagetime;
    }

    public Date getLeavepagetime() {
        return leavepagetime;
    }

    public void setLeavepagetime(Date leavepagetime) {
        this.leavepagetime = leavepagetime;
    }

    public Integer getLeavepagetype() {
        return leavepagetype;
    }

    public void setLeavepagetype(Integer leavepagetype) {
        this.leavepagetype = leavepagetype;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public String getUserip() {
        return userip;
    }

    public void setUserip(String userip) {
        this.userip = userip;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrecisionaddress() {
        return precisionaddress;
    }

    public void setPrecisionaddress(String precisionaddress) {
        this.precisionaddress = precisionaddress;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
