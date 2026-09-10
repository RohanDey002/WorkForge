package com.taskmanagement.aitaskmanagement.Redis.config;

import com.taskmanagement.aitaskmanagement.DTO.response.TaskResponse;
import com.taskmanagement.aitaskmanagement.DTO.response.UserResponse;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                               ObjectMapper objectMapper){

        JavaType userResponseTypeList = objectMapper.getTypeFactory()
                .constructCollectionType(List.class,
                        UserResponse.class);

        JavaType taskResponseTypeList = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, TaskResponse.class);


        TypeJsonRedisSerializer<List<UserResponse>> userSerializer =
                new TypeJsonRedisSerializer<>(objectMapper,userResponseTypeList);

        TypeJsonRedisSerializer<List<TaskResponse>> taskSerializer =
                new TypeJsonRedisSerializer<>(objectMapper,taskResponseTypeList);



        RedisCacheConfiguration defaultCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues();

        Map<String,RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put(
                "allUsers",
                defaultCacheConfiguration
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(userSerializer)
                        )
                        .entryTtl(Duration.ofMinutes(5))
        );

        cacheConfigurations.put(
                "managerEmployees",
                defaultCacheConfiguration
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(userSerializer)
                        )
                        .entryTtl(Duration.ofMinutes(10))
        );

        /*
         * Task caches
         */
        cacheConfigurations.put(
                "employeeTasks",
                defaultCacheConfiguration
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(taskSerializer)
                        )
                        .entryTtl(Duration.ofMinutes(2))
        );

        cacheConfigurations.put(
                "managerTasks",
                defaultCacheConfiguration
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(taskSerializer)
                        )
                        .entryTtl(Duration.ofMinutes(2))
        );

       return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();

    }
}
