package com.aegis.utils;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/22 14:20
 * @Description: Security工具类
 */
public final class SecurityUtils {

    /**
     * 请求级别的用户缓存，避免同一请求内多次查库
     * 由 JwtTokenFilter 在 finally 中调用 clearCurrentUser() 清理
     */
    private static final ThreadLocal<User> CURRENT_USER_CACHE = new ThreadLocal<>();

    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return CommonConstants.ANONYMOUS;
        }
        return authentication.getName();
    }

    public static Long getUserId() {
        User currentUser = getCurrentUser();
        return currentUser.getId();
    }

    public static User getCurrentUser() {
        // 优先从 ThreadLocal 缓存获取
        User cached = CURRENT_USER_CACHE.get();
        if (cached != null) {
            return cached;
        }

        UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);
        String username = getUsername();
        if (CommonConstants.ANONYMOUS.equals(username)) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }

        // 缓存到 ThreadLocal
        CURRENT_USER_CACHE.set(user);
        return user;
    }

    /**
     * 清除当前线程的用户缓存，应在请求结束时调用
     */
    public static void clearCurrentUser() {
        CURRENT_USER_CACHE.remove();
    }

    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public static boolean matchesPassword(String oldPassword, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(oldPassword, newPassword);
    }
}
