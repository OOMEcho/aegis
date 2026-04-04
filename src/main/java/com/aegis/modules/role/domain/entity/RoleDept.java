package com.aegis.modules.role.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import java.io.Serializable;

/**
 * 角色和部门关联表
 *
 * @author xuesong.lei
 * @since 2025-08-30
 * @TableName t_role_dept
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("角色和部门关联表")
@TableName(value = "t_role_dept")
public class RoleDept implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @TableField(value = "dept_id")
    private Long deptId;

}
