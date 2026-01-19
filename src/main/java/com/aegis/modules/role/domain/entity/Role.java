package com.aegis.modules.role.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:48:14
 * @Description: 角色信息表
 * @TableName t_role
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("角色信息表")
@TableName(value = "t_role")
public class Role implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @ApiModelProperty("逻辑删除标记(0=正常,1=删除)")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 版本号,用于乐观锁
     */
    @ApiModelProperty("版本号,用于乐观锁")
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    @TableField(value = "role_code")
    private String roleCode;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限)
     */
    @ApiModelProperty("数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限)")
    @TableField(value = "data_scope")
    private String dataScope;

    /**
     * 部门树选择项是否关联显示
     */
    @ApiModelProperty("部门树选择项是否关联显示")
    @TableField(value = "dept_check_strictly")
    private Integer deptCheckStrictly;

    /**
     * 角色状态(0-正常,1-停用)
     */
    @ApiModelProperty("角色状态(0-正常,1-停用)")
    @TableField(value = "status")
    private String status;

}
