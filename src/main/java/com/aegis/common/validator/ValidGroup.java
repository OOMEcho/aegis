package com.aegis.common.validator;

import javax.validation.groups.Default;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 13:08
 * @Description: 分组校验
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
