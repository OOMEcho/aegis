package com.aegis.modules.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import java.io.Serializable;

/**
 * 用户和角色关联表
 *
 * @author xuesong.lei
 * @since 2025-08-30 10:49:13
 * @TableName t_user_role
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户和角色关联表")
@TableName(value = "t_user_role")
public class UserRole implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @TableField(value = "role_id")
    private Long roleId;

}
