package com.aegis.common.trace;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @Author: xuesong.lei
 * @Date: 2025/12/01 09:32
 * @Description: MDC 上下文传递装饰器，用于在异步任务中传递 TraceId
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 获取主线程的 MDC 上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // 将主线程的 MDC 上下文设置到子线程
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                // 执行完毕后清除子线程的 MDC 上下文
                MDC.clear();
            }
        };
    }
}
