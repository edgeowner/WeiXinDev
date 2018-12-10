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

// ------------------------------------------------------------------------

/**
 * 针对org.apache.commons.codec.binary.Base64，
 * 需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本）
 * 官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
 */
package me.chanjar.weixin.open.util.crypto;

import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class WxOpenCryptUtil extends me.chanjar.weixin.common.util.crypto.WxCryptUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxOpenCryptUtil.class);

    /**
     * 构造函数
     *
     * @param wxOpenConfigStorage
     */
    public WxOpenCryptUtil(WxOpenConfigStorage wxOpenConfigStorage) {
    /*
     * @param token          公众平台上，开发者设置的token
     * @param encodingAesKey 公众平台上，开发者设置的EncodingAESKey
     * @param appId          公众平台appid
     */
        String encodingAesKey = wxOpenConfigStorage.getComponentEncodingAesKey();
        String token = wxOpenConfigStorage.getComponentToken();
        String appId = wxOpenConfigStorage.getComponentAppId();

        this.token = token;
        this.appidOrCorpid = appId;
        this.aesKey = Base64.decodeBase64(encodingAesKey + "=");
    }


    /**
     * 判断是否加密
     *
     * @param token
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean verifySignature(String token, String signature, String timestamp, String nonce) {
        if (StringUtils.isAnyBlank(signature, timestamp, nonce)) {
            return false;
        }

        String sha1 = "";
        String[] arrays = new String[]{token, timestamp, nonce};
        Arrays.sort(arrays);
        for (String param : arrays) {
            sha1 += param;
        }

        String local_signature = "";
        try {
            String tempStr = "";
            MessageDigest md = MessageDigest.getInstance("SHA-1");     //选择SHA-1，也可以选择MD5
            byte[] digests = md.digest(sha1.getBytes());       //返回的是byet[]，要转化为String存储比较方便

            for (byte digest : digests) {
                tempStr = (Integer.toHexString(digest & 0xff));
                if (tempStr.length() == 1) {
                    local_signature = local_signature + "0" + tempStr;
                } else {
                    local_signature = local_signature + tempStr;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }

        return Objects.equals(signature, local_signature.toLowerCase());
    }


}
