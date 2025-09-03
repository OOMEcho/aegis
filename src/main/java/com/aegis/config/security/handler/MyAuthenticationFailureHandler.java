package com.aegis.config.security.handler;

import cn.hutool.json.JSONUtil;
import com.aegis.common.result.Result;
import com.aegis.common.result.ResultCodeEnum;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 22:39
 * @Description: 登录失败处理逻辑
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (e instanceof AccountExpiredException || e instanceof BadCredentialsException || e instanceof CredentialsExpiredException || e instanceof LockedException) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSONUtil.toJsonStr(Result.error(ResultCodeEnum.ACCOUNT_ERROR)));
        } else if (e instanceof DisabledException) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSONUtil.toJsonStr(Result.error(ResultCodeEnum.USER_IS_DISABLE)));
        } else {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSONUtil.toJsonStr(Result.error(e.getMessage())));
        }
    }
}
