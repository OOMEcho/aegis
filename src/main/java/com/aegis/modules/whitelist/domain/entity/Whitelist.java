package com.aegis.modules.whitelist.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2025-09-04 13:46:14
 * @Description: 白名单表
 * @TableName t_whitelist
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_whitelist")
public class Whitelist implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 版本号,用于乐观锁
     */
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 请求方法,GET,POST,PUT,DELETE,ALL=不限制
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * URL匹配模式,支持Ant风格,比如/api/user/**
     */
    @TableField(value = "url_pattern")
    private String urlPattern;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 状态(0-正常,1停用)
     */
    @TableField(value = "status")
    private String status;

}
