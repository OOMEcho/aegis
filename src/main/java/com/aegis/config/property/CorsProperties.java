package com.aegis.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/3 10:24
 * @Description: CORS 跨域配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    /**
     * 允许的源地址列表
     * <p>
     * 示例：
     * - http://localhost:9090
     * - https://aegis.example.cn
     * <p>
     * 注意：不能使用 * 通配符，因为启用了 allowCredentials
     */
    private List<String> allowedOrigins;
}
