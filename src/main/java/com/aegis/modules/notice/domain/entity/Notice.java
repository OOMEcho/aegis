package com.aegis.modules.notice.domain.entity;

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
 * @Date: 2025-08-30 10:47:49
 * @Description: 通知公告表
 * @TableName t_notice
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_notice")
public class Notice implements Serializable {

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
     * 公告标题
     */
    @TableField(value = "notice_title")
    private String noticeTitle;

    /**
     * 公告类型(1-通知,2-公告)
     */
    @TableField(value = "notice_type")
    private String noticeType;

    /**
     * 公告内容
     */
    @TableField(value = "notice_content")
    private String noticeContent;

    /**
     * 目标类型(1=全部用户,2=指定用户)
     */
    @TableField(value = "target_type")
    private Integer targetType;

    /**
     * 公告状态(0-正常,1-关闭)
     */
    @TableField(value = "status")
    private String status;

    /**
     * 发布时间
     */
    @TableField(value = "publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /**
     * 过期时间,可为空
     */
    @TableField(value = "expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

}
