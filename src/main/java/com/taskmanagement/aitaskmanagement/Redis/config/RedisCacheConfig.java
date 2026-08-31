package com.taskmanagement.aitaskmanagement.Redis.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){

        RedisCacheConfiguration defaultCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(
                                                GenericJacksonJsonRedisSerializer.builder().build()
                                                )
                        )
                        .disableCachingNullValues();

        Map<String,RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put(
                "employeeTasks",
                defaultCacheConfiguration
                        .entryTtl(Duration.ofMinutes(2))
        );

        cacheConfigurations.put(
                "managerEmployees",
                defaultCacheConfiguration
                        .entryTtl(Duration.ofMinutes(10))
        );

        cacheConfigurations.put(
                "managerTasks",
                defaultCacheConfiguration
                        .entryTtl(Duration.ofMinutes(5))
        );

        cacheConfigurations.put(
                "adminUsers",
                defaultCacheConfiguration
                        .entryTtl(Duration.ofMinutes(2))
        );

       return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();

    }
}
