package com.gt.mess.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * RedisSession配置Config 默认session超时时间 1小时 3600秒
 *
 * @author zengwx
 * @version 1.0.0
 * @date 2017/07/16
 */
@Configuration
@EnableRedisHttpSession( maxInactiveIntervalInSeconds = 3600 )
public class RedisSessionConfig {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(RedisSessionConfig.class);
    // 注入配置属性 根据环境配置切换
    @Value( "${redisSession.cookieName}" )
    private String cookieName;
    @Value( "${redisSession.cookiePath}" )
    private String cookiePath;
    @Value( "${redisSession.domainName}" )
    private String domainName;

    /**
     * 设置Cookie作用于
     *
     * @return DefaultCookieSerializer
     */
    @Bean( name = "defaultCookieSerializer" )
    public DefaultCookieSerializer defaultCookieSerializer() {
	LOG.debug("domainName:{},cookieName:{},cookiePath:{} ", domainName, cookieName, cookiePath);
	DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
	//	cookieSerializer.setDomainName( domainName );
	cookieSerializer.setCookieName(cookieName);
	cookieSerializer.setCookiePath(cookiePath);
	return cookieSerializer;
    }

}
