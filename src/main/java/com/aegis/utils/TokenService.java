package com.aegis.utils;

import com.aegis.common.constant.RedisConstants;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Opaque Token 服务，替代 JWT 实现
 *
 * @author xuesong.lei
 * @since 2025-08-31
 */
@Component
@RequiredArgsConstructor
public class TokenService {

    private final RedisUtils redisUtils;

    @Value("${token.access-expiration:900}")
    private long accessExpiration;

    @Value("${token.refresh-expiration:604800}")
    private long refreshExpiration;

    @Data
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;

        public TokenResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    /**
     * 根据认证信息生成双 Token 并存入 Redis
     */
    public TokenResponse createSession(Authentication authentication) {
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return createSession(username, authorities);
    }

    /**
     * 根据用户名和权限字符串生成双 Token 并存入 Redis
     */
    public TokenResponse createSession(String username, String authorities) {
        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        // 存储 access token session：session:{accessToken} → username + authorities
        String sessionValue = username + "|" + authorities;
        redisUtils.set(RedisConstants.SESSION + accessToken, sessionValue, accessExpiration, TimeUnit.SECONDS);

        // 用户 session 反向索引：user_session:{username} → accessToken
        redisUtils.set(RedisConstants.USER_SESSION + username, accessToken, accessExpiration, TimeUnit.SECONDS);

        // 存储 refresh token：refresh:{refreshToken} → username
        redisUtils.set(RedisConstants.REFRESH + refreshToken, username, refreshExpiration, TimeUnit.SECONDS);

        // 用户 refresh 反向索引：user_refresh:{username} → refreshToken
        redisUtils.set(RedisConstants.USER_REFRESH + username, refreshToken, refreshExpiration, TimeUnit.SECONDS);

        return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * 通过 access token 获取认证信息（单次 Redis 查询）
     */
    public Authentication getAuthentication(String accessToken) {
        String sessionValue = redisUtils.get(RedisConstants.SESSION + accessToken);
        if (sessionValue == null) {
            return null;
        }
        int separatorIndex = sessionValue.indexOf('|');
        if (separatorIndex < 0) {
            return null;
        }
        String username = sessionValue.substring(0, separatorIndex);
        String authoritiesStr = sessionValue.substring(separatorIndex + 1);
        List<GrantedAuthority> authorities = Arrays.stream(authoritiesStr.split(","))
                .filter(str -> !str.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    /**
     * 验证 access token 是否属于该用户的当前 session（防止旧 token 复用）
     */
    public boolean validateAccessToken(String accessToken, String username) {
        String currentToken = redisUtils.get(RedisConstants.USER_SESSION + username);
        return accessToken.equals(currentToken);
    }

    /**
     * 验证 refresh token 是否有效
     */
    public String validateRefreshToken(String refreshToken) {
        return redisUtils.get(RedisConstants.REFRESH + refreshToken);
    }

    /**
     * 验证 refresh token 是否属于该用户的当前 refresh session
     */
    public boolean isCurrentRefreshToken(String refreshToken, String username) {
        String currentRefresh = redisUtils.get(RedisConstants.USER_REFRESH + username);
        return refreshToken.equals(currentRefresh);
    }

    /**
     * 使用户的所有 token 立即失效
     */
    public void invalidateSession(String username) {
        String accessToken = redisUtils.get(RedisConstants.USER_SESSION + username);
        if (accessToken != null) {
            redisUtils.delete(RedisConstants.SESSION + accessToken);
        }
        redisUtils.delete(RedisConstants.USER_SESSION + username);
        String refreshToken = redisUtils.get(RedisConstants.USER_REFRESH + username);
        if (refreshToken != null) {
            redisUtils.delete(RedisConstants.REFRESH + refreshToken);
        }
        redisUtils.delete(RedisConstants.USER_REFRESH + username);
    }

    /**
     * 通过 access token 使 session 失效（用于登出）
     */
    public String invalidateByAccessToken(String accessToken) {
        String sessionValue = redisUtils.get(RedisConstants.SESSION + accessToken);
        if (sessionValue == null) {
            return null;
        }
        String username = sessionValue.substring(0, sessionValue.indexOf('|'));
        invalidateSession(username);
        return username;
    }

    /**
     * 获取 refresh token 过期时间（秒）
     */
    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    /**
     * 检查用户是否在线
     */
    public boolean isOnline(String username) {
        return redisUtils.hasKey(RedisConstants.USER_SESSION + username);
    }
}
