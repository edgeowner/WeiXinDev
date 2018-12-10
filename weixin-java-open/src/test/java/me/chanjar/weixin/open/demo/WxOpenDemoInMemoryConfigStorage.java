package me.chanjar.weixin.open.demo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import me.chanjar.weixin.open.api.WxOpenInMemoryConfigStorage;

import java.io.InputStream;

/**
 * @author Daniel Qian
 */
@XStreamAlias("xml")
class WxOpenDemoInMemoryConfigStorage extends WxOpenInMemoryConfigStorage {

    public static WxOpenDemoInMemoryConfigStorage fromXml(InputStream is) {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxOpenDemoInMemoryConfigStorage.class);
        return (WxOpenDemoInMemoryConfigStorage) xstream.fromXML(is);
    }

    @Override
    public String toString() {
        return "SimpleWxConfigProvider [appId=" + this.appId + ", secret=" + this.componentAppSecret + ", accessToken=" + this.accessToken
                + ", expiresTime=" + this.expiresTime + ", token=" + this.componentToken + ", aesKey=" + this.componentEncodingAesKey + "]";
    }

}
