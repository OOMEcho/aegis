package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.config.security.customize.WhitelistProperties;
import com.aegis.modules.menu.domain.entity.Menu;
import com.aegis.modules.menu.mapper.MenuMapper;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
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

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final RedisUtils redisUtils;

    private final WhitelistProperties whitelistProperties;

    @PostConstruct
    public void init() {
        // 系统启动时预加载菜单数据
        loadDataSourceAllUrl();
    }

    /**
     * 加载所有的URL存入Redis中
     * 在新增、修改、删除角色关联菜单时,删除Redis中的数据,重新加载
     */
    public List<Menu> loadDataSourceAllUrl() {
        if (redisUtils.hasKey(CommonConstants.MENUS)) {
            return redisUtils.getList(CommonConstants.MENUS, Menu.class);
        } else {
            List<Menu> allMenu = menuMapper.getAllMenu();
            redisUtils.set(CommonConstants.MENUS, allMenu, 1, TimeUnit.DAYS);
            return allMenu;
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        // 放行指定路径
        for (String permitUrl : whitelistProperties.getUrls()) {
            if (antPathMatcher.match(permitUrl, requestUrl)) {
                return null; // 返回 null 表示无需权限
            }
        }
        List<Menu> allMenu = loadDataSourceAllUrl();
        for (Menu menu : allMenu) {
            if (antPathMatcher.match(menu.getRequestUrl(), requestUrl)) {
                String[] roles = menu.getRoleList().stream().map(Role::getRoleCode).toArray(String[]::new);
                return SecurityConfig.createList(roles);
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
