package com.aegis.modules.log.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 操作日志DTO
 *
 * @author xuesong.lei
 * @since 2025/9/7 17:52
 */
@Data
@ApiModel("操作日志DTO")
@EqualsAndHashCode(callSuper = true)
public class SysOperateLogDTO extends PageDTO {

    @ApiModelProperty("模块标题")
    private String moduleTitle;

    @ApiModelProperty("操作人员")
    private String operateUser;

    @ApiModelProperty("操作类型")
    private Integer businessType;

    @ApiModelProperty("状态")
    private String operateStatus;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
