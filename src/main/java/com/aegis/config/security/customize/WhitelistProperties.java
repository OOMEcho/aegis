package com.aegis.config.security.customize;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/30 22:00
 * @Description: 白名单配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "whitelist")
public class WhitelistProperties {

    private List<String> urls = new ArrayList<>();
}
