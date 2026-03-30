package com.aegis.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户名密码DTO
 *
 * @author xuesong.lei
 * @since 2025/9/2 21:26
 */
@Data
@ApiModel("用户名密码DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordLoginRequestDTO {

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
}
