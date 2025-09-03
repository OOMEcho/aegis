package com.aegis.config.security.email;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.RedisConstants;
import com.aegis.common.exception.LoginException;
import com.aegis.config.security.customize.UserDetailsServiceImpl;
import com.aegis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/3 11:16
 * @Description: 邮箱登录认证逻辑
 */
@Component
@RequiredArgsConstructor
public class EmailAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;

    private final RedisUtils redisUtils;

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(EmailAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));
        String email = authentication.getPrincipal().toString();
        if (StrUtil.isEmpty(email)) {
            throw new LoginException("邮箱不能为空");
        }
        String code = authentication.getCredentials().toString();
        if (StrUtil.isEmpty(code)) {
            throw new BadCredentialsException("验证码不能为空");
        }

        // 校验验证码
        validateEmailCode(email, code);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 检查用户状态
        check(userDetails);

        return new EmailAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void validateEmailCode(String email, String code) {
        // 检查验证码错误次数（防暴力破解）
        String errorKey = RedisConstants.EMAIL_LOGIN_ERROR + email;
        String errorCount = redisUtils.get(errorKey);
        if (StrUtil.isNotEmpty(errorCount) && Integer.parseInt(errorCount) >= 5) {
            throw new LoginException("验证码错误次数过多，请30分钟后再试");
        }

        // 获取缓存中的验证码
        String emailLogin = RedisConstants.EMAIL_LOGIN + email;
        String emailCode = redisUtils.get(emailLogin);
        if (StrUtil.isEmpty(emailCode)) {
            throw new LoginException("验证码已过期");
        }

        // 验证码校验
        if (!code.equals(emailCode)) {
            // 错误次数+1
            redisUtils.increment(errorKey, 30);
            throw new LoginException("验证码不正确");
        }

        // 验证成功，清除验证码和错误计数
        redisUtils.delete(emailLogin);
        redisUtils.delete(errorKey);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (EmailAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void check(UserDetails user) {
        if (!user.isEnabled()) {
            throw new DisabledException(
                    this.messages.getMessage("AccountStatusUserDetailsChecker.disabled", "User is disabled"));
        }
    }
}
