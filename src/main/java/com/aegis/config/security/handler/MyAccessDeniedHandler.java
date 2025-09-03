package com.aegis.config.security.handler;

import cn.hutool.json.JSONUtil;
import com.aegis.common.result.Result;
import com.aegis.common.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 22:35
 * @Description: 权限拒绝处理逻辑
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(Result.error(ResultCodeEnum.LACK_OF_AUTHORITY)));
    }
}
