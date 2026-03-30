package com.aegis.modules.notice.domain.entity;

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
 * 通知接收记录表
 *
 * @author xuesong.lei
 * @since 2025-09-16 21:39:42
 * @TableName t_notice_user
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "通知接收记录表")
@TableName(value = "t_notice_user")
public class NoticeUser implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 通知ID
     */
    @Schema(description = "通知ID")
    @TableField(value = "notice_id")
    private Long noticeId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 是否已读(0=未读,1=已读)
     */
    @Schema(description = "是否已读(0=未读,1=已读)")
    @TableField(value = "read_flag")
    private Integer readFlag;

    /**
     * 阅读时间
     */
    @Schema(description = "阅读时间")
    @TableField(value = "read_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

}
