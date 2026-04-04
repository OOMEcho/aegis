package com.aegis.common.trace;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TraceId 过滤器，为每个请求生成或传递 TraceId
 *
 * @author xuesong.lei
 * @since 2025-12-01
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 优先从请求头获取 TraceId（支持分布式链路传递）
            String traceId = request.getHeader(TraceIdUtils.TRACE_ID_HEADER);

            // 如果请求头没有，则生成新的 TraceId
            if (!StringUtils.hasText(traceId)) {
                traceId = TraceIdUtils.generateTraceId();
            }

            // 设置到 MDC 中
            TraceIdUtils.setTraceId(traceId);

            // 在响应头中返回 TraceId，便于前端或调用方追踪
            response.setHeader(TraceIdUtils.TRACE_ID_HEADER, traceId);

            filterChain.doFilter(request, response);
        } finally {
            // 请求结束后清除 MDC，防止线程复用时数据污染
            TraceIdUtils.removeTraceId();
        }
    }
}
