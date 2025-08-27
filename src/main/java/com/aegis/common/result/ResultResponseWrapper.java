package com.aegis.common.result;

import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.NotNull;
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
@RestControllerAdvice(basePackages = "com.aegis")
public class ResultResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 排除已经是Result类型的返回值
        if (returnType.getParameterType().equals(Result.class)) {
            return false;
        }
        // 排除Swagger等文档接口
        String packageName = returnType.getContainingClass().getPackage().getName();
        return !packageName.contains("springfox") && !packageName.contains("swagger");
    }

    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType, @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        // 已经是Result类型，直接返回
        if (body instanceof Result) {
            return body;
        }

        // String类型特殊处理，避免序列化问题
        if (body instanceof String) {
            return JSONUtil.toJsonStr(Result.success(body));
        }

        // 其他类型统一包装
        return Result.success(body);
    }
}
