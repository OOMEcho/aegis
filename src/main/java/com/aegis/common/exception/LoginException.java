package com.aegis.common.exception;

import com.aegis.common.result.ResultCodeEnum;
import org.springframework.security.core.AuthenticationException;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/30 22:41
 * @Description: 登录异常处理器
 */
public class LoginException extends AuthenticationException {

    private final Integer code;

    public LoginException(String message) {
        super(message);
        this.code = ResultCodeEnum.ERROR.getCode();
    }

    public LoginException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public LoginException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

}
