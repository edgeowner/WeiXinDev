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


package com.github.asherli0103.core.resolver;

import com.github.asherli0103.core.entity.AjaxJson;
import com.github.asherli0103.core.exception.BusinessRuntimeException;
import com.github.asherli0103.utils.JacksonUtil;
import com.github.asherli0103.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常拦截器
 *
 * @author AsherLi
 * @version V1.0.00
 */
//@Component
public class GlobalExceptionResolver extends ExceptionHandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
    //记录数据库最大字符长度
    private static final int WIRTE_DB_MAX_LENGTH = 1500;
    //记录数据库最大字符长度
    private static final short LOG_LEVEL = 6;
    //记录数据库最大字符长度
    private static final short LOG_OPT = 3;

    /**
     * 对异常信息进行统一处理，区分异步和同步请求，分别处理
     */
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        boolean isajax = isAjax(request, response);
        Throwable deepestException = deepestException(ex);
        return processException(request, response, handler, deepestException, isajax);
    }

    /**
     * 判断当前请求是否为异步请求.
     */
    private boolean isAjax(HttpServletRequest request, HttpServletResponse response) {
        return StringUtil.isNotEmpty(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取最原始的异常出处，即最初抛出异常的地方
     */
    private Throwable deepestException(Throwable e) {
        Throwable tmp = e;
        int breakPoint = 0;
        while (tmp.getCause() != null) {
            if (tmp.equals(tmp.getCause())) {
                break;
            }
            tmp = tmp.getCause();
            breakPoint++;
            if (breakPoint > 1000) {
                break;
            }
        }
        return tmp;
    }

    /**
     * 处理异常.
     *
     * @param request
     * @param response
     * @param handler
     * @param isajax
     * @return
     */
    private ModelAndView processException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                          Throwable ex, boolean isajax) {
        //步骤一、异常信息记录到日志文件中.
        logger.error("全局处理异常捕获:", ex);
        //步骤二、异常信息记录截取前50字符写入数据库中.
        //logDb(ex);
        //步骤三、分普通请求和ajax请求分别处理.
        if (isajax) {
            return processAjax(request, response, handler, ex);
        } else {
            return processNotAjax(request, response, handler, ex);
        }
    }

    /**
     * 异常信息记录截取前50字符写入数据库中
     *
     * @param ex
     */
    private void logDb(Throwable ex) {
        //String exceptionMessage = getThrowableMessage(ex);
        String exceptionMessage = "错误异常: " + ex.getClass().getSimpleName() + ",错误描述：" + ex.getMessage();
        if (StringUtil.isNotEmpty(exceptionMessage)) {
            if (exceptionMessage.length() > WIRTE_DB_MAX_LENGTH) {
                exceptionMessage = exceptionMessage.substring(0, WIRTE_DB_MAX_LENGTH);
            }
        }
        //systemService.addLog(exceptionMessage, LOG_LEVEL, LOG_OPT);
    }

    /**
     * ajax异常处理并返回.
     *
     * @param request
     * @param response
     * @param handler
     * @param deepestException
     * @return
     */
    private ModelAndView processAjax(HttpServletRequest request, HttpServletResponse response, Object handler,
                                     Throwable deepestException) {
        ModelAndView empty = new ModelAndView();
        //response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        AjaxJson json = new AjaxJson();
        json.setSuccess(false);
        json.setMsg(deepestException.getMessage());
        if (deepestException instanceof BusinessRuntimeException) {
            json.setCode(((BusinessRuntimeException) deepestException).getStatus());
        }
        try {
            PrintWriter pw = response.getWriter();
            pw.write(JacksonUtil.toJSONString(json));
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        empty.clear();
        return empty;
    }

    /**
     * 普通页面异常处理并返回.
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    private ModelAndView processNotAjax(HttpServletRequest request, HttpServletResponse response, Object handler,
                                        Throwable ex) {
        String exceptionMessage = getThrowableMessage(ex);
        Map<String, Object> model = new HashMap<>();
        model.put("exceptionMessage", exceptionMessage);
        model.put("url", request.getRequestURL());
        model.put("ex", ex);
        return new ModelAndView("/error/error", model);
    }

    /**
     * 返回错误信息字符串
     *
     * @param ex Exception
     * @return 错误信息字符串
     */
    public String getThrowableMessage(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

}
