package com.aegis.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 短信登录DTO
 *
 * @author xuesong.lei
 * @since 2025-09-02
 */
@Data
@Schema(description = "短信登录DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsLoginRequestDTO {

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;
}
