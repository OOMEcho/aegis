package com.aegis.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 邮箱验证码DTO
 *
 * @author xuesong.lei
 * @since 2025/9/2 15:51
 */
@Data
@Schema(description = "邮箱验证码DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailLoginRequestDTO {

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;
}
