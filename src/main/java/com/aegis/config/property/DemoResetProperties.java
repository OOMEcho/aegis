package com.aegis.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/3 11:57
 * @Description: 演示数据重置配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "demo.reset")
public class DemoResetProperties {

    /**
     * 是否启用演示数据重置功能
     * 仅在演示环境中启用，生产环境请设置为 false
     */
    private boolean enabled = false;

    /**
     * 重置执行的 cron 表达式
     * 默认每小时整点执行
     */
    private String cron = "0 0 * * * ?";
}
