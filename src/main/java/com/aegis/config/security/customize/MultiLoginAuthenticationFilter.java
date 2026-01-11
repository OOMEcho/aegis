package com.aegis.config.security.customize;

import com.aegis.common.constant.LoginRequestConstants;
import com.aegis.common.domain.dto.EmailLoginRequestDTO;
import com.aegis.common.domain.dto.PasswordLoginRequestDTO;
import com.aegis.common.domain.dto.SmsLoginRequestDTO;
import com.aegis.common.exception.LoginException;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.config.security.LoginSecurityProperties;
import com.aegis.config.security.email.EmailAuthenticationToken;
import com.aegis.config.security.sms.SmsAuthenticationToken;
import com.aegis.utils.CaptchaUtils;
import com.aegis.utils.RequestUtils;
import com.aegis.utils.RsaUtils;
import com.aegis.utils.SpringContextUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 20:32
 * @Description: 将表单登录替换为JSON格式登录，多种登录方式集合，并增加滑块验证码校验
 */
public class MultiLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final LoginSecurityProperties loginSecurityProperties;

    public MultiLoginAuthenticationFilter(LoginSecurityProperties loginSecurityProperties) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.loginSecurityProperties = loginSecurityProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (request.getContentType() == null || !request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            throw new LoginException("只支持 application/json 请求");
        }

        String rawJson = RequestUtils.getRequestBody(request);
        Map<String, Object> map = objectMapper.readValue(rawJson, new TypeReference<Map<String, Object>>() {
        });

        // 获取登录类型
        final String loginType = (String) map.get(LoginRequestConstants.LOGIN_TYPE);
        if (!StringUtils.hasText(loginType)) {
            throw new LoginException("loginType不能为空");
        }

        // 校验滑块验证码（根据配置决定是否启用）
        if (loginSecurityProperties.isEnableCaptcha()) {
            checkSlideCaptcha(map);
        }

        // 根据不同类型构建 Authentication
        Authentication authToken = buildAuthenticationToken(loginType, map);

        setDetails(request, authToken);

        return this.getAuthenticationManager().authenticate(authToken);
    }

    private Authentication buildAuthenticationToken(String loginType, Map<String, Object> map) {
        switch (loginType) {
            case LoginRequestConstants.PASSWORD:
                PasswordLoginRequestDTO passwordDTO = objectMapper.convertValue(map, PasswordLoginRequestDTO.class);
                checkRequestParam(passwordDTO.getUsername(), passwordDTO.getPassword());
                String password = loginSecurityProperties.isEnablePasswordEncryption()
                        ? RsaUtils.decryptByPrivateKey(passwordDTO.getPassword())
                        : passwordDTO.getPassword();
                return new UsernamePasswordAuthenticationToken(passwordDTO.getUsername(), password);
            case LoginRequestConstants.EMAIL:
                EmailLoginRequestDTO emailDTO = objectMapper.convertValue(map, EmailLoginRequestDTO.class);
                checkRequestParam(emailDTO.getEmail(), emailDTO.getCode());
                return new EmailAuthenticationToken(emailDTO.getEmail(), emailDTO.getCode());
            case LoginRequestConstants.SMS:
                SmsLoginRequestDTO smsDTO = objectMapper.convertValue(map, SmsLoginRequestDTO.class);
                checkRequestParam(smsDTO.getPhone(), smsDTO.getCode());
                return new SmsAuthenticationToken(smsDTO.getPhone(), smsDTO.getCode());
            default:
                throw new LoginException("不支持的 loginType: " + loginType);
        }
    }

    protected void setDetails(HttpServletRequest request, Authentication authRequest) {
        if (authRequest instanceof AbstractAuthenticationToken) {
            AbstractAuthenticationToken token = (AbstractAuthenticationToken) authRequest;
            token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        }
    }

    private void checkSlideCaptcha(Map<String, Object> map) {
        String captchaKey = (String) map.get("captchaKey");
        Integer slideX = (Integer) map.get("slideX");
        if (!StringUtils.hasText(captchaKey) || slideX == null) {
            throw new LoginException("滑块验证码参数不能为空");
        }
        CaptchaUtils captchaUtils = SpringContextUtil.getBean(CaptchaUtils.class);
        if (!captchaUtils.verifyCaptcha(captchaKey, slideX)) {
            throw new LoginException("验证码校验失败");
        }
    }

    private void checkRequestParam(Object principal, Object credentials) {
        if (!StringUtils.hasText(principal.toString()) || !StringUtils.hasText(credentials.toString())) {
            throw new LoginException(ResultCodeEnum.ACCOUNT_ERROR.getMessage());
        }
    }
}
