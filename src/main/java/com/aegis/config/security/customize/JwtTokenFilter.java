package com.aegis.config.security.customize;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.RedisConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.utils.JwtTokenUtil;
import com.aegis.utils.RedisUtils;
import com.aegis.utils.ResponseUtils;
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
 * @Author: xuesong.lei
 * @Date: 2025/9/2 23:10
 * @Description: JWT过滤器
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isEmpty(header) || !header.startsWith(CommonConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.substring(CommonConstants.TOKEN_PREFIX.length()).trim();
        try {
            // 校验是否为access_token
            if (!jwtTokenUtil.isAccessToken(token)){
                throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
            }

            // 校验token有效性
            if (!jwtTokenUtil.validateToken(token)) {
                throw new BusinessException(ResultCodeEnum.LOGIN_EXPIRE);
            }

            // 黑名单校验
            if (redisUtils.hasKey(RedisConstants.BLACKLIST_TOKEN + token)) {
                throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
            }

            Authentication authentication = jwtTokenUtil.getAuthenticationToken(token);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            // access_token 过期，返回特定业务码，前端据此触发刷新
            ResponseUtils.writeError(response, ResultCodeEnum.LOGIN_EXPIRE);
        } catch (Exception e) {
            // 其他异常统一当作未登录处理
            ResponseUtils.writeError(response, ResultCodeEnum.NOT_LOGGED_IN);
        } finally {
            // 确保在请求结束后清理SecurityContext
            SecurityContextHolder.clearContext();
        }
    }
}
