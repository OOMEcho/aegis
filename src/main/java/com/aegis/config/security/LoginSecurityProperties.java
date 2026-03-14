package com.aegis.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xuesong.lei
 * @Date: 2025/1/11
 * @Description: 登录安全配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.login")
public class LoginSecurityProperties {

    /**
     * 是否启用滑块验证码校验
     */
    private boolean enableCaptcha = true;

    /**
     * 是否启用密码加密
     */
    private boolean enablePasswordEncryption = true;

    /**
     * Cookie 是否启用 Secure 标志（生产环境应设为 true）
     */
    private boolean cookieSecure = false;

    /*
     * refreshToken Cookie 路径
     */
    private String cookiePath;
}
