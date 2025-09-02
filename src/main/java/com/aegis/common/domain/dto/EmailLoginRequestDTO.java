package com.aegis.common.domain.dto;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 15:51
 * @Description: 邮箱验证码DTO
 */
@Data
public class EmailLoginRequestDTO {

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;
}
