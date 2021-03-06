package com.gt.mess.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * Redis 缓存配置
 *
 * @author zengwx
 * @version 1.0.0
 * @date 2017/07/16
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Bean
    public RedisTemplate< String, String > redisTemplate(RedisConnectionFactory cf) {
	LOG.debug("注入StringRedisTemplate");
	RedisTemplate< String, String > redisTemplate = new RedisTemplate<>();
	redisTemplate.setConnectionFactory(cf);
	Jackson2JsonRedisSerializer< Object > jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
	ObjectMapper om = new ObjectMapper();
	om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	jackson2JsonRedisSerializer.setObjectMapper(om);
	redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);//如果key是String 需要配置一下StringSerializer,不然key会乱码 /XX/XX
	redisTemplate.afterPropertiesSet();
	return redisTemplate;
    }
    /*@Bean
    public CacheManager cacheManager( RedisTemplate redisTemplate ) {
	RedisCacheManager cacheManager = new RedisCacheManager( redisTemplate );
	//默认超时时间,单位秒
	cacheManager.setDefaultExpiration( 3000 );
	//根据缓存名称设置超时时间,0为不超时
	Map< String,Long > expires = new ConcurrentHashMap<>();
	cacheManager.setExpires( expires );
	return cacheManager;
    }
*/
}
