package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.event.DataChangePublisher;
import com.aegis.common.exception.PermissionDeniedException;
import com.aegis.common.ip2region.Ip2regionService;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.common.trace.TraceIdUtils;
import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.utils.IpUtils;
import com.aegis.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 权限拒绝处理逻辑
 *
 * @author xuesong.lei
 * @since 2025-09-02
 */
@Component
@RequiredArgsConstructor
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private final DataChangePublisher dataChangePublisher;

    private final Ip2regionService ip2regionService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        // 记录授权失败日志
        logAccessDenied(request, accessDeniedException);

        ResponseUtils.writeError(response, ResultCodeEnum.LACK_OF_AUTHORITY);
    }

    /**
     * 记录授权失败日志
     */
    private void logAccessDenied(HttpServletRequest request, AccessDeniedException accessDeniedException) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : CommonConstants.ANONYMOUS;
            String ip = IpUtils.getIpAddr(request);

            SysOperateLog log = new SysOperateLog();
            log.setTraceId(TraceIdUtils.getTraceId());
            log.setModuleTitle("安全-授权检查");
            log.setBusinessType(0);
            log.setRequestUrl(request.getRequestURI());
            log.setRequestIp(ip);
            log.setRequestLocal(ip2regionService.getRegion(ip));
            log.setRequestType(request.getMethod());
            log.setRequestMethod("MyAccessDeniedHandler.handle()");
            log.setOperateUser(username);
            log.setOperateTime(LocalDateTime.now());
            log.setOperateStatus(CommonConstants.DISABLE_STATUS);

            // 构建详细的错误信息
            String errorMsg = buildErrorMessage(request, accessDeniedException);
            log.setErrorMessage(errorMsg);

            dataChangePublisher.publishLog(log);
        } catch (Exception e) {
            // 日志记录失败不应影响主流程，静默处理
        }
    }

    /**
     * 构建详细的错误信息
     */
    private String buildErrorMessage(HttpServletRequest request, AccessDeniedException exception) {
        if (exception instanceof PermissionDeniedException pde) {
            return String.format("权限不足 - 请求: %s %s, 需要权限: %s, 用户权限: %s",
                    request.getMethod(),
                    request.getRequestURI(),
                    pde.getRequiredPermissions(),
                    pde.getUserPermissions());
        }

        return exception.getMessage();
    }
}
