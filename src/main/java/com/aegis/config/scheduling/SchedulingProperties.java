package com.aegis.config.scheduling;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务配置类
 *
 * @author xuesong.lei
 * @since 2025-09-21
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "task.scheduling")
public class SchedulingProperties {

    /**
     * 是否开启定时任务
     */
    private boolean enabled = true;
}
