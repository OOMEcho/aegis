package com.aegis.modules.log.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 登录日志DTO
 *
 * @author xuesong.lei
 * @since 2025/9/7 21:30
 */
@Data
@ApiModel("登录日志DTO")
@EqualsAndHashCode(callSuper = true)
public class SysLoginLogDTO extends PageDTO {

    @ApiModelProperty("登录地址")
    private String loginLocal;

    @ApiModelProperty("登录名")
    private String loginUsername;

    @ApiModelProperty("状态")
    private String loginStatus;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
