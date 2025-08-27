package com.aegis.config.mvc;

import com.aegis.common.file.FileUploadProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 静态资源配置
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUploadProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置本地文件访问路径
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + properties.getLocal().getPath() + "/");
    }
}
