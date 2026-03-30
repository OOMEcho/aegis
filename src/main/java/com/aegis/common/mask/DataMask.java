package com.aegis.common.mask;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据脱敏注解
 *
 * @author xuesong.lei
 * @since 2025/9/20 13:45
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface DataMask {

    /**
     * 脱敏类型
     */
    MaskTypeEnum type() default MaskTypeEnum.CUSTOM;

    /**
     * 自定义脱敏规则：保留前几位
     */
    int prefixKeep() default 0;

    /**
     * 自定义脱敏规则：保留后几位
     */
    int suffixKeep() default 0;

    /**
     * 脱敏替换字符
     */
    String maskChar() default "*";

    /**
     * 是否启用脱敏（可用于动态控制）
     */
    boolean enabled() default true;
}
