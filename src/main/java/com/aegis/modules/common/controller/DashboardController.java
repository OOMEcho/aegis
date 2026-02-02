package com.aegis.modules.common.controller;

import com.aegis.modules.common.domain.vo.AccessTrendVO;
import com.aegis.modules.common.domain.vo.DashboardVO;
import com.aegis.modules.common.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/2 15:16
 * @Description: 首页接口
 */
@Api(tags = "首页接口")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @ApiOperation("获取统计卡片数据")
    @GetMapping("/statistics")
    public DashboardVO getStatistics() {
        return dashboardService.getStatistics();
    }

    @ApiOperation("获取访问趋势数据")
    @GetMapping("/accessTrend")
    public List<AccessTrendVO> getAccessTrend(@RequestParam(defaultValue = "7") Integer days) {
        return dashboardService.getAccessTrend(days);
    }
}
