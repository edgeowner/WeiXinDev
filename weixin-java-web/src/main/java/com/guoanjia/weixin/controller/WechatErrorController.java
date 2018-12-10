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

package com.guoanjia.weixin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
@Controller
public class WechatErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(WechatErrorController.class);
    private final static String ERROR_PATH = "/error";
    private static WechatErrorController appErrorController;
    /**
     * Error Attributes in the Application
     */
    private ErrorAttributes errorAttributes;

    /**
     * Controller for the Error Controller
     *
     * @param errorAttributes 异常属性
     */
    @Autowired
    public WechatErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    public WechatErrorController() {
        if (appErrorController == null) {
            appErrorController = new WechatErrorController(this.errorAttributes);
        }
    }

    /**
     * Supports the HTML Error View
     *
     * @param request 请求流
     * @return model
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {
        return new ModelAndView("error", this.getErrorAttributes(request, false));
    }

    /**
     * Supports other formats like JSON, XML
     *
     * @param request 请求流
     * @return data
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = this.getErrorAttributes(request, this.getTraceParameter(request));
        HttpStatus status = this.getStatus(request);
        return new ResponseEntity<>(body, status);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @SuppressWarnings("static-method")
    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        return parameter != null && !"false".equals(parameter.toLowerCase());

    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String, Object> map = this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
        logger.error("map is [{}]", map);
        String url = request.getRequestURL().toString();
        map.put("URL", url);
        logger.error("[error info]: status-{}, request url-{}", map.get("status"), url);
        return map;
    }

    @SuppressWarnings("static-method")
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            return HttpStatus.valueOf(statusCode);
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
