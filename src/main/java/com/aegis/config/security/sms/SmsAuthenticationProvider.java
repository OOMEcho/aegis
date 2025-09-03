package com.aegis.config.security.sms;

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
 * @Description: 短信登录认证逻辑
 */
@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;

    private final RedisUtils redisUtils;

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(SmsAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));
        String phone = authentication.getPrincipal().toString();
        if (StrUtil.isEmpty(phone)) {
            throw new LoginException("手机号不能为空");
        }
        String code = authentication.getCredentials().toString();
        if (StrUtil.isEmpty(code)) {
            throw new BadCredentialsException("验证码不能为空");
        }

        // 校验验证码
        validateSmsCode(phone, code);

        UserDetails userDetails = userDetailsService.loadUserByUsername(phone);

        // 检查用户状态
        check(userDetails);

        return new SmsAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void validateSmsCode(String phone, String code) {
        // 检查验证码错误次数（防暴力破解）
        String errorKey = RedisConstants.SMS_LOGIN_ERROR + phone;
        String errorCount = redisUtils.get(errorKey);
        if (StrUtil.isNotEmpty(errorCount) && Integer.parseInt(errorCount) >= 5) {
            throw new LoginException("验证码错误次数过多，请30分钟后再试");
        }

        // 获取缓存中的验证码
        String smsLogin = RedisConstants.SMS_LOGIN + phone;
        String smsCode = redisUtils.get(smsLogin);
        if (StrUtil.isEmpty(smsCode)) {
            throw new LoginException("验证码已过期");
        }

        // 验证码校验
        if (!code.equals(smsCode)) {
            // 错误次数+1
            redisUtils.increment(errorKey, 30);
            throw new LoginException("验证码不正确");
        }

        // 验证成功，清除验证码和错误计数
        redisUtils.delete(smsLogin);
        redisUtils.delete(errorKey);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void check(UserDetails user) {
        if (!user.isEnabled()) {
            throw new DisabledException(
                    this.messages.getMessage("AccountStatusUserDetailsChecker.disabled", "User is disabled"));
        }
    }
}
