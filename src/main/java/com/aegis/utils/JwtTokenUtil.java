package com.aegis.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/31 12:12
 * @Description: JWT工具类
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationThatShouldBeLongEnough}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration:900}") // 15分钟
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800}") // 7天
    private long refreshTokenExpiration;

    @Value("${jwt.issuer:aegis}")
    private String issuer;

    private static final String CLAIM_KEY_AUTHORITIES = "authorities";

    public static final String TOKEN_TYPE = "token_type";

    public static final String TOKEN_TYPE_ACCESS = "access";

    public static final String TOKEN_TYPE_REFRESH = "refresh";

    /**
     * 生成Token响应对象
     */
    @Data
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        private long expiresIn;

        public TokenResponse(String accessToken, String refreshToken, long expiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
        }
    }

    /**
     * 根据Spring Security认证信息生成双Token
     */
    public TokenResponse generateTokenResponse(Authentication authentication) {
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = generateAccessToken(username, authorities);
        String refreshToken = generateRefreshToken(username);

        return new TokenResponse(accessToken, refreshToken, accessTokenExpiration);
    }

    /**
     * 生成Access Token
     */
    public String generateAccessToken(String username, String authorities) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(accessTokenExpiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_AUTHORITIES, authorities);
        claims.put(TOKEN_TYPE, TOKEN_TYPE_ACCESS);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 生成Refresh Token
     */
    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(refreshTokenExpiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE, TOKEN_TYPE_REFRESH);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 从Token中提取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从Token中提取权限信息
     */
    public String getAuthoritiesFromToken(String token) {
        return getClaimsFromToken(token).get(CLAIM_KEY_AUTHORITIES, String.class);
    }

    /**
     * 验证Token是否为Access Token
     */
    public boolean isAccessToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return TOKEN_TYPE_ACCESS.equals(claims.get(TOKEN_TYPE));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证Token是否为Refresh Token
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return TOKEN_TYPE_REFRESH.equals(claims.get(TOKEN_TYPE));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 检查Token是否即将过期（剩余时间少于5分钟）
     */
    public boolean isTokenNearExpiry(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            long timeLeft = expiration.getTime() - now.getTime();
            return timeLeft < 300000; // 5分钟 = 300000毫秒
        } catch (Exception e) {
            return true; // 解析失败视为即将过期
        }
    }

    /**
     * 使用Refresh Token刷新Access Token
     */
    public TokenResponse refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken) || !isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = getUsernameFromToken(refreshToken);
        // 这里需要从数据库或缓存中获取用户权限
        // 为了演示，暂时使用空权限
        String authorities = getUserAuthoritiesFromDatabase(username);

        String newAccessToken = generateAccessToken(username, authorities);
        String newRefreshToken = generateRefreshToken(username);

        return new TokenResponse(newAccessToken, newRefreshToken, accessTokenExpiration);
    }

    /**
     * 从Token中解析Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 从数据库获取用户权限
     */
    private String getUserAuthoritiesFromDatabase(String username) {
        // TODO: 实现从数据库或缓存中获取用户权限的逻辑
        // 示例返回
        return "ROLE_USER";
    }

    /**
     * 撤销Token
     */
    public void revokeToken(String token) {
        // TODO: 将token加入黑名单
        // 可以存储到Redis中，设置过期时间为token的剩余有效时间
    }
}
