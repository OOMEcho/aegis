package com.aegis.modules.common.service;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/3 15:18
 * @Description: 邮箱业务层
 */
public interface EmailService {

    String sendRegisterCode(String email);
}
