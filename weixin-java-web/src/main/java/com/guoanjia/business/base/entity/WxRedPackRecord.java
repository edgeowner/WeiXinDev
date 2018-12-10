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

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Entity
@Table(name = "wx_red_pack_record")
public class WxRedPackRecord extends IdEntity implements Serializable {

    @Column(name = "RETURN_CODE", length = 16)
    private String returnCode;

    @Column(name = "RETURN_MSG", length = 128)
    private String returnMsg;

    @Column(name = "SIGN", length = 32)
    private String sign;

    @Column(name = "RESULT_CODE", length = 16)
    private String resultCode;

    @Column(name = "ERR_CODE", length = 32)
    private String errCode;

    @Column(name = "ERR_CODE_DES", length = 128)
    private String errCodeDes;

    @Column(name = "MCH_BILLNO", length = 28)
    private String mchBillno;

    @Column(name = "MCH_ID", length = 32)
    private String mchId;

    @Column(name = "WXAPPID", length = 32)
    private String wxappid;

    @Column(name = "RE_OPENID", length = 32)
    private String reOpenid;

    @Column(name = "TOTAL_AMOUNT", length = 10)
    private int totalAmount;

    @Column(name = "SEND_LISTID", length = 32)
    private String sendListid;

    public String getReturnCode() {
        return returnCode;
    }

    public WxRedPackRecord setReturnCode(String returnCode) {
        this.returnCode = returnCode == null ? "" : returnCode;
        return this;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public WxRedPackRecord setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg == null ? "" : returnMsg;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public WxRedPackRecord setSign(String sign) {
        this.sign = sign == null ? "" : sign;
        return this;
    }

    public String getResultCode() {
        return resultCode;
    }

    public WxRedPackRecord setResultCode(String resultCode) {
        this.resultCode = resultCode == null ? "" : resultCode;
        return this;
    }

    public String getErrCode() {
        return errCode;
    }

    public WxRedPackRecord setErrCode(String errCode) {
        this.errCode = errCode == null ? "" : errCode;
        return this;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public WxRedPackRecord setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes == null ? "" : errCodeDes;
        return this;
    }

    public String getMchBillno() {
        return mchBillno;
    }

    public WxRedPackRecord setMchBillno(String mchBillno) {
        this.mchBillno = mchBillno == null ? "" : mchBillno;
        return this;
    }

    public String getMchId() {
        return mchId;
    }

    public WxRedPackRecord setMchId(String mchId) {
        this.mchId = mchId == null ? "" : mchId;
        return this;
    }

    public String getWxappid() {
        return wxappid;
    }

    public WxRedPackRecord setWxappid(String wxappid) {
        this.wxappid = wxappid == null ? "" : wxappid;
        return this;
    }

    public String getReOpenid() {
        return reOpenid;
    }

    public WxRedPackRecord setReOpenid(String reOpenid) {
        this.reOpenid = reOpenid == null ? "" : reOpenid;
        return this;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public WxRedPackRecord setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public String getSendListid() {
        return sendListid;
    }

    public WxRedPackRecord setSendListid(String sendListid) {
        this.sendListid = sendListid == null ? "" : sendListid;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
