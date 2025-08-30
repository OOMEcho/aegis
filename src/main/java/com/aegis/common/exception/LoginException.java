package com.aegis.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/30 22:41
 * @Description: 登录异常处理器
 */
public class LoginException extends AuthenticationException {

    public LoginException(String msg) {
        super(msg);
    }

}
