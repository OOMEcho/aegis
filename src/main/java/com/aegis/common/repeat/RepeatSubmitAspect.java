package com.aegis.common.repeat;

import com.aegis.common.exception.BusinessException;
import com.aegis.utils.IpUtils;
import com.aegis.utils.JacksonUtils;
import com.aegis.utils.RedisUtils;
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
import java.security.MessageDigest;
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
    public Object around(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = getRequest();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
        String url = request.getRequestURI();
        String ip = IpUtils.getIpAddr(request);
        String redisKey = url.concat(ip).concat(getMethodSign(method, joinPoint.getArgs()));
        if (!redisUtils.hasKey(redisKey)) {
            redisUtils.set(redisKey, annotation.value(), annotation.expireSeconds(), TimeUnit.SECONDS);
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                redisUtils.delete(redisKey);
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new BusinessException("请勿重复提交");
        }
    }

    /**
     * 生成方法标记：采用数字签名算法SHA1对方法签名字符串加签
     */
    private String getMethodSign(Method method, Object... args) {
        StringBuilder sb = new StringBuilder(method.toString());
        for (Object arg : args) {
            sb.append(toString(arg));
        }
        return sha1Hex(sb.toString());
    }

    private String toString(Object arg) {
        if (Objects.isNull(arg)) {
            return "null";
        }
        if (arg instanceof Number) {
            return arg.toString();
        }
        return JacksonUtils.toJson(arg);
    }

    private ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    private HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    private String sha1Hex(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(data.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception e) {
            throw new BusinessException("SHA1计算失败");
        }
    }
}
