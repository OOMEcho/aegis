package com.aegis.config.scheduling;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置
 *
 * @author xuesong.lei
 * @since 2025/9/21 15:35
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "task.scheduling", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SchedulingConfig {
    // 只有 task.scheduling.enabled=true 时才会加载定时任务
}
