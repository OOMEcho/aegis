package com.aegis.modules.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.RedisConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.exception.LoginException;
import com.aegis.modules.common.service.EmailService;
import com.aegis.utils.EmailUtils;
import com.aegis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/3 15:18
 * @Description: 邮箱业务实现层
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailUtils emailUtils;

    private final RedisUtils redisUtils;

    // 验证码过期时间，单位：分钟
    public static final Integer EXPIRE_MINUTES = 5;

    @Override
    public String sendRegisterCode(String email) {
        if (emailUtils.isValidEmail(email)) {
            throw new BusinessException("邮箱格式不正确");
        }
        String errorKey = RedisConstants.EMAIL_LOGIN_ERROR + email;
        String errorCount = redisUtils.get(errorKey);
        if (StrUtil.isNotEmpty(errorCount) && Integer.parseInt(errorCount) >= 5) {
            throw new LoginException("验证码错误次数过多，请30分钟后再试");
        }

        String frequencyKey = RedisConstants.EMAIL_SEND_FREQUENCY + email;
        if (redisUtils.hasKey(frequencyKey)) {
            throw new BusinessException("发送过于频繁，请60秒后再试");
        }

        String dailyKey = RedisConstants.EMAIL_DAILY_LIMIT + email + ":" + LocalDate.now();
        String dailyCount = redisUtils.get(dailyKey);
        if (StrUtil.isNotEmpty(dailyCount) && Integer.parseInt(dailyCount) >= 10) {
            throw new BusinessException("今日发送次数已达上限");
        }

        String verifyCode = getCode();

        emailUtils.sendVerificationCode(email, verifyCode, EXPIRE_MINUTES);

        redisUtils.set(RedisConstants.EMAIL_LOGIN + email, verifyCode, 5, TimeUnit.MINUTES);
        redisUtils.set(frequencyKey, "1", 60, TimeUnit.SECONDS);
        // 发送次数递增，并设置每天凌晨过期
        redisUtils.increment(dailyKey, 1);
        redisUtils.expireAt(dailyKey, Date.from(LocalDate.now().plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()));

        return "发送成功";
    }

    private String getCode() {
        String numbers = "1234567890";
        int codeLength = 6;
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(10);
            builder.append(numbers.charAt(index));
        }
        return builder.toString();
    }
}
