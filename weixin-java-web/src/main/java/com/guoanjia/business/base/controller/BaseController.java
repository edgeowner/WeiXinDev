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

package com.guoanjia.business.base.controller;

import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.WxOpenService;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@RestControllerAdvice
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final WxOpenService wxOpenService;
    protected WxOpenConfigStorage wxOpenConfigStorage;
    protected JedisPool jedisPool;

    public BaseController(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
        this.wxOpenConfigStorage = this.wxOpenService.getWxOpenConfigStorage();
        this.jedisPool = this.wxOpenConfigStorage.getJedisPool();
    }


    protected String set(String key, Serializable object) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key.getBytes(), SerializationUtils.serialize(object));
        }
    }

    protected Object get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            try {
                byte[] value = jedis.get(key.getBytes());
                return SerializationUtils.deserialize(value);
            }catch (Exception e){
                return null;
            }
        }
    }

    protected String getString(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    protected Long llen(String key){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.llen(key);
        }
    }


    protected boolean del(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key.getBytes()) > 0;
        }
    }

    protected void set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    protected String lpop(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpop(key);
        }
    }

    protected Long rpush(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpush(key, value);
        }
    }

    protected void expire(String key, int value){
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.expire(key, value);
        }
    }


    public static class BaseParameters implements Serializable {
        private String openid;
        private String userphone;

        public String getOpenid() {
            return openid;
        }

        public BaseParameters setOpenid(String openid) {
            this.openid = openid;
            return this;
        }

        public String getUserphone() {
            return userphone;
        }

        public BaseParameters setUserphone(String userphone) {
            this.userphone = userphone;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }
    }

    public static class CheckValidateCodeParameters extends BaseParameters implements Serializable {

        private String validateCode;

        public String getValidateCode() {
            return validateCode;
        }

        public CheckValidateCodeParameters setValidateCode(String validateCode) {
            this.validateCode = validateCode;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE).concat(super.toString());
        }
    }


    public static class RedPackParameters implements Serializable {
        private String openid;


        public String getOpenid() {
            return openid;
        }

        public RedPackParameters setOpenid(String openid) {
            this.openid = openid;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }
    }

    public static class GuessParameters implements Serializable {
        private String phonenum;

        public String getPhonenum() {
            return phonenum;
        }

        public GuessParameters setPhonenum(String phonenum) {
            this.phonenum = phonenum;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }
    }


}
