package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: redis配置参数
 * @author: Excell
 * @data 2025年03月24日 21:55
 */
@Component
@ConfigurationProperties(prefix = "sky.redis")
@Data
public class RedisProperties {
    private String host;
    private Integer port;
    private String password;
}
