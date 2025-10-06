package com.aegis.modules.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.RedisConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.exception.LoginException;
import com.aegis.modules.common.service.SmsService;
import com.aegis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/14 16:14
 * @Description: 短信业务实现层
 */
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final RedisUtils redisUtils;

    @Override
    public String sendPhoneCode(String phone) {
        // TODO 目前个人开发者申请短信服务门槛较高,暂时不做短信发送功能,直接返回成功

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public void validateSmsCode(String phone, String code, boolean isLogin) {
        // 检查验证码错误次数（防暴力破解）
        String errorKey = RedisConstants.SMS_LOGIN_ERROR + phone;
        String errorCount = redisUtils.get(errorKey);
        if (StrUtil.isNotEmpty(errorCount) && Integer.parseInt(errorCount) >= 5) {
            if (isLogin){
                throw new LoginException("手机验证码错误次数过多，请30分钟后再试");
            }else {
                throw new BusinessException("手机验证码错误次数过多，请30分钟后再试");
            }
        }

        // 获取缓存中的验证码
        String smsLogin = RedisConstants.SMS_LOGIN + phone;
        String smsCode = redisUtils.get(smsLogin);
        if (StrUtil.isEmpty(smsCode)) {
            if (isLogin){
                throw new LoginException("手机验证码已过期");
            }else {
                throw new BusinessException("手机验证码已过期");
            }
        }

        // 验证码校验
        if (!code.equals(smsCode)) {
            // 错误次数+1
            redisUtils.increment(errorKey, 1);
            redisUtils.expire(errorKey, 30, TimeUnit.MINUTES);
            if (isLogin){
                throw new LoginException("手机验证码不正确");
            }else {
                throw new BusinessException("手机验证码不正确");
            }
        }

        // 验证成功，清除验证码和错误计数
        redisUtils.delete(smsLogin);
        redisUtils.delete(errorKey);
    }
}
