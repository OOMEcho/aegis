package com.aegis.common.repeatable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 拦截需要重复读取的请求
 *
 * @author xuesong.lei
 * @since 2025/9/7 22:55
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RepeatableRequestFilter implements Filter {

    private static final Set<String> METHODS_WITH_BODY = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("POST", "PUT", "PATCH")));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest && shouldWrapRequest((HttpServletRequest) request)) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            try {
                RepeatableHttpServletRequestWrapper cachedRequest = new RepeatableHttpServletRequestWrapper(httpRequest);
                chain.doFilter(cachedRequest, response);
                return;
            } catch (IOException e) {
                log.warn("Failed to create repeatable request wrapper for URI: {}, Method: {}",
                        httpRequest.getRequestURI(), httpRequest.getMethod(), e);
            }
        }

        chain.doFilter(request, response);
    }

    private boolean shouldWrapRequest(HttpServletRequest request) {
        if (request instanceof RepeatableHttpServletRequestWrapper) {
            return false;
        }

        String method = request.getMethod();

        if (!METHODS_WITH_BODY.contains(method.toUpperCase(Locale.ROOT))) {
            return false;
        }

        String contentType = request.getContentType();
        // 排除文件上传
        return contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("multipart/");
    }
}
