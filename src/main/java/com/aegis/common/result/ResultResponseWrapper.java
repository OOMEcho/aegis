package com.aegis.common.result;

import com.aegis.utils.JacksonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 13:08
 * @Description: 统一返回结果包装类
 */
@RestControllerAdvice
public class ResultResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
            return JacksonUtils.toJson(Result.success(body));
        }
        // 防止全局异常处理后返回的结果（类型为Result）再次被包装
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }
}
