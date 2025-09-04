package com.aegis.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
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

        public TokenResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
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
        String refreshToken = generateRefreshToken(username, authorities);

        return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * 使用Refresh Token刷新Access Token
     */
    public TokenResponse refreshAccessToken(String refreshToken) {

        String username = getUsernameFromToken(refreshToken);
        String authorities = getUserAuthorities(refreshToken);

        String newAccessToken = generateAccessToken(username, authorities);
        String newRefreshToken = generateRefreshToken(username, authorities);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    /**
     * 从Token中获取Spring Security认证信息
     */
    public Authentication getAuthenticationToken(String token) {
        String username = getUsernameFromToken(token);
        List<GrantedAuthority> authorities = getAuthoritiesFromToken(token);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
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
    public String generateRefreshToken(String username, String authorities) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(refreshTokenExpiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_AUTHORITIES, authorities);
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
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取Access Token过期时间（秒）
     */
    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    /**
     * 获取Refresh Token过期时间（秒）
     */
    public Long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    /**
     * 获取Access Token剩余有效时间（秒）
     */
    public Long getAccessTokenExpireSeconds(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            long diff = expiration.getTime() - System.currentTimeMillis();
            return Math.max(diff / 1000, 0);
        } catch (Exception e) {
            return 0L;
        }
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
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        String authorityStr = getClaimsFromToken(token).get(CLAIM_KEY_AUTHORITIES, String.class);
        return Arrays.stream(authorityStr.split(","))
                .filter(str -> !str.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 从Token获取用户权限
     */
    public String getUserAuthorities(String token) {
        return getClaimsFromToken(token).get(CLAIM_KEY_AUTHORITIES, String.class);
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
}
