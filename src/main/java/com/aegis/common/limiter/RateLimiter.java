package com.aegis.common.limiter;

import com.aegis.common.constant.RedisConstants;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 限流注解
 *
 * @author xuesong.lei
 * @since 2025-09-19
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 限流key前缀
     */
    String key() default RedisConstants.RATE_LIMIT;

    /**
     * 防重复操作限时标记数值
     */
    String message() default "访问过于频繁,请稍候再试";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 20;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;
}
