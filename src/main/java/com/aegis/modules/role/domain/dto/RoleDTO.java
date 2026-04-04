package com.aegis.modules.role.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色DTO
 *
 * @author xuesong.lei
 * @since 2025-09-13
 */
@Data
@ApiModel("角色DTO")
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "角色名称不能为空")
    @Size(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, max = 32, message = "角色名称长度不能超过32个字符")
    private String roleName;

    @ApiModelProperty("角色编码")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "角色编码不能为空")
    @Size(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, max = 16, message = "角色编码长度不能超过16个字符")
    private String roleCode;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限,5-仅本人数据权限)")
    private String dataScope;

    @ApiModelProperty("部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @ApiModelProperty("角色状态(0-正常,1-停用)")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"0", "1"}, message = "状态只允许为0或1")
    private String status;

    @ApiModelProperty("部门组(数据权限)")
    private List<Long> deptIds;
}
