package com.aegis.config.security.handler;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.RedisConstants;
import com.aegis.utils.JwtTokenUtil;
import com.aegis.utils.RedisUtils;
import com.aegis.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 22:47
 * @Description: 注销登录处理逻辑
 */
@Component
@RequiredArgsConstructor
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RedisUtils redisUtils;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotBlank(header) && header.startsWith(CommonConstants.TOKEN_PREFIX)) {
            String accessToken = header.substring(CommonConstants.TOKEN_PREFIX.length()).trim();

            // 将 access_token 加入黑名单，防止后续使用
            long expireSeconds = jwtTokenUtil.getAccessTokenExpireSeconds(accessToken);
            if (expireSeconds > 0) {
                redisUtils.set(RedisConstants.BLACKLIST_TOKEN + accessToken, "logout", expireSeconds, TimeUnit.SECONDS);
            }

            if (authentication != null) {
                // 删除 Redis 中的 refresh_token
                redisUtils.delete(RedisConstants.REFRESH_TOKEN + authentication.getName());
            }
        }

        // 删除 Cookie 中的 refreshToken
        Cookie cookie = new Cookie(CommonConstants.REFRESH_TOKEN_COOKIE, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/user/refreshToken");
        cookie.setSecure(false); // 如果你本地是 http，可以临时改为 false
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // 清除认证上下文
        SecurityContextHolder.clearContext();

        ResponseUtils.writeSuccess(response, "退出成功");
    }
}
