package com.aegis.modules.common.service;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/14 16:14
 * @Description: 短信业务层
 */
public interface SmsService {

    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @return 响应消息
     */
    String sendPhoneCode(String phone);

    /**
     * 校验手机验证码
     *
     * @param phone   手机号
     * @param code    验证码
     * @param isLogin 是否是登录场景
     */
    void validateSmsCode(String phone, String code, boolean isLogin);
}
