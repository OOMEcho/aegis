package com.aegis.modules.notice.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 通知管理端VO
 *
 * @author xuesong.lei
 * @since 2026/2/27 14:07
 */
@Data
@ApiModel("通知管理端VO")
public class NoticeAdminVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("通知标题")
    private String noticeTitle;

    @ApiModelProperty("通知类型(1=系统通知,2=公告,3=提醒)")
    private String noticeType;

    @ApiModelProperty("通知内容")
    private String noticeContent;

    @ApiModelProperty("目标类型(1=全部用户,2=指定用户,3=指定角色,4=指定部门)")
    private Integer targetType;

    @ApiModelProperty("目标对象ID列表")
    private String targetIds;

    @ApiModelProperty("通知状态(0=待发布,1=已发布,2=已撤回)")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("计划发布时间")
    private Date publishTime;
}
