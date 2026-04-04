package com.aegis.config.knife4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Knife4j配置类
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        // 网站标题
                        .title("aegis")
                        // 描述 可以穿插md语法
                        .description("aegis 系统 是一款基于角色的访问控制（RBAC）权限管理系统，采用 Spring Boot + Vue 前后端分离架构")
                        // 设置作者 服务器url 邮箱
                        .contact(new Contact("xuesong.lei", "http://127.0.0.1:8080", "aegis_system@163.com"))
                        // 版本
                        .version("1.0")
                        .build())
                .select()
                // 要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.aegis"))
                // 要扫描的url
                .paths(PathSelectors.any())
                .build();
    }
}
