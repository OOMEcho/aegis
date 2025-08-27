package com.aegis.common.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 文件上传配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {

    private StoragePlatform platform = StoragePlatform.LOCAL;
    private LocalConfig local = new LocalConfig();
    private MinioConfig minio = new MinioConfig();
    private AliyunConfig aliyun = new AliyunConfig();
    private TencentConfig tencent = new TencentConfig();

    @Data
    public static class LocalConfig {
        private String path = "/tmp/uploads/";
    }

    @Data
    public static class MinioConfig {
        private String endpoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
    }

    @Data
    public static class AliyunConfig {
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String bucketName;
    }

    @Data
    public static class TencentConfig {
        private String region;
        private String secretId;
        private String secretKey;
        private String bucketName;
    }
}
