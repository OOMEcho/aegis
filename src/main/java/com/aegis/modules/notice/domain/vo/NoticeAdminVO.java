package com.aegis.modules.notice.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 通知管理端VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@Schema(description = "通知管理端VO")
public class NoticeAdminVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "通知标题")
    private String noticeTitle;

    @Schema(description = "通知类型(1=系统通知,2=公告,3=提醒)")
    private String noticeType;

    @Schema(description = "通知内容")
    private String noticeContent;

    @Schema(description = "目标类型(1=全部用户,2=指定用户,3=指定角色,4=指定部门)")
    private Integer targetType;

    @Schema(description = "目标对象ID列表")
    private String targetIds;

    @Schema(description = "通知状态(0=待发布,1=已发布,2=已撤回)")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "计划发布时间")
    private Date publishTime;
}
