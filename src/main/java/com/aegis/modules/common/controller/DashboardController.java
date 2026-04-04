package com.aegis.modules.common.controller;

import com.aegis.modules.common.domain.vo.AccessTrendVO;
import com.aegis.modules.common.domain.vo.DashboardVO;
import com.aegis.modules.common.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页接口
 *
 * @author xuesong.lei
 * @since 2026-02-02
 */
@Tag(name = "首页接口")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "获取统计卡片数据")
    @GetMapping("/statistics")
    public DashboardVO getStatistics() {
        return dashboardService.getStatistics();
    }

    @Operation(summary = "获取访问趋势数据")
    @GetMapping("/accessTrend")
    public List<AccessTrendVO> getAccessTrend(@RequestParam(defaultValue = "7") Integer days) {
        return dashboardService.getAccessTrend(days);
    }
}
