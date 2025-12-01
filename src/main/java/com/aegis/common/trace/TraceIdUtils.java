package com.aegis.common.trace;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * @Author: xuesong.lei
 * @Date: 2025/12/01 09:26
 * @Description: TraceId 工具类，用于全链路日志追踪
 */
public final class TraceIdUtils {

    /**
     * MDC 中 TraceId 的 key
     */
    public static final String TRACE_ID_KEY = "traceId";

    /**
     * HTTP 请求头中 TraceId 的 key
     */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    private TraceIdUtils() {
    }

    /**
     * 生成 TraceId
     * 使用 UUID 去掉横线，生成32位字符串
     *
     * @return traceId
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前线程的 TraceId
     *
     * @return traceId，如果不存在返回 null
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 设置当前线程的 TraceId
     *
     * @param traceId traceId
     */
    public static void setTraceId(String traceId) {
        if (traceId != null && !traceId.isEmpty()) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    /**
     * 移除当前线程的 TraceId
     */
    public static void removeTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }

    /**
     * 清空 MDC 上下文
     */
    public static void clear() {
        MDC.clear();
    }
}
