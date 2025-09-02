package com.aegis.common.domain.dto;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/2 21:25
 * @Description: 滑块验证码DTO
 */
@Data
public class CaptchaDTO {

    /**
     * 验证码key
     */
    private String captchaKey;

    /**
     * 滑块X轴位置
     */
    private Integer slideX;
}
