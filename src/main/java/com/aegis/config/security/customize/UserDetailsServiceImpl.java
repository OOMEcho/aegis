package com.aegis.config.security.customize;

import com.aegis.common.exception.LoginException;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 自定义用户认证服务
 *
 * @author xuesong.lei
 * @since 2025/8/30 22:15
 */
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userMapper.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new LoginException("登录失败,请检查您的账号或凭证信息");
        }
        return new SecurityUser(user);
    }
}
