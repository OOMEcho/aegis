package com.aegis.modules.role.domain.dto;

import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/31 22:51
 * @Description: 角色数据权限DTO
 */
@Data
@ApiModel("角色数据权限DTO")
public class RoleDataScopeDTO {

    @ApiModelProperty("主键ID")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @ApiModelProperty("数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限,5-仅本人数据权限)")
    @NotBlank(groups = ValidGroup.Update.class, message = "数据范围不能为空")
    @EnumString(groups = ValidGroup.Update.class, value = {"1", "2", "3", "4"}, message = "数据范围参数错误")
    private String dataScope;

    @ApiModelProperty("部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @ApiModelProperty("部门组(数据权限)")
    private List<Long> deptIds;
}
