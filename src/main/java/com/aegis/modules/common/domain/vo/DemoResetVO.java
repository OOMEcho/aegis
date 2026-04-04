package com.aegis.modules.common.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 演示数据重置VO
 *
 * @author xuesong.lei
 * @since 2026-02-03
 */
@Data
@Schema(description ="演示数据重置VO")
public class DemoResetVO {

    @Schema(description ="是否启用演示数据重置功能")
    private boolean enabled;

    @Schema(description ="距离下次重置的秒数")
    private long secondsToNextReset;

    @Schema(description ="下次重置时间")
    private String nextResetTime;

    @Schema(description ="上次重置时间")
    private String lastResetTime;
}
