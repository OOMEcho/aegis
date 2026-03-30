package com.aegis.modules.common.controller;

import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.modules.common.domain.vo.DemoResetVO;
import com.aegis.modules.common.service.DemoResetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示数据重置接口
 *
 * @author xuesong.lei
 * @since 2026/2/3 11:57
 */
@Api(tags = "演示数据重置接口")
@RestController
@RequestMapping("/demo/reset")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "demo.reset.enabled", havingValue = "true")
public class DemoResetController {

    private final DemoResetService demoResetService;

    /**
     * 获取重置倒计时信息
     * 此接口无需登录即可访问，用于前端展示
     */
    @ApiOperation("获取重置倒计时信息")
    @GetMapping("/countdown")
    public DemoResetVO countdown() {
        return demoResetService.countdown();
    }

    /**
     * 手动触发数据重置
     * 仅管理员可执行此操作
     */
    @ApiOperation("手动触发数据重置")
    @PostMapping("/trigger")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "演示数据重置", businessType = BusinessType.OTHER)
    public String trigger() {
        return demoResetService.resetData();
    }
}
