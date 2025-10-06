package com.aegis.config.security.email;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.exception.LoginException;
import com.aegis.config.security.customize.UserDetailsServiceImpl;
import com.aegis.modules.common.service.EmailService;
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

    private final EmailService emailService;

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
        emailService.validateEmailCode(email, code, true);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 检查用户状态
        check(userDetails);

        return new EmailAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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
