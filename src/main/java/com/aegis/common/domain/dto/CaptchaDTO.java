package com.aegis.common.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 滑块验证码DTO
 *
 * @author xuesong.lei
 * @since 2025/9/2 21:25
 */
@Data
@Schema(description = "滑块验证码DTO")
public class CaptchaDTO {

    /**
     * 验证码key
     */
    @Schema(description = "验证码key")
    private String captchaKey;

    /**
     * 滑块X轴位置
     */
    @Schema(description = "滑块X轴位置")
    private Integer slideX;
}
