package com.gt.mess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 多粉饭堂服务 程序入口
 *
 * @author zengwx
 * @create 2017/7/8
 */
@ServletComponentScan
@SpringBootApplication
public class MessApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	return application.sources(MessApplication.class);
    }

    public static void main(String[] args) {
	SpringApplication.run(MessApplication.class, args);
    }
}
