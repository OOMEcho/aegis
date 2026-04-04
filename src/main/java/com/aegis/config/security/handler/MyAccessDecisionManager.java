package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.exception.PermissionDeniedException;
import com.aegis.common.result.ResultCodeEnum;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义决策管理器
 *
 * @author xuesong.lei
 * @since 2025-09-02
 */
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // 检查是否有NONE权限
        if (configAttributes.stream().anyMatch(attr -> CommonConstants.NONE.equals(attr.getAttribute()))) {
            return;
        }

        // 检查用户是否具备所需权限编码
        Set<String> userPermissions  = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // 检查权限匹配
        for (ConfigAttribute configAttribute : configAttributes) {
            if (userPermissions.contains(configAttribute.getAttribute())) {
                return;
            }
        }

        Set<String> requiredPermissions = configAttributes.stream()
                .map(ConfigAttribute::getAttribute)
                .collect(Collectors.toSet());

        throw new PermissionDeniedException(
                ResultCodeEnum.LACK_OF_AUTHORITY.getMessage(),
                requiredPermissions,
                userPermissions
        );
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getAttribute() != null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
