package com.aegis.common.domain.dto;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 22:11
 * @Description: 短信登录DTO
 */
@Data
public class SmsLoginRequestDTO {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;
}
