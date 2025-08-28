package com.aegis.common.file.config;

import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/28 10:12
 * @Description: 阿里云OSS配置类
 */
@Configuration
public class AliyunOssConfig {

    @Bean
    public OSS ossClient(FileUploadProperties properties) {
        FileUploadProperties.AliyunConfig config = properties.getAliyun();

        OSS oss = new OSSClientBuilder().build(
                config.getEndpoint(),
                config.getAccessKeyId(),
                config.getAccessKeySecret());

        // 确保 bucket 存在
        try {
            if (!oss.doesBucketExist(config.getBucketName())) {
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(config.getBucketName());
                // 根据需要设置存储类型/权限
                createBucketRequest.setCannedACL(CannedAccessControlList.Private);
                oss.createBucket(createBucketRequest);
            }
        } catch (Exception e) {
            throw new BusinessException("OSS Bucket 初始化失败: " + e.getMessage());
        }

        return oss;
    }
}
