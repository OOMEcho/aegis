package com.aegis.utils;

import com.aegis.common.repeatable.RepeatableHttpServletRequestWrapper;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 请求工具类
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
public final class RequestUtils {

    private RequestUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 获取HTTP请求
     */
    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取请求属性
     */
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取请求体字符串
     * @param request HTTP请求
     * @return 请求体字符串，如果获取失败返回空字符串
     */
    public static String getRequestBody(HttpServletRequest request) {
        if (request instanceof RepeatableHttpServletRequestWrapper) {
            return ((RepeatableHttpServletRequestWrapper) request).getBodyAsString();
        }

        try {
            return StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read request body", e);
        }
    }

    /**
     * 获取请求体字节数组
     * @param request HTTP请求
     * @return 请求体字节数组，如果获取失败返回空数组
     */
    public static byte[] getRequestBodyAsBytes(HttpServletRequest request) {
        if (request instanceof RepeatableHttpServletRequestWrapper) {
            return ((RepeatableHttpServletRequestWrapper) request).getBodyAsByteArray();
        }

        try {
            return StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read request body", e);
        }
    }
}
