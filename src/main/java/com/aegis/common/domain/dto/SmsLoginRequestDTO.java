package com.aegis.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 短信登录DTO
 *
 * @author xuesong.lei
 * @since 2025-09-02
 */
@Data
@ApiModel("短信登录DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsLoginRequestDTO {

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String code;
}
