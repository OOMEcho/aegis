package com.aegis.common.limiter;

import com.aegis.common.exception.BusinessException;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.utils.IpUtils;
import com.aegis.utils.RedisUtils;
import com.aegis.utils.RequestUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 限流处理切面
 *
 * @author xuesong.lei
 * @since 2025-09-19
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final RedisUtils redisUtils;

    private final RedisScript<Long> limitScript;

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String message = rateLimiter.message();

        String combineKey = getCombineKey(rateLimiter, point);
        List<String> keys = Collections.singletonList(combineKey);
        try {
            Long number = redisUtils.execute(limitScript, keys, count, time);
            if (ObjectUtils.isNull(number) || number.intValue() > count) {
                throw new BusinessException(message);
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), combineKey);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("限流异常", e);
            throw BusinessException.of(ResultCodeEnum.ERROR);
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuilder stringBuffer = new StringBuilder(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            HttpServletRequest request = RequestUtils.getRequest();
            if (request == null) {
                log.warn("无法获取HTTP请求，跳过添加IP地址");
            } else {
                stringBuffer.append(IpUtils.getIpAddr(request)).append("-");
            }
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
