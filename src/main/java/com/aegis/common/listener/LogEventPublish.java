package com.aegis.common.listener;

import com.aegis.modules.log.domain.entity.SysOperateLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 14:43
 * @Description: 事件发布
 */
@Component
@RequiredArgsConstructor
public class LogEventPublish {

    private final ApplicationContext applicationContext;

    public void publishEvent(SysOperateLog sysOperateLog) {
        applicationContext.publishEvent(sysOperateLog);
    }
}
