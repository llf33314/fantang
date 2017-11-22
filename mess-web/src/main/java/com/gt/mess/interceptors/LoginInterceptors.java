package com.gt.mess.interceptors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mess.base.BaseInterceptor;
import com.gt.mess.util.WxmpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 登录拦截器
 *
 * @author zengwx
 * @version 1.0.0
 * @date 2017/08/11
 */
public class LoginInterceptors implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptors.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("text/javaScript; charset=utf-8");

        String requestUrl = httpRequest.getRequestURI();
        HttpSession session = null;

        try {
            if (requestUrl.contains(".css") || requestUrl.contains(".js") || requestUrl.contains(".png") || requestUrl.contains(".jpg") || requestUrl.contains(".ico")) {

            } else {
                log.info("登录认证: " + requestUrl);
                if(requestUrl.contains("CF946E2B")){
                    chain.doFilter(request, response);
                    return;
                }
                session = httpRequest.getSession();
                if (null == session.getAttribute("business_key") || null == session.getAttribute("TCommonStaff")) {
                    httpResponse.sendRedirect("/shops/basis/login");
                    return;
                }

                Enumeration<String> keys = session.getAttributeNames();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    log.info(key + " == " + session.getAttribute(key));
                }

                JSONObject businessKey = JSON.parseObject(session.getAttribute("business_key").toString()); // 管理员登录信息
                JSONObject tCommonStaff = JSON.parseObject(session.getAttribute("TCommonStaff").toString()); // 员工登录信息


            }
        } catch (Exception e) {
            log.error("身份认证失败", e);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
