package com.aegis.modules.log.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 操作日志DTO
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
@Data
@Schema(description = "操作日志DTO")
@EqualsAndHashCode(callSuper = true)
public class SysOperateLogDTO extends PageDTO {

    @Schema(description = "模块标题")
    private String moduleTitle;

    @Schema(description = "操作人员")
    private String operateUser;

    @Schema(description = "操作类型")
    private Integer businessType;

    @Schema(description = "状态")
    private String operateStatus;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
