package com.aegis.modules.common.service;

/**
 * 邮箱业务层
 *
 * @author xuesong.lei
 * @since 2025/9/3 15:18
 */
public interface EmailService {

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 响应消息
     */
    String sendEmailCode(String email);

    /**
     * 校验邮箱验证码
     *
     * @param email   邮箱
     * @param code    验证码
     * @param isLogin 是否是登录场景
     */
    void validateEmailCode(String email, String code, boolean isLogin);
}
