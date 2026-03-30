package com.aegis.modules.role.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色DTO
 *
 * @author xuesong.lei
 * @since 2025/9/13 17:25
 */
@Data
@Schema(description = "角色DTO")
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends PageDTO {

    @Schema(description = "主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "角色名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "角色名称不能为空")
    @Size(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, max = 32, message = "角色名称长度不能超过32个字符")
    private String roleName;

    @Schema(description = "角色编码")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "角色编码不能为空")
    @Size(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, max = 16, message = "角色编码长度不能超过16个字符")
    private String roleCode;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限,5-仅本人数据权限)")
    private String dataScope;

    @Schema(description = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @Schema(description = "角色状态(0-正常,1-停用)")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"0", "1"}, message = "状态只允许为0或1")
    private String status;

    @Schema(description = "部门组(数据权限)")
    private List<Long> deptIds;
}
