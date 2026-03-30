package com.aegis.modules.notice.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 通知DTO
 *
 * @author xuesong.lei
 * @since 2025/9/16 21:49
 */
@Data
@Schema(description = "通知DTO")
@EqualsAndHashCode(callSuper = true)
public class NoticeDTO extends PageDTO {

    @Schema(description = "主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @Schema(description = "通知标题")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "通知标题不能为空")
    private String noticeTitle;

    @Schema(description = "通知类型(1=系统通知,2=公告,3=提醒)")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "通知类型不能为空")
    private String noticeType;

    @Schema(description = "通知内容")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "通知内容不能为空")
    @Size(max = 100000, groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "通知内容不能超过100000字符")
    private String noticeContent;

    @Schema(description = "目标类型(1=全部用户,2=指定用户,3=指定角色,4=指定部门))")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "目标类型不能为空")
    private Integer targetType;

    @Schema(description = "目标对象ID列表")
    private List<Long> targetIds;

    @Schema(description = "通知状态(0=待发布,1=已发布,2=已撤回)")
    private String status;

    @Schema(description = "计划发布时间,为空则立即发布")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
}
