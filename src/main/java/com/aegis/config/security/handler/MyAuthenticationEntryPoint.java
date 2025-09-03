package com.aegis.config.security.handler;

import cn.hutool.json.JSONUtil;
import com.aegis.common.result.Result;
import com.aegis.common.result.ResultCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 22:35
 * @Description: 匿名用户访问无权限资源时（即未登录，或者登录状态过期失效）的处理逻辑
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(Result.error(ResultCodeEnum.SIGN_EXPIRES)));
    }
}
