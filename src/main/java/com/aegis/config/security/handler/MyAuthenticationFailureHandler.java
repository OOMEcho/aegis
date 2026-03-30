package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.ip2region.Ip2regionService;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.modules.log.domain.entity.SysLoginLog;
import com.aegis.modules.log.mapper.SysLoginLogMapper;
import com.aegis.utils.IpUtils;
import com.aegis.utils.RequestUtils;
import com.aegis.utils.ResponseUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 登录失败处理逻辑
 *
 * @author xuesong.lei
 * @since 2025/9/2 22:39
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final Ip2regionService ip2regionService;

    private final SysLoginLogMapper sysLoginLogMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        // 获取请求体
        String rawJson = RequestUtils.getRequestBody(request);
        Map<String, Object> map = objectMapper.readValue(rawJson, new TypeReference<Map<String, Object>>() {
        });

        // 记录登录日志
        loginLog(request, map, e);

        if (e instanceof AccountExpiredException
                || e instanceof BadCredentialsException
                || e instanceof CredentialsExpiredException
                || e instanceof LockedException) {
            ResponseUtils.writeError(response, ResultCodeEnum.ACCOUNT_ERROR);
        } else if (e instanceof DisabledException) {
            ResponseUtils.writeError(response, ResultCodeEnum.USER_IS_DISABLE);
        } else {
            ResponseUtils.writeError(response, e.getMessage());
        }
    }

    private void loginLog(HttpServletRequest request, Map<String, Object> map, AuthenticationException e) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(CommonConstants.USER_AGENT));
        final String ip = IpUtils.getIpAddr(request);
        final String region = ip2regionService.getRegion(ip);
        SysLoginLog sysLoginLog = new SysLoginLog();
        final String loginUsername = Arrays.stream(new String[]{"username", "email", "phone"})
                .map(map::get)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .findFirst()
                .orElse(CommonConstants.ANONYMOUS);
        sysLoginLog.setLoginUsername(loginUsername);
        sysLoginLog.setLoginIp(ip);
        sysLoginLog.setLoginLocal(region);
        sysLoginLog.setLoginBrowser(userAgent.getBrowser().getName());
        sysLoginLog.setLoginOs(userAgent.getOperatingSystem().getName());
        sysLoginLog.setLoginTime(new Date());
        sysLoginLog.setLoginStatus(CommonConstants.DISABLE_STATUS);
        sysLoginLog.setErrorMessage(e.getMessage());
        sysLoginLogMapper.insert(sysLoginLog);
    }
}
