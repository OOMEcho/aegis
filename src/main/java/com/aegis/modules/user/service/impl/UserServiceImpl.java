package com.aegis.modules.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.CommonConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.modules.user.service.UserService;
import com.aegis.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/4 22:50
 * @Description: 用户业务实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> CommonConstants.REFRESH_TOKEN_COOKIE.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (StrUtil.isBlank(refreshToken)) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }

        // 验证 refresh_token 签名
        if (!jwtTokenUtil.validateToken(refreshToken) || !jwtTokenUtil.isRefreshToken(refreshToken)) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }

        JwtTokenUtil.TokenResponse tokenResponse = jwtTokenUtil.refreshAccessToken(refreshToken);

        Cookie cookie = new Cookie(CommonConstants.REFRESH_TOKEN_COOKIE, tokenResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/user/refreshToken");
        cookie.setSecure(false);
        cookie.setMaxAge(Math.toIntExact(jwtTokenUtil.getRefreshTokenExpiration()));
        response.addCookie(cookie);

        return tokenResponse.getAccessToken();
    }
}
