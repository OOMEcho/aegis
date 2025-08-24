package com.aegis.config.minio;

import com.aegis.utils.MinioUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/24 18:38
 * @Description: Minio配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    @Bean
    public MinioUtils creatMinioClient() {
        return new MinioUtils(endpoint, bucketName, accessKey, secretKey);
    }

}
