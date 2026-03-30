package com.aegis.common.file.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云COS配置类
 *
 * @author xuesong.lei
 * @since 2025/8/28 10:12
 */
@Configuration
@ConditionalOnProperty(prefix = "file.upload", name = "platform", havingValue = "tencent_cos")
public class TencentCosConfig {

    @Bean
    public COSClient cosClient(FileUploadProperties properties) {
        FileUploadProperties.TencentConfig config = properties.getTencent();

        COSCredentials cred = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(config.getRegion()));
        return new COSClient(cred, clientConfig);
    }
}
