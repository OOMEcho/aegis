package com.aegis.modules.resource.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源(URL)与权限映射表
 *
 * @author xuesong.lei
 * @since 2026-01-11
 * @TableName t_resource
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("资源(URL)与权限映射表")
@TableName(value = "t_resource")
public class Resource implements Serializable {

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
     * 请求方法(GET/POST/PUT/DELETE/ALL)
     */
    @ApiModelProperty("请求方法(GET/POST/PUT/DELETE/ALL)")
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * URI匹配模式(Ant风格)
     */
    @ApiModelProperty("URI匹配模式(Ant风格)")
    @TableField(value = "request_uri")
    private String requestUri;

    /**
     * 关联权限编码
     */
    @ApiModelProperty("关联权限编码")
    @TableField(value = "perm_code")
    private String permCode;

    /**
     * 状态(0-正常,1-停用)
     */
    @ApiModelProperty("状态(0-正常,1-停用)")
    @TableField(value = "status")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @TableField(value = "remark")
    private String remark;

}
