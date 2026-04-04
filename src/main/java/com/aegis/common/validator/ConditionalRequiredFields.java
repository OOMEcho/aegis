package com.aegis.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 条件必填字段校验注解
 *
 * @author xuesong.lei
 * @since 2025-09-13
 */
@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(ConditionalRequiredFields.List.class)
@Documented
@Constraint(validatedBy = ConditionalRequiredFieldsValidator.class)
public @interface ConditionalRequiredFields {

    String message() default "当 {field} 等于 '{value}' 时，必填字段缺失";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 条件字段名
     */
    String field();

    /**
     * 条件字段的值
     */
    String value();

    /**
     * 当条件满足时，必须非空的字段列表
     */
    String[] requiredFields();

    /**
     * 在同一元素上定义多个 {@link ConditionalRequiredFields} 注释。
     */
    @Target({TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ConditionalRequiredFields[] value();
    }
}
