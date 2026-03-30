package com.aegis.modules.common.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Dashboard统计VO
 *
 * @author xuesong.lei
 * @since 2026/2/2
 */
@Data
@Schema(description = "Dashboard统计VO")
public class DashboardVO {

    @Schema(description = "用户总数")
    private Long userCount;

    @Schema(description = "用户近7日增长百分比")
    private String userGrowthRate;

    @Schema(description = "角色总数")
    private Long roleCount;

    @Schema(description = "角色近7日增长百分比")
    private String roleGrowthRate;

    @Schema(description = "权限总数")
    private Long permissionCount;

    @Schema(description = "权限近7日增长百分比")
    private String permissionGrowthRate;

    @Schema(description = "通知总数")
    private Long noticeCount;

    @Schema(description = "通知近7日增长百分比")
    private String noticeGrowthRate;
}
