package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 23:05
 * @Description: 自定义决策管理器
 */
@Slf4j
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // 如果没有配置权限要求，直接放行（适用于首页等公开接口）
        if (configAttributes == null || configAttributes.isEmpty()) {
            log.debug("No authorities required, access granted");
            return;
        }

        // 检查用户是否已认证
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException("User not authenticated");
        }

        // 检查是否有NONE权限（表示只需要认证即可访问）
        boolean hasNoneAuthority = configAttributes.stream().anyMatch(attr -> CommonConstants.NONE.equals(attr.getAttribute()));

        if (hasNoneAuthority) {
            log.debug("Access granted for authenticated user to unrestricted resource");
            return;
        }

        // 获取用户权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            log.warn("User has no authorities: {}", authentication.getName());
            throw new AccessDeniedException(ResultCodeEnum.LACK_OF_AUTHORITY.getMessage());
        }

        // 检查用户权限是否匹配所需权限
        for (ConfigAttribute configAttribute : configAttributes) {
            String requiredAuthority = configAttribute.getAttribute();

            for (GrantedAuthority authority : authorities) {
                if (requiredAuthority.equals(authority.getAuthority())) {
                    log.debug("Access granted for user: {} with authority: {}",
                            authentication.getName(), authority.getAuthority());
                    return;
                }
            }
        }

        // 权限不匹配，记录日志并拒绝访问
        log.warn("Access denied for user: {} to resource requiring authorities: {}",
                authentication.getName(),
                configAttributes.stream()
                        .map(ConfigAttribute::getAttribute)
                        .collect(Collectors.joining(", ")));

        throw new AccessDeniedException(ResultCodeEnum.LACK_OF_AUTHORITY.getMessage());
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
