package com.aegis.common.listener;

import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.modules.log.mapper.SysOperateLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 14:44
 * @Description: 事件监听
 */
@Component
@RequiredArgsConstructor
public class LogEventListener {

    private final SysOperateLogMapper sysOperateLogMapper;

    @Async
    @EventListener(SysOperateLog.class)
    public void onApplicationEvent(SysOperateLog sysOperateLog) {
        sysOperateLogMapper.insert(sysOperateLog);
    }
}
