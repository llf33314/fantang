package com.gt.mess.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截器父类
 * <pre>
 *     项目所有拦截器继承这里
 * </pre>
 *
 * @author zengwx
 * @version 1.0.0
 * @date 2017/08/11
 */
public class BaseInterceptor extends HandlerInterceptorAdapter {

    /**
     * 日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);

}
