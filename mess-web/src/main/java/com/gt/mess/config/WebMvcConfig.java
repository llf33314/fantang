package com.gt.mess.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * SpringMVC 配置类
 *
 * @author zengwx
 * @create 2017/6/20
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    //快速解决页面转向问题
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
	registry.addViewController("/").setViewName("/index.html");
	registry.addViewController("/error").setViewName("/error/defaultError.html");
    }

    /**
     * 静态资源访问地址修改
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry.addResourceHandler("/**").addResourceLocations("classpath:/page/").addResourceLocations("classpath:/static/");
	super.addResourceHandlers(registry);
    }
}
