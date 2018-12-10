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

package me.chanjar.weixin.open.bean.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import me.chanjar.weixin.common.util.xml.XStreamCDataConverter;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.util.crypto.WxOpenCryptUtil;
import me.chanjar.weixin.open.util.xml.XStreamTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@XStreamAlias("xml")
public class WxOpenXmlAuthorizeMessage implements Serializable {

    private static final long serialVersionUID = -3586245291677274984L;
    @XStreamAlias("AppId")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String appId;

    @XStreamAlias("CreateTime")
    private String createTime;

    @XStreamAlias("InfoType")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String infoType;

    @XStreamAlias("ComponentVerifyTicket")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String componentVerifyTicket;


    public static WxOpenXmlAuthorizeMessage fromXml(String xml) {
        return XStreamTransformer.fromXml(WxOpenXmlAuthorizeMessage.class, xml);
    }

    public static WxOpenXmlAuthorizeMessage fromXml(InputStream is) {
        return XStreamTransformer.fromXml(WxOpenXmlAuthorizeMessage.class, is);
    }

    public static WxOpenXmlAuthorizeMessage fromEncryptedXml(String encryptedXml, WxOpenConfigStorage wxOpenConfigStorage, String timestamp, String nonce, String msgSignature) {
        WxOpenCryptUtil cryptUtil = new WxOpenCryptUtil(wxOpenConfigStorage);
        String plainText = cryptUtil.decrypt(msgSignature, timestamp, nonce, encryptedXml);
        return fromXml(plainText);
    }

    public static WxOpenXmlAuthorizeMessage fromEncryptedXml(InputStream is, WxOpenConfigStorage wxOpenConfigStorage, String timestamp, String nonce, String msgSignature) {
        try {
            return fromEncryptedXml(IOUtils.toString(is, "UTF-8"), wxOpenConfigStorage, timestamp, nonce, msgSignature);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAppId() {
        return appId;
    }

    public WxOpenXmlAuthorizeMessage setAppId(String appId) {
        this.appId = appId == null ? "" : appId;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public WxOpenXmlAuthorizeMessage setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getInfoType() {
        return infoType;
    }

    public WxOpenXmlAuthorizeMessage setInfoType(String infoType) {
        this.infoType = infoType == null ? "" : infoType;
        return this;
    }

    public String getComponentVerifyTicket() {
        return componentVerifyTicket;
    }

    public WxOpenXmlAuthorizeMessage setComponentVerifyTicket(String componentVerifyTicket) {
        this.componentVerifyTicket = componentVerifyTicket == null ? "" : componentVerifyTicket;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
