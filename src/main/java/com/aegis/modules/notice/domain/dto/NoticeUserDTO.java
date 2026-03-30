package com.aegis.modules.notice.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知用户DTO
 *
 * @author xuesong.lei
 * @since 2025/9/16 22:07
 */
@Data
@Schema(description = "通知用户DTO")
@EqualsAndHashCode(callSuper = true)
public class NoticeUserDTO extends PageDTO {

    @Schema(description = "通知标题")
    private String noticeTitle;

    @Schema(description = "通知类型(1=系统通知,2=公告,3=提醒)")
    private String noticeType;

    @Schema(description = "是否已读(0=未读,1=已读)")
    private Integer readFlag;

}
