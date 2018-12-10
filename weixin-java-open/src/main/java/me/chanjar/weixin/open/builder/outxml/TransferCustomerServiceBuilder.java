package me.chanjar.weixin.open.builder.outxml;

import me.chanjar.weixin.open.bean.message.WxOpenXmlOutTransferKefuMessage;
import org.apache.commons.lang3.StringUtils;

/**
 * 客服消息builder
 * <pre>
 * 用法: WxOpenKefuMessage m = WxOpenXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().content(...).toUser(...).build();
 * </pre>
 *
 * @author chanjarster
 */
public final class TransferCustomerServiceBuilder extends BaseBuilder<TransferCustomerServiceBuilder, WxOpenXmlOutTransferKefuMessage> {
    private String kfAccount;

    public TransferCustomerServiceBuilder kfAccount(String kf) {
        this.kfAccount = kf;
        return this;
    }

    @Override
    public WxOpenXmlOutTransferKefuMessage build() {
        WxOpenXmlOutTransferKefuMessage m = new WxOpenXmlOutTransferKefuMessage();
        setCommon(m);
        if (StringUtils.isNotBlank(this.kfAccount)) {
            WxOpenXmlOutTransferKefuMessage.TransInfo transInfo = new WxOpenXmlOutTransferKefuMessage.TransInfo();
            transInfo.setKfAccount(this.kfAccount);
            m.setTransInfo(transInfo);
        }
        return m;
    }
}
