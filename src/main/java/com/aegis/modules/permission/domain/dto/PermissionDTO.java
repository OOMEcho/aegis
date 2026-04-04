package com.aegis.modules.permission.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限DTO
 *
 * @author xuesong.lei
 * @since 2026-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限DTO")
public class PermissionDTO extends PageDTO {

    @Schema(description = "主键ID")
    @NotNull(message = "ID不能为空", groups = {ValidGroup.Update.class})
    private Long id;

    @Schema(description = "权限编码")
    @NotBlank(message = "权限编码不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String permCode;

    @Schema(description = "权限名称")
    @NotBlank(message = "权限名称不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String permName;

    @Schema(description = "权限类型(M=页面,B=按钮,A=API)")
    @NotBlank(message = "权限类型不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String permType;

    @Schema(description = "状态(0-正常,1-停用)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
