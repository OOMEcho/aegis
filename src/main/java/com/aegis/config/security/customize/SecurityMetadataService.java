package com.aegis.config.security.customize;


import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.RedisConstants;
import com.aegis.common.event.DataChangeListener;
import com.aegis.modules.resource.domain.entity.Resource;
import com.aegis.modules.resource.mapper.ResourceMapper;
import com.aegis.modules.whitelist.domain.entity.Whitelist;
import com.aegis.modules.whitelist.mapper.WhitelistMapper;
import com.aegis.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 安全元数据服务，提供URL权限映射
 *
 * @author xuesong.lei
 * @since 2025/9/24 10:04
 */
@Component
@RequiredArgsConstructor
public class SecurityMetadataService {

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
     * 获取指定URL和HTTP方法需要的角色
     */
    public Set<String> getRequiredPermissions(String requestURI, String method) {
        // 判断请求路径是否在白名单内,在白名单内直接放行
        if (isWhitelisted(requestURI, method)) {
            return null;// 白名单直接放行
        }

        // 从 t_resource 中匹配 (request_method + request_uri)，获取 perm_code
        List<Resource> allResource = loadDataSourceAllResource();
        for (Resource resource : allResource) {
            if (matchesRequestMethod(resource.getRequestMethod(), method) &&
                    antPathMatcher.match(resource.getRequestUri(), requestURI)) {
                return Set.of(resource.getPermCode());
            }
        }

        // 默认需要认证但无特定角色要求
        return Set.of(CommonConstants.NONE);
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

    /**
     * 加载所有的资源存入Redis中（使用分布式锁避免并发重复查询）
     * 在新增、修改、删除资源时,发布事件监听器{@link DataChangeListener},重新加载
     */
    public List<Resource> loadDataSourceAllResource() {
        // 第一次检查（无锁）
        if (redisUtils.hasKey(RedisConstants.RESOURCES)) {
            return redisUtils.getList(RedisConstants.RESOURCES, Resource.class);
        }

        // 尝试获取锁
        String requestId = UUID.randomUUID().toString();
        try {
            if (redisUtils.tryLock(RedisConstants.RESOURCE_LOCK_KEY, requestId, 10, TimeUnit.SECONDS)) {
                try {
                    // 第二次检查（持有锁）
                    if (redisUtils.hasKey(RedisConstants.RESOURCES)) {
                        return redisUtils.getList(RedisConstants.RESOURCES, Resource.class);
                    }

                    // 确实没有缓存，从数据库加载
                    List<Resource> allResource = resourceMapper.getAllResource();
                    redisUtils.set(RedisConstants.RESOURCES, allResource, 1, TimeUnit.DAYS);
                    return allResource;
                } finally {
                    // 释放锁
                    redisUtils.unlock(RedisConstants.RESOURCE_LOCK_KEY, requestId);
                }
            } else {
                // 获取锁失败，等待一小段时间后重试读取缓存
                // 此时其他线程可能正在加载数据
                Thread.sleep(100);
                if (redisUtils.hasKey(RedisConstants.RESOURCES)) {
                    return redisUtils.getList(RedisConstants.RESOURCES, Resource.class);
                }
                // 如果还是没有，直接查数据库（降级方案）
                return resourceMapper.getAllResource();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 异常情况直接查数据库
            return resourceMapper.getAllResource();
        }
    }

    /**
     * 加载所有的白名单存入Redis中（使用分布式锁避免并发重复查询）
     * 在新增、修改、删除白名单时,发布事件监听器{@link DataChangeListener},重新加载
     */
    public List<Whitelist> loadDataSourceAllWhitelist() {
        // 第一次检查（无锁）
        if (redisUtils.hasKey(RedisConstants.WHITELIST)) {
            return redisUtils.getList(RedisConstants.WHITELIST, Whitelist.class);
        }

        // 尝试获取锁
        String requestId = UUID.randomUUID().toString();
        try {
            if (redisUtils.tryLock(RedisConstants.WHITELIST_LOCK_KEY, requestId, 10, TimeUnit.SECONDS)) {
                try {
                    // 第二次检查（持有锁）
                    if (redisUtils.hasKey(RedisConstants.WHITELIST)) {
                        return redisUtils.getList(RedisConstants.WHITELIST, Whitelist.class);
                    }

                    // 从数据库加载
                    LambdaQueryWrapper<Whitelist> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Whitelist::getStatus, CommonConstants.NORMAL_STATUS);
                    List<Whitelist> rules = whitelistMapper.selectList(queryWrapper);
                    redisUtils.set(RedisConstants.WHITELIST, rules, 1, TimeUnit.DAYS);
                    return rules;
                } finally {
                    // 释放锁
                    redisUtils.unlock(RedisConstants.WHITELIST_LOCK_KEY, requestId);
                }
            } else {
                // 获取锁失败，等待后重试
                Thread.sleep(100);
                if (redisUtils.hasKey(RedisConstants.WHITELIST)) {
                    return redisUtils.getList(RedisConstants.WHITELIST, Whitelist.class);
                }
                // 降级：直接查数据库
                LambdaQueryWrapper<Whitelist> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Whitelist::getStatus, CommonConstants.NORMAL_STATUS);
                return whitelistMapper.selectList(queryWrapper);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LambdaQueryWrapper<Whitelist> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Whitelist::getStatus, CommonConstants.NORMAL_STATUS);
            return whitelistMapper.selectList(queryWrapper);
        }
    }
}
