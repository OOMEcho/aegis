package com.aegis.common.exception;

import lombok.Getter;
import org.springframework.security.access.AccessDeniedException;

import java.util.Set;

/**
 * 携带权限详情的访问拒绝异常
 *
 * @author xuesong.lei
 * @since 2026-01-17
 */
@Getter
public class PermissionDeniedException extends AccessDeniedException {

    private final Set<String> requiredPermissions;
    private final Set<String> userPermissions;

    public PermissionDeniedException(String msg, Set<String> requiredPermissions, Set<String> userPermissions) {
        super(msg);
        this.requiredPermissions = requiredPermissions;
        this.userPermissions = userPermissions;
    }
}
