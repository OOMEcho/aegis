package com.aegis.common.repeat;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 13:39
 * @Description: 放重复提交注解
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 防重复操作限时标记数值
     */
    String message() default "请勿重复提交";

    /**
     * 防重复操作过期时间(秒)
     */
    long expireSeconds() default 60;

    /**
     * 是否包含用户信息到key中
     */
    boolean includeUser() default true;

    /**
     * 自定义key前缀
     */
    String keyPrefix() default "repeat_submit";
}
