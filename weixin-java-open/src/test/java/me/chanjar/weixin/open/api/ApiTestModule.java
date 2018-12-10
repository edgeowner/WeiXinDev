package me.chanjar.weixin.open.api;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.thoughtworks.xstream.XStream;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;

public class ApiTestModule implements Module {

    @Override
    public void configure(Binder binder) {
        try (InputStream is1 = ClassLoader.getSystemResourceAsStream("test-config.xml")) {
            WxXmlOpenInMemoryConfigStorage config = this.fromXml(WxXmlOpenInMemoryConfigStorage.class, is1);
            config.setAccessTokenLock(new ReentrantLock());
            WxOpenService wxService = new WxOpenServiceImpl();
            wxService.setWxOpenConfigStorage(config);

            binder.bind(WxOpenService.class).toInstance(wxService);
            binder.bind(WxOpenConfigStorage.class).toInstance(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T fromXml(Class<T> clazz, InputStream is) {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.alias("xml", clazz);
        xstream.processAnnotations(clazz);
        return (T) xstream.fromXML(is);
    }

}
