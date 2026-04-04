package com.aegis.common.log;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 操作日志注解
 *
 * @author xuesong.lei
 * @since 2025-08-21
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 模块标题
     */
    String moduleTitle() default "";

    /**
     * 业务类型
     */
    BusinessType businessType() default BusinessType.OTHER;
}
