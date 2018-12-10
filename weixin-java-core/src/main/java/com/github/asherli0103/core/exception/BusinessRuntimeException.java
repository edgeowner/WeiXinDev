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

package com.github.asherli0103.core.exception;


import com.github.asherli0103.utils.StringUtil;
import com.github.asherli0103.utils.exception.StatefulException;

/**
 * 业务异常类
 *
 * @author AsherLi
 * @version V1.0.00
 */
public class BusinessRuntimeException extends StatefulException {
    static final long serialVersionUID = 1L;

    private int status = 0;

    public BusinessRuntimeException() {
    }

    public BusinessRuntimeException(String msg) {
        super(msg);
    }

    public BusinessRuntimeException(String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params));
    }

    public BusinessRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public BusinessRuntimeException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public BusinessRuntimeException(int status, String msg) {

        super(msg);
        this.status = status;
    }

    public BusinessRuntimeException(int status, Throwable throwable) {
        super(throwable);
        this.status = status;
    }

    public BusinessRuntimeException(int status, String msg, Throwable throwable) {
        super(msg, throwable);
        this.status = status;
    }

    /**
     * @return 获得异常状态码
     */
    public int getStatus() {
        return status;
    }

}
