package com.aegis.common.domain.dto;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 21:26
 * @Description: 用户名密码DTO
 */
@Data
public class PasswordLoginRequestDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
