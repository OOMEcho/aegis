package com.aegis.modules.role.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 角色取消授权用户DTO
 *
 * @author xuesong.lei
 * @since 2025-09-13
 */
@Data
@ApiModel("角色取消授权用户DTO")
public class CancelDTO {

    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty("角色ID")
    private Long roleId;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private Long userId;
}
