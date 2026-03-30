package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.exception.PermissionDeniedException;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.config.security.customize.SecurityMetadataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 自定义授权管理器
 *
 * @author xuesong.lei
 * @since 2025/9/24 9:59
 */
@Component
@RequiredArgsConstructor
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final SecurityMetadataService securityMetadataService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        // OPTIONS请求全部放行
        if (HttpMethod.OPTIONS.matches(method)) {
            return new AuthorizationDecision(true);
        }

        // 获取当前请求需要的角色
        Set<String> requiredPermissions = securityMetadataService.getRequiredPermissions(requestURI, method);

        // 如果是白名单，直接放行
        if (requiredPermissions == null) {
            return new AuthorizationDecision(true);
        }

        // 获取认证信息
        Authentication auth = authentication.get();

        // 需要登录但不需要特定角色
        if (requiredPermissions.contains(CommonConstants.NONE)) {
            return new AuthorizationDecision(true);
        }

        // 检查用户是否具备所需权限编码
        Set<String> userPermissions = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        boolean hasPermission = requiredPermissions.stream().anyMatch(userPermissions::contains);

        // 如果没有权限，抛出自定义异常
        if (!hasPermission) {
            throw new PermissionDeniedException(
                    ResultCodeEnum.LACK_OF_AUTHORITY.getMessage(),
                    requiredPermissions,
                    userPermissions
            );
        }

        return new AuthorizationDecision(true);
    }
}
