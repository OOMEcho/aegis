package com.aegis.config.security;

import com.aegis.config.security.customize.JwtTokenFilter;
import com.aegis.config.security.customize.MultiLoginAuthenticationFilter;
import com.aegis.config.security.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/25 22:30
 * @Description: SpringSecurity配置类
 */
@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    private final MyAccessDeniedHandler myAccessDeniedHandler;

    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    private final MyLogoutSuccessHandler myLogoutSuccessHandler;

    private final MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

    private final MyAccessDecisionManager myAccessDecisionManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions().disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 禁用session
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(myAuthenticationEntryPoint)// 未登录处理
                        .accessDeniedHandler(myAccessDeniedHandler)// 无权限处理
                )
                .logout(logout -> logout.logoutSuccessHandler(myLogoutSuccessHandler))// 退出登录处理
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(multiLoginAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(filterSecurityInterceptor(authenticationManager), FilterSecurityInterceptor.class)
                .build();
    }

    /**
     * 多种登录方式Filter
     */
    @Bean
    public MultiLoginAuthenticationFilter multiLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        MultiLoginAuthenticationFilter multiLoginAuthenticationFilter = new MultiLoginAuthenticationFilter();
        multiLoginAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        multiLoginAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        multiLoginAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return multiLoginAuthenticationFilter;
    }

    /**
     * 自定义FilterSecurityInterceptor
     */
    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor(AuthenticationManager authenticationManager) {
        FilterSecurityInterceptor interceptor = new FilterSecurityInterceptor();
        interceptor.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
        interceptor.setAccessDecisionManager(myAccessDecisionManager);
        interceptor.setAuthenticationManager(authenticationManager);
        return interceptor;
    }

    /**
     * 使用自定义的UserDetailsService和密码加密器
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * 密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }
}
