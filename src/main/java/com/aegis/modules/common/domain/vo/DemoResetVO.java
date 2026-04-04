package com.aegis.modules.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 演示数据重置VO
 *
 * @author xuesong.lei
 * @since 2026-02-03
 */
@Data
@ApiModel("演示数据重置VO")
public class DemoResetVO {

    @ApiModelProperty("是否启用演示数据重置功能")
    private boolean enabled;

    @ApiModelProperty("距离下次重置的秒数")
    private long secondsToNextReset;

    @ApiModelProperty("下次重置时间")
    private String nextResetTime;

    @ApiModelProperty("上次重置时间")
    private String lastResetTime;
}
