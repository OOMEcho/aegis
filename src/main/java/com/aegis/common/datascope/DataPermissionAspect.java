package com.aegis.common.datascope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 数据权限AOP切面
 *
 * @author xuesong.lei
 * @since 2025-09-11
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DataPermissionAspect {

    private final DataPermissionHandler dataPermissionHandler;

    /**
     * 拦截带有 @DataPermission 注解的方法
     */
    @Around("@annotation(dataPermission)")
    public Object applyDataPermission(ProceedingJoinPoint joinPoint, DataPermission dataPermission) throws Throwable {
        // 检查注解是否启用
        if (!dataPermission.enable()) {
            return joinPoint.proceed();
        }

        try {
            // 获取当前用户的数据权限上下文
            DataPermissionContext context = dataPermissionHandler.getCurrentUserDataPermission();
            if (context == null) {
                return joinPoint.proceed();
            }

            // 将数据权限信息设置到线程本地变量
            DataPermissionContextHolder.set(context, dataPermission);

            // 执行目标方法
            return joinPoint.proceed();

        } catch (Exception e) {
            log.error("应用数据权限时发生错误，方法: {}", joinPoint.getSignature().toShortString(), e);
            // 出错时也要执行原方法，避免业务中断
            return joinPoint.proceed();
        } finally {
            // 清除线程本地变量，避免内存泄漏
            DataPermissionContextHolder.clear();
            if (log.isTraceEnabled()) {
                log.trace("清除数据权限上下文，方法: {}", joinPoint.getSignature().toShortString());
            }
        }
    }

    /**
     * 拦截类级别的 @DataPermission 注解
     * 优先级低于方法级别的注解
     */
    @Around("@within(dataPermission) && !@annotation(com.aegis.common.datascope.DataPermission)")
    public Object applyClassLevelDataPermission(ProceedingJoinPoint joinPoint, DataPermission dataPermission) throws Throwable {
        return applyDataPermission(joinPoint, dataPermission);
    }
}
