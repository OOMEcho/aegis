package com.aegis.common.validator;

import jakarta.validation.groups.Default;

/**
 * 分组校验
 *
 * @author xuesong.lei
 * @since 2025/08/21 13:08
 */
public interface ValidGroup extends Default {

    interface Create extends ValidGroup {
    }

    interface Query extends ValidGroup {
    }

    interface Update extends ValidGroup {
    }

    interface Delete extends ValidGroup {
    }
}
