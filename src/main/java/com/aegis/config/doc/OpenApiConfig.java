package com.aegis.config.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc配置类
 *
 * @author xuesong.lei
 * @since 2025/9/7 14:14
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact();
        contact.setName("xuesong.lei");
        contact.setUrl("http://127.0.0.1:8080");
        contact.setEmail("aegis_system@163.com");

        return new OpenAPI()
                .info(new Info()
                        .title("aegis")
                        .version("1.0")
                        .description("aegis 系统 是一款基于角色的访问控制（RBAC）权限管理系统，采用 Spring Boot + Vue 前后端分离架构")
                        .contact(contact)
                );
    }
}
