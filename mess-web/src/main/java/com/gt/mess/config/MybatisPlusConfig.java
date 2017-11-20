package com.gt.mess.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-plus 配置类
 *
 * @author zengwx
 * @create 2017/6/20
 */
@MapperScan( "com.gt.mess.dao" )
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件
     *
     * @return PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
	PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
	// 开启 PageHelper 的支持
	paginationInterceptor.setLocalPage(true);
	return paginationInterceptor;
    }
}
