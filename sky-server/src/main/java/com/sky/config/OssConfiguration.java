package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 用来生成AliOssUtil对象的工具类
 * @author: Excell
 * @data 2025年03月19日 21:29
 */
@Configuration
public class OssConfiguration {
    /**
     * @description: 用来创建阿里云工具类的Bean对象
     * @title: aliOssUtil
     * @param: [aliOssProperties]
     */
    @Bean
    @ConditionalOnMissingBean// 只创建一个Bean对象
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        return new AliOssUtil(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(), aliOssProperties.getBucketName());
    }
}
