package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.RedisConstants;
import com.aegis.common.event.DataChangeListener;
import com.aegis.modules.resource.domain.entity.Resource;
import com.aegis.modules.resource.mapper.ResourceMapper;
import com.aegis.modules.whitelist.domain.entity.Whitelist;
import com.aegis.modules.whitelist.mapper.WhitelistMapper;
import com.aegis.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 23:04
 * @Description: 该类的主要功能就是通过当前的请求地址，获取该地址需要的权限编码(perm_code)
 */
@Component
@RequiredArgsConstructor
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final ResourceMapper resourceMapper;

    private final WhitelistMapper whitelistMapper;

    private final RedisUtils redisUtils;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @PostConstruct
    public void init() {
        // 系统启动时预加载资源数据
        loadDataSourceAllResource();
        // 系统启动时预加载白名单数据
        loadDataSourceAllWhitelist();
    }

    /**
     * 加载所有的资源存入Redis中
     * 在新增、修改、删除资源时,发布事件监听器{@link DataChangeListener},重新加载
     */
    public List<Resource> loadDataSourceAllResource() {
        if (redisUtils.hasKey(RedisConstants.RESOURCES)) {
            return redisUtils.getList(RedisConstants.RESOURCES, Resource.class);
        } else {
            List<Resource> allResource = resourceMapper.getAllResource();
            redisUtils.set(RedisConstants.RESOURCES, allResource, 1, TimeUnit.DAYS);
            return allResource;
        }
    }

    /**
     * 加载所有的白名单存入Redis中
     * 在新增、修改、删除白名单时,发布事件监听器{@link DataChangeListener},重新加载
     */
    public List<Whitelist> loadDataSourceAllWhitelist() {
        if (redisUtils.hasKey(RedisConstants.WHITELIST)) {
            return redisUtils.getList(RedisConstants.WHITELIST, Whitelist.class);
        } else {
            LambdaQueryWrapper<Whitelist> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Whitelist::getStatus, CommonConstants.NORMAL_STATUS);
            List<Whitelist> rules = whitelistMapper.selectList(queryWrapper);
            redisUtils.set(RedisConstants.WHITELIST, rules, 1, TimeUnit.DAYS);
            return rules;
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        final String method = request.getMethod();

        // OPTIONS 请求全部放行
        if (HttpMethod.OPTIONS.matches(method)) {
            return null;
        }

        // 获取请求路径
        final String requestURI = request.getRequestURI();

        // 判断请求路径是否在白名单内,在白名单内直接放行
        if (isWhitelisted(requestURI, method)) {
            return null;// 白名单直接放行
        }

        // 从 t_resource 中匹配 (request_method + request_uri)，获取 perm_code
        List<Resource> allResource = loadDataSourceAllResource();
        for (Resource resource : allResource) {
            if (matchesRequestMethod(resource.getRequestMethod(), method) &&
                    antPathMatcher.match(resource.getRequestUri(), requestURI)) {
                // 命中 → 返回 perm_code
                return SecurityConfig.createList(resource.getPermCode());
            }
        }

        // 未命中 → 返回 NONE（允许通过，由后续逻辑决定是否需要登录）
        return SecurityConfig.createList(CommonConstants.NONE);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        List<Resource> allResource = loadDataSourceAllResource();
        return allResource.stream()
                .map(Resource::getPermCode)
                .distinct()
                .map(SecurityConfig::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 检查URL是否在白名单中
     */
    private boolean isWhitelisted(String requestURI, String method) {
        List<Whitelist> whitelists = loadDataSourceAllWhitelist();
        return whitelists.stream()
                .anyMatch(whitelist ->
                        matchesRequestMethod(whitelist.getRequestMethod(), method) &&
                                antPathMatcher.match(whitelist.getRequestUri(), requestURI));
    }

    /**
     * 匹配HTTP方法
     */
    private boolean matchesRequestMethod(String configuredMethod, String requestMethod) {
        return CommonConstants.REQUEST_METHOD_ALL.equalsIgnoreCase(configuredMethod) ||
                configuredMethod.equalsIgnoreCase(requestMethod);
    }
}
