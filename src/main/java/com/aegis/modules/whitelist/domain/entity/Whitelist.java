package com.aegis.modules.whitelist.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 白名单表
 *
 * @author xuesong.lei
 * @since 2025-09-04
 * @TableName t_whitelist
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "白名单表")
@TableName(value = "t_whitelist")
public class Whitelist implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @Schema(description = "逻辑删除标记(0=正常,1=删除)")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 版本号,用于乐观锁
     */
    @Schema(description = "版本号,用于乐观锁")
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 请求方法,GET,POST,PUT,DELETE,ALL=不限制
     */
    @Schema(description = "请求方法,GET,POST,PUT,DELETE,ALL=不限制")
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * URL匹配模式,支持Ant风格,比如/api/user/**
     */
    @Schema(description = "URL匹配模式,支持Ant风格,比如/api/user/**")
    @TableField(value = "request_uri")
    private String requestUri;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @TableField(value = "description")
    private String description;

    /**
     * 状态(0-正常,1停用)
     */
    @Schema(description = "状态(0-正常,1停用)")
    @TableField(value = "status")
    private String status;

}
