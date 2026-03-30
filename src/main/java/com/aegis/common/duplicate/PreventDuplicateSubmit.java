package com.aegis.common.duplicate;

import com.aegis.common.constant.RedisConstants;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 防止重复提交注解
 *
 * @author xuesong.lei
 * @since 2025/08/21 13:39
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface PreventDuplicateSubmit {

    /**
     * 重复提交key前缀
     */
    String keyPrefix() default RedisConstants.REPEAT_SUBMIT;

    /**
     * 防重复操作限时标记数值
     */
    String message() default "请勿重复提交";

    /**
     * 防重复操作过期时间(秒)
     */
    long expireSeconds() default 30;

    /**
     * 是否包含用户信息到key中
     */
    boolean includeUser() default true;
}
