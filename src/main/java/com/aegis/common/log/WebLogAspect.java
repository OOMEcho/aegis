package com.aegis.common.log;


import com.aegis.utils.IpUtils;
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
import java.util.Date;

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

    private static final ThreadLocal<Object[]> initialArgsHolder = new ThreadLocal<>();

    @Before("@annotation(operationLog)")
    public void doBefore(JoinPoint joinPoint, OperationLog operationLog) {
        initialArgsHolder.set(joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "@annotation(operationLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, OperationLog operationLog, Object result) {
        handleLog(joinPoint, operationLog, result, null);
        initialArgsHolder.remove();
    }

    @AfterThrowing(pointcut = "@annotation(operationLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, OperationLog operationLog, Throwable e) {
        handleLog(joinPoint, operationLog, null, e);
        String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.error("执行方法【{}】出错，异常为:【{}】", method, e.getMessage());
        initialArgsHolder.remove();
    }

    private void handleLog(JoinPoint joinPoint, OperationLog operationLog, Object result, Throwable e) {
        HttpServletRequest request = getRequest();
        String ip = IpUtils.getIpAddr(request);
        SysOperateLog sysOperateLog = new SysOperateLog();
        sysOperateLog.setModuleTitle(operationLog.moduleTitle());
        sysOperateLog.setBusinessType(operationLog.businessType().ordinal());
        sysOperateLog.setRequestUrl(request.getRequestURI());
        sysOperateLog.setRequestIp(ip);
        sysOperateLog.setRequestLocal(AddressUtils.getRealAddressByIp(ip));
        sysOperateLog.setRequestType(request.getMethod());
        sysOperateLog.setRequestMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
        sysOperateLog.setRequestArgs(JSONUtil.toJsonStr(initialArgsHolder.get()));
        sysOperateLog.setOperateUser(SecurityUtils.getUsername());
        sysOperateLog.setOperateTime(new Date());
        if (e != null) {
            sysOperateLog.setErrorMessage(e.getMessage());
            sysOperateLog.setOperateStatus("1");
        }
        if (BeanUtil.isNotEmpty(result)) {
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
