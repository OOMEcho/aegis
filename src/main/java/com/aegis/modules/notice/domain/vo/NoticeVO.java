package com.aegis.modules.notice.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 通知VO
 *
 * @author xuesong.lei
 * @since 2025/9/16 22:06
 */
@Data
@Schema(description = "通知VO")
public class NoticeVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "通知标题")
    private String noticeTitle;

    @Schema(description = "通知类型(1=系统通知,2=公告,3=提醒)")
    private String noticeType;

    @Schema(description = "通知内容")
    private String noticeContent;

    @Schema(description = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @Schema(description = "是否已读(0=未读,1=已读)")
    private Integer readFlag;

    @Schema(description = "阅读时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;
}
