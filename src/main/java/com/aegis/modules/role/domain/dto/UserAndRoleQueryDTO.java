package com.aegis.modules.role.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 用户和角色DTO
 *
 * @author xuesong.lei
 * @since 2025/09/13 18:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户和角色DTO")
public class UserAndRoleQueryDTO extends PageDTO {

    @ApiModelProperty("角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("电话")
    private String phone;
}
