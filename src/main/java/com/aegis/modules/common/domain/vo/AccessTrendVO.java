package com.aegis.modules.common.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 访问趋势VO
 *
 * @author xuesong.lei
 * @since 2026-02-02
 */
@Data
@Schema(description = "访问趋势VO")
public class AccessTrendVO {

    @Schema(description = "日期")
    private String date;

    @Schema(description = "访问量")
    private Long count;
}
