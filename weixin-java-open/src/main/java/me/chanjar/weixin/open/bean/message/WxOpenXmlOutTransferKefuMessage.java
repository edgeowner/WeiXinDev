package me.chanjar.weixin.open.bean.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.util.xml.XStreamCDataConverter;

@XStreamAlias("xml")
public class WxOpenXmlOutTransferKefuMessage extends WxOpenXmlOutMessage {
    private static final long serialVersionUID = 1850903037285841322L;

    @XStreamAlias("TransInfo")
    protected TransInfo transInfo;

    public WxOpenXmlOutTransferKefuMessage() {
        this.msgType = WxConsts.CUSTOM_MSG_TRANSFER_CUSTOMER_SERVICE;
    }

    public TransInfo getTransInfo() {
        return this.transInfo;
    }

    public void setTransInfo(TransInfo transInfo) {
        this.transInfo = transInfo;
    }

    @XStreamAlias("TransInfo")
    public static class TransInfo {

        @XStreamAlias("KfAccount")
        @XStreamConverter(value = XStreamCDataConverter.class)
        private String kfAccount;

        public String getKfAccount() {
            return this.kfAccount;
        }

        public void setKfAccount(String kfAccount) {
            this.kfAccount = kfAccount;
        }
    }
}
