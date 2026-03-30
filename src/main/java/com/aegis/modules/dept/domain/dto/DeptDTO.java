package com.aegis.modules.dept.domain.dto;

import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * 部门DTO
 *
 * @author xuesong.lei
 * @since 2025/9/9 17:56
 */
@Data
@Schema(description = "部门DTO")
public class DeptDTO {

    @Schema(description = "主键")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @Schema(description = "父部门ID")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "上级部门不能为空")
    private Long parentId;

    @Schema(description = "部门名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "部门名称不能为空")
    private String deptName;

    @Schema(description = "显示顺序")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "显示顺序不能为空")
    private Integer orderNum;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门状态")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"0", "1"}, message = "状态只允许为0或1")
    private String status;
}
