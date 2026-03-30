package com.aegis.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户名密码DTO
 *
 * @author xuesong.lei
 * @since 2025/9/2 21:26
 */
@Data
@Schema(description = "用户名密码DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordLoginRequestDTO {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
}
