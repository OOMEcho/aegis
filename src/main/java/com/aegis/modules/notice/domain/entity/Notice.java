package com.aegis.modules.notice.domain.entity;

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
 * 通知公告表
 *
 * @author xuesong.lei
 * @since 2025-09-16 21:39:03
 * @TableName t_notice
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("通知公告表")
@TableName(value = "t_notice")
public class Notice implements Serializable {

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
     * 通知标题
     */
    @ApiModelProperty("通知标题")
    @TableField(value = "notice_title")
    private String noticeTitle;

    /**
     * 通知类型(1=系统通知,2=公告,3=提醒)
     */
    @ApiModelProperty("通知类型(1=系统通知,2=公告,3=提醒)")
    @TableField(value = "notice_type")
    private String noticeType;

    /**
     * 通知内容
     */
    @ApiModelProperty("通知内容")
    @TableField(value = "notice_content")
    private String noticeContent;

    /**
     * 目标类型(1=全部用户,2=指定用户,3=指定角色,4=指定部门))
     */
    @ApiModelProperty("目标类型(1=全部用户,2=指定用户,3=指定角色,4=指定部门))")
    @TableField(value = "target_type")
    private Integer targetType;

    /**
     * 目标对象ID列表,逗号分隔(根据target_type解释含义)
     */
    @ApiModelProperty("目标对象ID列表,逗号分隔(根据target_type解释含义)")
    @TableField(value = "target_ids")
    private String targetIds;

    /**
     * 通知状态(0=待发布,1=已发布,2=已撤回)
     */
    @ApiModelProperty("通知状态(0=待发布,1=已发布,2=已撤回)")
    @TableField(value = "status")
    private String status;

    /**
     * 计划发布时间,为空则立即发布
     */
    @ApiModelProperty("计划发布时间,为空则立即发布")
    @TableField(value = "publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

}
