package com.aegis.common.log;

import cn.hutool.json.JSONUtil;
import com.aegis.common.constant.Constants;
import com.aegis.common.constant.FileConstants;
import com.aegis.common.ip2region.Ip2regionService;
import com.aegis.common.log.listener.LogEventPublish;
import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.utils.IpUtils;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 21:32
 * @Description: 日志记录切面
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class WebLogAspect {

    private final LogEventPublish logEventPublish;

    private final Ip2regionService ip2regionService;

    private static final ThreadLocal<Object[]> initialArgsHolder = new ThreadLocal<>();

    private static final ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();

    @Before("@annotation(operationLog)")
    public void doBefore(JoinPoint joinPoint, OperationLog operationLog) {
        initialArgsHolder.set(joinPoint.getArgs());
        startTimeHolder.set(System.currentTimeMillis());
    }

    @AfterReturning(pointcut = "@annotation(operationLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, OperationLog operationLog, Object result) {
        long executionTime = calculateExecutionTime();
        handleLog(joinPoint, operationLog, result, null, executionTime);
        cleanupThreadLocals();
    }

    @AfterThrowing(pointcut = "@annotation(operationLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, OperationLog operationLog, Throwable e) {
        long executionTime = calculateExecutionTime();
        handleLog(joinPoint, operationLog, null, e, executionTime);
        String method = joinPoint.getSignature().getDeclaringTypeName() + FileConstants.POINT + joinPoint.getSignature().getName();
        log.error("执行方法【{}】出错，异常为:【{}】", method, e.getMessage());
        cleanupThreadLocals();
    }

    private long calculateExecutionTime() {
        Long startTime = startTimeHolder.get();
        return startTime != null ? System.currentTimeMillis() - startTime : 0L;
    }

    private void cleanupThreadLocals() {
        initialArgsHolder.remove();
        startTimeHolder.remove();
    }

    private void handleLog(JoinPoint joinPoint, OperationLog operationLog, Object result, Throwable e, long executionTime) {
        HttpServletRequest request = getRequest();
        String ip = IpUtils.getIpAddr(request);
        SysOperateLog sysOperateLog = new SysOperateLog();
        sysOperateLog.setModuleTitle(operationLog.moduleTitle());
        sysOperateLog.setBusinessType(operationLog.businessType().ordinal());
        sysOperateLog.setRequestUrl(request.getRequestURI());
        sysOperateLog.setRequestIp(ip);
        sysOperateLog.setRequestLocal(ip2regionService.getRegion(ip));
        sysOperateLog.setRequestType(request.getMethod());
        sysOperateLog.setRequestMethod(joinPoint.getTarget().getClass().getName() + FileConstants.POINT + joinPoint.getSignature().getName() + "()");
        sysOperateLog.setRequestArgs(JSONUtil.toJsonStr(initialArgsHolder.get()));
        sysOperateLog.setOperateUser(SecurityUtils.getUsername());
        sysOperateLog.setOperateTime(LocalDateTime.now());
        sysOperateLog.setDepleteTime(executionTime);

        if (e != null) {
            sysOperateLog.setErrorMessage(e.getMessage());
            sysOperateLog.setOperateStatus(Constants.DISABLE_STATUS);
        }
        if (ObjectUtils.isNotEmpty(result)) {
            sysOperateLog.setResponseResult(JSONUtil.toJsonStr(result));
        }
        logEventPublish.publishEvent(sysOperateLog);
    }

    private ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    private HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

}
