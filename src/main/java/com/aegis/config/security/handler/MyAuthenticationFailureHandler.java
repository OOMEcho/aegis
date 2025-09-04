package com.aegis.config.security.handler;

import com.aegis.common.result.ResultCodeEnum;
import com.aegis.utils.ResponseUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 22:39
 * @Description: 登录失败处理逻辑
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        if (e instanceof AccountExpiredException
                || e instanceof BadCredentialsException
                || e instanceof CredentialsExpiredException
                || e instanceof LockedException) {
            ResponseUtils.writeError(response, ResultCodeEnum.ACCOUNT_ERROR);
        } else if (e instanceof DisabledException) {
            ResponseUtils.writeError(response, ResultCodeEnum.USER_IS_DISABLE);
        } else {
            ResponseUtils.writeError(response, e.getMessage());
        }
    }
}
