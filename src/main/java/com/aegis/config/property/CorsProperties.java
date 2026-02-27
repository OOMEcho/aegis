package com.aegis.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
     * 允许的源地址（逗号分隔）
     * <p>
     * 示例：https://aegis.example.cn,https://admin.example.cn
     * <p>
     * 注意：不能使用 * 通配符，因为启用了 allowCredentials
     */
    private String allowedOrigins;

    /**
     * 将逗号分隔的字符串解析为 List
     *
     * @return 允许的源地址列表
     */
    public List<String> getAllowedOriginList() {
        if (allowedOrigins == null || allowedOrigins.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
