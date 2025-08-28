package com.aegis.common.file.config;

import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/28 10:24
 * @Description: Minio配置类
 */
@Configuration
@ConditionalOnProperty(prefix = "file.upload", name = "platform", havingValue = "minio")
public class MinioConfig {

    @Bean
    public MinioClient minioClient(FileUploadProperties properties) {
        FileUploadProperties.MinioConfig config = properties.getMinio();

        MinioClient minioClient = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();

        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(config.getBucketName()).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(config.getBucketName()).build()
                );
            }
        } catch (Exception e) {
            throw new BusinessException("MinIO Bucket 初始化失败: " + e.getMessage());
        }

        return minioClient;
    }
}
