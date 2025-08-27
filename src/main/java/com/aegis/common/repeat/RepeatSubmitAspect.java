package com.aegis.common.repeat;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.utils.IpUtils;
import com.aegis.utils.RedisUtils;
import com.aegis.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 9:58
 * @Description: 防重复提交切面
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class RepeatSubmitAspect {

    private final RedisUtils redisUtils;

    @Pointcut("@annotation(com.aegis.common.repeat.RepeatSubmit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            HttpServletRequest request = getRequest();
            if (request == null) {
                log.warn("无法获取HTTP请求，跳过防重复检查");
                return joinPoint.proceed();
            }

            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);

            String redisKey = buildRedisKey(request, method, annotation, joinPoint.getArgs());

            // 检查是否重复提交
            if (isRepeatSubmit(redisKey)) {
                throw new BusinessException(annotation.message());
            }

            // 设置防重复标记
            setRepeatFlag(redisKey, annotation);

            try {
                // 执行原方法
                return joinPoint.proceed();
            } catch (BusinessException e) {
                // 业务异常不清理缓存，避免恶意重试
                throw e;
            } catch (Exception e) {
                // 系统异常清理缓存，允许重试
                clearRepeatFlag(redisKey);
                throw e;
            }

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("防重复提交检查异常", e);
            // Redis异常时不影响业务执行
            return joinPoint.proceed();
        }
    }

    /**
     * 构建Redis键
     */
    private String buildRedisKey(HttpServletRequest request, Method method, RepeatSubmit annotation, Object[] args) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(annotation.keyPrefix()).append(":");

        // URL
        keyBuilder.append(request.getRequestURI()).append(":");

        // IP地址
        String ip = IpUtils.getIpAddr(request);
        keyBuilder.append(ip).append(":");

        // 用户信息（如果需要）
        if (annotation.includeUser()) {
            try {
                String username = SecurityUtils.getUsername();
                if (username != null) {
                    keyBuilder.append(username).append(":");
                }
            } catch (Exception e) {
                log.debug("获取用户ID失败，使用会话ID", e);
                String sessionId = request.getSession(false) != null ?
                        request.getSession().getId() : "anonymous";
                keyBuilder.append(sessionId).append(":");
            }
        }

        // 方法签名
        keyBuilder.append(getMethodSign(method, args));

        return keyBuilder.toString();
    }

    /**
     * 检查是否重复提交
     */
    private boolean isRepeatSubmit(String redisKey) {
        try {
            return redisUtils.hasKey(redisKey);
        } catch (Exception e) {
            log.error("检查重复提交状态失败: {}", redisKey, e);
            // Redis异常时放行，避免影响业务
            return false;
        }
    }

    /**
     * 设置防重复标记
     */
    private void setRepeatFlag(String redisKey, RepeatSubmit annotation) {
        try {
            redisUtils.set(redisKey, System.currentTimeMillis(),
                    annotation.expireSeconds(), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("设置防重复标记失败: {}", redisKey, e);
            // 设置失败时抛出异常，避免防重复失效
            throw BusinessException.of(ResultCodeEnum.ERROR);
        }
    }

    /**
     * 清理防重复标记
     */
    private void clearRepeatFlag(String redisKey) {
        try {
            redisUtils.delete(redisKey);
        } catch (Exception e) {
            log.error("清理防重复标记失败: {}", redisKey, e);
            // 清理失败不影响主流程
        }
    }

    /**
     * 生成方法签名：采用SHA-256算法
     */
    private String getMethodSign(Method method, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getName())
                .append("#")
                .append(method.getName());

        // 添加参数
        for (Object arg : args) {
            sb.append("#").append(toString(arg));
        }

        return DigestUtil.sha256Hex(sb.toString());
    }

    /**
     * 对象转字符串
     */
    private String toString(Object arg) {
        if (Objects.isNull(arg)) {
            return "null";
        }
        if (arg instanceof Number) {
            return arg.toString();
        }
        try {
            return JSONUtil.toJsonStr(arg);
        } catch (Exception e) {
            return arg.toString();
        }
    }

    /**
     * 获取请求属性
     */
    private ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取HTTP请求
     */
    private HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            log.debug("获取HTTP请求失败", e);
            return null;
        }
    }
}
