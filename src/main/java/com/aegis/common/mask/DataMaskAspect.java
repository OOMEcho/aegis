package com.aegis.common.mask;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.result.Result;
import com.aegis.utils.DataMaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 数据脱敏切面
 *
 * @author xuesong.lei
 * @since 2025/9/20 13:50
 */
@Slf4j
@Aspect
@Component
@Order(1) // 确保在ResultResponseWrapper之前执行
public class DataMaskAspect {

    /**
     * 拦截标注了@DataMask的方法
     */
    @Around("@annotation(dataMask)")
    public Object maskMethodResult(ProceedingJoinPoint joinPoint, DataMask dataMask) throws Throwable {
        Object result = joinPoint.proceed();

        if (result == null || !dataMask.enabled()) {
            return result;
        }

        try {

            return maskData(result);
        } catch (Exception e) {
            log.error("数据脱敏处理失败，方法：{}", joinPoint.getSignature().toShortString(), e);
            return result; // 脱敏失败时返回原数据，保证业务不受影响
        }
    }

    /**
     * 递归处理数据脱敏
     */
    private Object maskData(Object data) throws IllegalAccessException {
        if (data == null) {
            return null;
        }

        Class<?> dataClass = data.getClass();

        // 处理基本类型和包装类
        if (isPrimitiveOrWrapper(dataClass)) {
            return data;
        }

        // 处理字符串
        switch (data) {
            case String s -> {
                return data;
            }


            // 特殊处理PageVO
            case PageVO<?> pageVO -> {
                return maskPageVO(pageVO);
            }


            // 处理Result包装类型
            case Result<?> result -> {
                if (result.getData() != null) {
                    Object maskedData = maskData(result.getData());
                    // 创建新的Result对象避免修改原对象
                    return Result.success(maskedData);
                }
                return result;
            }


            // 处理集合
            case Collection<?> collection -> {
                collection.forEach(item -> {
                    try {
                        maskData(item);
                    } catch (IllegalAccessException e) {
                        log.warn("集合元素脱敏失败", e);
                    }
                });
                return data;
            }
            default -> {
            }
        }

        // 处理数组
        if (dataClass.isArray()) {
            Object[] array = (Object[]) data;
            for (Object item : array) {
                maskData(item);
            }
            return data;
        }

        // 处理普通对象
        return maskPojo(data);
    }

    /**
     * 专门处理PageVO类型的脱敏
     */
    private PageVO<?> maskPageVO(PageVO<?> pageVO) throws IllegalAccessException {
        if (pageVO == null || pageVO.getRecords() == null) {
            return pageVO;
        }

        // 对分页数据中的records进行脱敏
        for (Object record : pageVO.getRecords()) {
            maskData(record);
        }

        return pageVO;
    }

    /**
     * 处理POJO对象的脱敏 - 增强深层嵌套处理
     */
    private Object maskPojo(Object pojo) throws IllegalAccessException {
        if (pojo == null) return null;

        Field[] fields = getAllFields(pojo.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(pojo);

            if (fieldValue == null) continue;

            DataMask annotation = field.getAnnotation(DataMask.class);

            // 处理直接标注@DataMask的字符串字段
            if (annotation != null && annotation.enabled() && fieldValue instanceof String) {
                String maskedValue = maskFieldValue((String) fieldValue, annotation);
                field.set(pojo, maskedValue);
            }
            // 处理嵌套对象 - 不管是否有@DataMask注解都要递归检查
            else if (!isPrimitiveOrWrapper(fieldValue.getClass())) {
                // 递归处理嵌套对象、集合等
                maskData(fieldValue);
            }
        }
        return pojo;
    }

    /**
     * 获取类的所有字段（包括父类）
     */
    private Field[] getAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();

        // 获取当前类及所有父类的字段
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                // 排除静态字段和final字段（除非是集合类型）
                if (!Modifier.isStatic(field.getModifiers())) {
                    allFields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return allFields.toArray(new Field[0]);
    }

    /**
     * 根据注解配置脱敏字段值
     */
    private String maskFieldValue(String value, DataMask annotation) {
        if (annotation.type().equals(MaskTypeEnum.CUSTOM)) {
            return DataMaskUtil.maskCustom(value,
                    annotation.prefixKeep(),
                    annotation.suffixKeep(),
                    annotation.maskChar());
        } else {
            return DataMaskUtil.mask(value, annotation.type(), annotation.maskChar());
        }
    }

    /**
     * 判断是否为基本类型或包装类
     */
    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(BigDecimal.class) ||
                clazz.equals(BigInteger.class) ||
                clazz.equals(LocalDate.class) ||
                clazz.equals(LocalDateTime.class) ||
                clazz.equals(LocalTime.class) ||
                clazz.equals(Date.class) ||
                clazz.isEnum();
    }
}
