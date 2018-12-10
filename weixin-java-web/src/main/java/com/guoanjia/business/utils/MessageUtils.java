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

package com.guoanjia.business.utils;

import com.github.asherli0103.utils.StringUtil;
import com.github.asherli0103.utils.VallidateUtil;
import com.github.asherli0103.utils.encrytion.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 短信发送工具类
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
public class MessageUtils {
    // 匹配手机号 <br>
    private static final String SMS_URL = "http://q.hl95.com:8061/";
    private static final String SMS_DEFAULT_CHARSET = "GB2312";
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);


    /**
     * 短信发送接口
     *
     * @param modelMessage 短信内容
     * @param phoneNumber  接受者号码
     * @return 发送结果
     */
    public static String messagePostRequest(String modelMessage, String phoneNumber) {
        String username = "xzgadc"; //util.readProperty("username");
        String password = "Gadc836121";// util.readProperty("password");
        String epid = "121638";//util.readProperty("epid");

        //密码MD5加密
        password = MD5.encryption(password);

        InputStream is = null;
        StringBuilder builder = new StringBuilder();
        try {
            //信息内容进行编码,防止传输乱码
            String covertMessage = URLEncoder.encode(modelMessage, SMS_DEFAULT_CHARSET);
            //URL地址生成
            String url = SMS_URL +
                    "?username=" + username +
                    "&password=" + password +
                    "&message=" + covertMessage +
                    "&phone=" + phoneNumber +
                    "&epid=" + epid +
                    "&linkid=&subcode=";

            //连接HL95发送信息
            SimpleClientHttpRequestFactory schr = new SimpleClientHttpRequestFactory();
            ClientHttpRequest chr = schr.createRequest(new URI(url), HttpMethod.GET);
            ClientHttpResponse res = chr.execute();

            //获取返回消息
            is = res.getBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            if (logger.isInfoEnabled()) {
                logger.info("短信发送详细信息: " + builder + " \n规则: \n\t00[成功],\n\t1[参数不完整]," +
                        "\n\t2[鉴权失败],\n\t3[批量发出数据超过50],\n\t4[发送失败],\n\t5[余额不足]," +
                        "\n\t6[发送内容含屏蔽词],\n\t7[短信内容超过350字符]");
            }

        } catch (URISyntaxException e) {
            logger.error("URI解析异常: ", e);
        } catch (IOException e) {
            logger.error("返回结果读取异常: ", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //判断返回信息结果
        return builder.toString();
    }

}
