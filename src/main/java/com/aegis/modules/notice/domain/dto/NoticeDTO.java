package com.aegis.modules.notice.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.ValidGroup;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/16 21:49
 * @Description: 通知DTO
 */
@Data
@ApiModel("通知DTO")
@EqualsAndHashCode(callSuper = true)
public class NoticeDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @ApiModelProperty("通知标题")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "通知标题不能为空")
    private String noticeTitle;

    @ApiModelProperty("通知类型(1=系统通知,2=公告,3=提醒)")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "通知类型不能为空")
    private String noticeType;

    @ApiModelProperty("通知内容")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "通知内容不能为空")
    @Size(max = 100000, groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "通知内容不能超过100000字符")
    private String noticeContent;

    @ApiModelProperty("目标类型(1=全部用户,2=指定用户,3=指定角色,4=指定部门))")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "目标类型不能为空")
    private Integer targetType;

    @ApiModelProperty("目标对象ID列表")
    private List<Long> targetIds;

    @ApiModelProperty("通知状态(0=待发布,1=已发布,2=已撤回)")
    @TableField(value = "status")
    private String status;

    @ApiModelProperty("计划发布时间,为空则立即发布")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
}
