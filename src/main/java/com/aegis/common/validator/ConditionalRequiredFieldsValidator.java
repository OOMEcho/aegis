package com.aegis.common.validator;

import com.aegis.common.exception.BusinessException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * 条件必填字段校验逻辑
 *
 * @author xuesong.lei
 * @since 2025/09/13 15:43
 */
public class ConditionalRequiredFieldsValidator implements ConstraintValidator<ConditionalRequiredFields, Object> {

    private String conditionField;
    private String conditionValue;
    private String[] requiredFields;

    @Override
    public void initialize(ConditionalRequiredFields annotation) {
        this.conditionField = annotation.field();
        this.conditionValue = annotation.value();
        this.requiredFields = annotation.requiredFields();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }

        try {
            // 获取条件字段的值
            Object conditionFieldValue = getFieldValue(obj, conditionField);

            // 检查条件是否满足
            if (conditionFieldValue != null && conditionValue.equals(conditionFieldValue.toString())) {
                boolean hasViolation = false;

                // 检查所有必填字段
                for (String fieldName : requiredFields) {
                    Object fieldValue = getFieldValue(obj, fieldName);

                    if (isFieldEmpty(fieldValue)) {
                        // 添加具体字段的错误信息
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(
                                        String.format("当 %s 等于 '%s' 时，字段 '%s' 是必填的",
                                                conditionField, conditionValue, fieldName))
                                .addPropertyNode(fieldName)
                                .addConstraintViolation();
                        hasViolation = true;
                    }
                }

                return !hasViolation;
            }

            return true;
        } catch (Exception e) {
            throw new BusinessException("字段校验异常: " + e.getMessage());
        }
    }

    private Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = findField(obj.getClass(), fieldName);
        if (field == null) {
            throw new NoSuchFieldException("字段未找到：" + fieldName);
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // 尝试在父类中查找
            if (clazz.getSuperclass() != null) {
                return findField(clazz.getSuperclass(), fieldName);
            }
            return null;
        }
    }

    private boolean isFieldEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        }
        return false;
    }
}
