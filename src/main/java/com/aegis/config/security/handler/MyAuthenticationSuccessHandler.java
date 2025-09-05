package com.aegis.config.security.handler;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.ip2region.Ip2regionService;
import com.aegis.modules.log.domain.entity.SysLoginLog;
import com.aegis.modules.log.mapper.SysLoginLogMapper;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.mapper.UserMapper;
import com.aegis.utils.IpUtils;
import com.aegis.utils.JwtTokenUtil;
import com.aegis.utils.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 22:39
 * @Description: 登录成功逻辑
 */
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;

    private final Ip2regionService ip2regionService;

    private final SysLoginLogMapper sysLoginLogMapper;

    private final UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 生成token
        JwtTokenUtil.TokenResponse tokenResponse = jwtTokenUtil.generateTokenResponse(authentication);

        // 将refreshToken放入HttpOnly的Cookie中
        Cookie cookie = new Cookie(CommonConstants.REFRESH_TOKEN_COOKIE, tokenResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/user/refreshToken");
        cookie.setSecure(false); // 如果你本地是 http，可以临时改为 false
        cookie.setMaxAge(Math.toIntExact(jwtTokenUtil.getRefreshTokenExpiration()));
        response.addCookie(cookie);

        // 记录登录日志
        loginLog(request, authentication);

        // 记录用户最后一次登录时间
        userLoginLog(request, authentication.getName());

        ResponseUtils.writeSuccess(response, tokenResponse.getAccessToken());
    }

    private void loginLog(HttpServletRequest request, Authentication authentication) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(CommonConstants.USER_AGENT));
        final String ip = IpUtils.getIpAddr(request);
        final String region = ip2regionService.getRegion(ip);
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setLoginUsername(authentication.getName());
        sysLoginLog.setLoginIp(ip);
        sysLoginLog.setLoginLocal(region);
        sysLoginLog.setLoginBrowser(userAgent.getBrowser().getName());
        sysLoginLog.setLoginOs(userAgent.getOperatingSystem().getName());
        sysLoginLog.setLoginTime(new Date());
        sysLoginLog.setLoginStatus(CommonConstants.NORMAL_STATUS);
        sysLoginLogMapper.insert(sysLoginLog);
    }

    private void userLoginLog(HttpServletRequest request, String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            user.setLastLoginIp(IpUtils.getIpAddr(request));
            user.setLastLoginTime(new Date());
            userMapper.updateById(user);
        }
    }
}
