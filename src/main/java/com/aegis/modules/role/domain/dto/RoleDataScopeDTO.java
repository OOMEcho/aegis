package com.aegis.modules.role.domain.dto;

import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色数据权限DTO
 *
 * @author xuesong.lei
 * @since 2026-01-31
 */
@Data
@Schema(description = "角色数据权限DTO")
public class RoleDataScopeDTO {

    @Schema(description = "主键ID")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @Schema(description = "数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限,5-仅本人数据权限)")
    @NotBlank(groups = ValidGroup.Update.class, message = "数据范围不能为空")
    @EnumString(groups = ValidGroup.Update.class, value = {"1", "2", "3", "4", "5"}, message = "数据范围参数错误")
    private String dataScope;

    @Schema(description = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @Schema(description = "部门组(数据权限)")
    private List<Long> deptIds;
}
