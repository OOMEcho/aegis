package com.aegis.config.security.handler;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.CommonConstants;
import com.aegis.config.security.LoginSecurityProperties;
import com.aegis.utils.ResponseUtils;
import com.aegis.utils.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注销登录处理逻辑
 *
 * @author xuesong.lei
 * @since 2025-09-02
 */
@Component
@RequiredArgsConstructor
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private final TokenService tokenService;

    private final LoginSecurityProperties loginSecurityProperties;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotBlank(header) && header.startsWith(CommonConstants.TOKEN_PREFIX)) {
            String accessToken = header.substring(CommonConstants.TOKEN_PREFIX.length()).trim();
            tokenService.invalidateByAccessToken(accessToken);
        }

        // 删除 Cookie 中的 refreshToken
        Cookie cookie = new Cookie(CommonConstants.REFRESH_TOKEN_COOKIE, null);
        cookie.setHttpOnly(true);
        cookie.setPath(loginSecurityProperties.getCookiePath());
        cookie.setSecure(loginSecurityProperties.isCookieSecure());
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // 清除认证上下文
        SecurityContextHolder.clearContext();

        ResponseUtils.writeSuccess(response, "退出成功");
    }
}
