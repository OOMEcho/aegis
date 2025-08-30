package com.aegis.modules.role.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import java.io.Serializable;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:48:29
 * @Description: 角色和部门关联表
 * @TableName t_role_dept
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_role_dept")
public class RoleDept implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 部门ID
     */
    @TableField(value = "dept_id")
    private Long deptId;

}
