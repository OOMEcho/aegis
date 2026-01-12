package com.aegis.modules.permission.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:20
 * @Description: 权限DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("权限DTO")
public class PermissionDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @NotNull(message = "ID不能为空", groups = {ValidGroup.Update.class})
    private Long id;

    @ApiModelProperty("权限编码")
    @NotBlank(message = "权限编码不能为空", groups = {ValidGroup.Create.class})
    private String permCode;

    @ApiModelProperty("权限名称")
    @NotBlank(message = "权限名称不能为空", groups = {ValidGroup.Create.class})
    private String permName;

    @ApiModelProperty("权限类型(M=页面,B=按钮,A=API)")
    @NotBlank(message = "权限类型不能为空", groups = {ValidGroup.Create.class})
    private String permType;

    @ApiModelProperty("状态(0-正常,1-停用)")
    private String status;

    @ApiModelProperty("备注")
    private String remark;
}
