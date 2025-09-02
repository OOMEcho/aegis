package com.aegis.config.security.customize;

import com.aegis.common.constant.LoginRequestConstants;
import com.aegis.common.domain.dto.EmailLoginRequestDTO;
import com.aegis.common.domain.dto.PasswordLoginRequestDTO;
import com.aegis.common.domain.dto.SmsLoginRequestDTO;
import com.aegis.common.exception.LoginException;
import com.aegis.config.security.email.EmailAuthenticationToken;
import com.aegis.config.security.sms.SmsAuthenticationToken;
import com.aegis.utils.CaptchaUtils;
import com.aegis.utils.SpringContextUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
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

    public MultiLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (request.getContentType() == null || !request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthenticationServiceException("只支持 application/json 请求");
        }

        // 解析请求体
        Map<String, Object> map = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {
        });

        // 获取登录类型
        String loginType = (String) map.get("loginType");
        if (!StringUtils.hasText(loginType)) {
            throw new AuthenticationServiceException("loginType不能为空");
        }

        // 校验滑块验证码
        checkSlideCaptcha(map);

        Authentication authToken;

        switch (loginType) {
            case LoginRequestConstants.PASSWORD:
                PasswordLoginRequestDTO passwordRequest = objectMapper.convertValue(map, PasswordLoginRequestDTO.class);
                authToken = new UsernamePasswordAuthenticationToken(passwordRequest.getUsername(), passwordRequest.getPassword());
                break;
            case LoginRequestConstants.EMAIL:
                EmailLoginRequestDTO emailLoginRequestDTO = objectMapper.convertValue(map, EmailLoginRequestDTO.class);
                authToken = new EmailAuthenticationToken(emailLoginRequestDTO.getEmail(), emailLoginRequestDTO.getCode());
                break;
            case LoginRequestConstants.SMS:
                SmsLoginRequestDTO smsLoginRequestDTO = objectMapper.convertValue(map, SmsLoginRequestDTO.class);
                authToken = new SmsAuthenticationToken(smsLoginRequestDTO.getPhone(), smsLoginRequestDTO.getCode());
                break;
            default:
                throw new AuthenticationServiceException("不支持的 loginType: " + loginType);
        }

        setDetails(request, authToken);
        return this.getAuthenticationManager().authenticate(authToken);
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
}
