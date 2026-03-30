package com.aegis.modules.role.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import java.io.Serializable;

/**
 * 角色和部门关联表
 *
 * @author xuesong.lei
 * @since 2025-08-30 10:48:29
 * @TableName t_role_dept
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "角色和部门关联表")
@TableName(value = "t_role_dept")
public class RoleDept implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    @TableField(value = "dept_id")
    private Long deptId;

}
