package com.aegis.config.jackson;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 配置类，解决前端 Long 类型精度丢失问题
 *
 * @author xuesong.lei
 * @since 2025-09-08
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            // 统一将 Long 类型转换为 String
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            // 处理 long 基本类型
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        };
    }
}
