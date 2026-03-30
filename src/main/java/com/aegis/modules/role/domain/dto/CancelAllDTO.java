package com.aegis.modules.role.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量选择角色授权用户DTO
 *
 * @author xuesong.lei
 * @since 2025/09/13 18:41
 */
@Data
@ApiModel("批量选择角色授权用户DTO")
public class CancelAllDTO {

    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty("角色ID")
    private Long roleId;

    @NotNull(message = "用户组ID不能为空")
    @ApiModelProperty("用户组ID")
    private List<Long> userIds;
}
