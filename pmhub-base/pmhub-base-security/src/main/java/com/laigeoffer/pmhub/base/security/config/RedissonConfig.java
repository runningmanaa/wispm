package com.laigeoffer.pmhub.base.security.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zw
 * @description RedissonConfig
 * @create 2024-06-18-16:36
 */
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://47.122.66.174:6379")
                .setDatabase(0);

        return Redisson.create(config);
    }
}
