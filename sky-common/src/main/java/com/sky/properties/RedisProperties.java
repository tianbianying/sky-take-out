package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: Redis的配置信息
 * @author: Excell
 * @data 2025年03月28日 09:39
 */
@Data
@Component
@ConfigurationProperties(prefix = "sky.redis")
public class RedisProperties {
    private String host;
    private Integer port;
    private String password;
    private LettucePool lettucePool = new LettucePool();

    @Data
    public static class LettucePool {
        private Integer maxActive;
        private Integer maxIdle;
        private Integer minIdle;
        private String timeBetweenEvictionRuns;
    }
}
