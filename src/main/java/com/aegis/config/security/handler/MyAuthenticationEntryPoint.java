package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.event.DataChangePublisher;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.common.trace.TraceIdUtils;
import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.utils.IpUtils;
import com.aegis.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 22:35
 * @Description: 匿名用户访问无权限资源时（即未登录，或者登录状态过期失效）的处理逻辑
 */
@Component
@RequiredArgsConstructor
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final DataChangePublisher dataChangePublisher;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        // 记录认证失败日志
        logAuthenticationFailure(request, authException);

        ResponseUtils.writeError(response, ResultCodeEnum.NOT_LOGGED_IN);
    }

    /**
     * 记录认证失败日志
     */
    private void logAuthenticationFailure(HttpServletRequest request, AuthenticationException authException) {
        try {
            String ip = IpUtils.getIpAddr(request);

            SysOperateLog log = new SysOperateLog();
            log.setTraceId(TraceIdUtils.getTraceId());
            log.setModuleTitle("安全-认证检查");
            log.setBusinessType(0);
            log.setRequestUrl(request.getRequestURI());
            log.setRequestIp(ip);
            log.setRequestType(request.getMethod());
            log.setRequestMethod("MyAuthenticationEntryPoint.commence()");
            log.setOperateUser("anonymous");
            log.setOperateTime(LocalDateTime.now());
            log.setOperateStatus(CommonConstants.DISABLE_STATUS);
            log.setErrorMessage(authException.getMessage());

            dataChangePublisher.publishLog(log);
        } catch (Exception e) {
            // 日志记录失败不应影响主流程，静默处理
        }
    }
}
