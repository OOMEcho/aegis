package com.aegis.modules.log.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 登录日志DTO
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
@Data
@Schema(description = "登录日志DTO")
@EqualsAndHashCode(callSuper = true)
public class SysLoginLogDTO extends PageDTO {

    @Schema(description = "登录地址")
    private String loginLocal;

    @Schema(description = "登录名")
    private String loginUsername;

    @Schema(description = "状态")
    private String loginStatus;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
