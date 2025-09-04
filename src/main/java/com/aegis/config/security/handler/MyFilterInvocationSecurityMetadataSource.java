package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.RedisConstants;
import com.aegis.modules.menu.domain.entity.Menu;
import com.aegis.modules.menu.mapper.MenuMapper;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.whitelist.domain.entity.Whitelist;
import com.aegis.modules.whitelist.mapper.WhitelistMapper;
import com.aegis.utils.RedisUtils;
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
 * @Description: 该类的主要功能就是通过当前的请求地址，获取该地址需要的用户角色
 */
@Component
@RequiredArgsConstructor
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final MenuMapper menuMapper;

    private final WhitelistMapper whitelistMapper;

    private final RedisUtils redisUtils;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @PostConstruct
    public void init() {
        // 系统启动时预加载菜单数据
        loadDataSourceAllUrl();
        // 系统启动时预加载白名单数据
        loadWhitelistFromCache();
    }

    /**
     * 加载所有的URL存入Redis中
     * TODO 在新增、修改、删除角色关联菜单时,删除Redis中的数据,重新加载
     */
    public List<Menu> loadDataSourceAllUrl() {
        if (redisUtils.hasKey(RedisConstants.MENUS)) {
            return redisUtils.getList(RedisConstants.MENUS, Menu.class);
        } else {
            List<Menu> allMenu = menuMapper.getAllMenu();
            redisUtils.set(RedisConstants.MENUS, allMenu, 1, TimeUnit.DAYS);
            return allMenu;
        }
    }

    /**
     * 加载所有的白名单存入Redis中
     * TODO 在新增、修改、删除白名单时,删除Redis中的数据,重新加载
     */
    public List<Whitelist> loadWhitelistFromCache() {
        if (redisUtils.hasKey(RedisConstants.WHITELIST)) {
            return redisUtils.getList(RedisConstants.WHITELIST, Whitelist.class);
        } else {
            List<Whitelist> rules = whitelistMapper.getAllWhitelist();
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
        List<Whitelist> whitelists = loadWhitelistFromCache();
        for (Whitelist whitelist : whitelists) {
            if (CommonConstants.REQUEST_METHOD_ALL.equalsIgnoreCase(whitelist.getRequestMethod()) || whitelist.getRequestMethod().equalsIgnoreCase(method)) {
                if (antPathMatcher.match(whitelist.getRequestUri(), requestURI)) {
                    return null;
                }
            }
        }

        // 获取所有菜单数据,匹配请求路径,获取该路径需要的角色
        List<Menu> allMenu = loadDataSourceAllUrl();
        for (Menu menu : allMenu) {
            if (CommonConstants.REQUEST_METHOD_ALL.equalsIgnoreCase(menu.getRequestMethod()) || menu.getRequestMethod().equalsIgnoreCase(method)) {
                if (antPathMatcher.match(menu.getRequestUri(), requestURI)) {
                    String[] roles = menu.getRoleList().stream().map(Role::getRoleCode).toArray(String[]::new);
                    return SecurityConfig.createList(roles);
                }
            }
        }

        return SecurityConfig.createList(CommonConstants.NONE);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        List<Menu> allMenu = loadDataSourceAllUrl();
        return allMenu.stream()
                .flatMap(menu -> menu.getRoleList().stream())
                .map(Role::getRoleCode)
                .distinct()
                .map(SecurityConfig::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
