package cn.xanderye.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/7 9:04
 */
@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("serviceConfig");
        cacheNames.add("concat");
        cacheNames.add("bot");
        return RedisCacheManager.builder(factory).initialCacheNames(cacheNames).build();
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //使用Fastjson2JsonRedisSerializer来序列化和反序列化redis的value值
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        return redisTemplate;
    }
}
