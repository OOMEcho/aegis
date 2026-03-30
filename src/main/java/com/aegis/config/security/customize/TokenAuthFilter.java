package com.aegis.config.security.customize;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.CommonConstants;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.utils.ResponseUtils;
import com.aegis.utils.SecurityUtils;
import com.aegis.utils.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token认证过滤器
 *
 * @author xuesong.lei
 * @since 2025/9/2 23:10
 */
@Component
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/profile/refreshToken")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isEmpty(header) || !header.startsWith(CommonConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.substring(CommonConstants.TOKEN_PREFIX.length()).trim();
        try {
            // 单次 Redis 查询获取 session
            Authentication authentication = tokenService.getAuthentication(token);
            if (authentication == null) {
                ResponseUtils.writeError(response, ResultCodeEnum.LOGIN_EXPIRE);
                return;
            }

            // 验证是否为用户当前有效的 token（单设备登录控制）
            String username = authentication.getName();
            if (!tokenService.validateAccessToken(token, username)) {
                ResponseUtils.writeError(response, ResultCodeEnum.NOT_LOGGED_IN);
                return;
            }

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ResponseUtils.writeError(response, ResultCodeEnum.NOT_LOGGED_IN);
        } finally {
            SecurityContextHolder.clearContext();
            SecurityUtils.clearCurrentUser();
        }
    }
}
