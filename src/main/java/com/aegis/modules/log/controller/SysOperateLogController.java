package com.aegis.modules.log.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.modules.log.domain.dto.SysOperateLogDTO;
import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.modules.log.service.SysOperateLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统操作日志接口
 *
 * @author xuesong.lei
 * @since 2025-08-23
 */
@RestController
@Tag(name = "系统操作日志接口")
@RequiredArgsConstructor
@RequestMapping("/operateLog")
public class SysOperateLogController {

    private final SysOperateLogService sysOperateLogService;

    @Operation(summary = "分页列表")
    @GetMapping("/pageList")
    public PageVO<SysOperateLog> pageList(SysOperateLogDTO dto) {
        return sysOperateLogService.pageList(dto);
    }

    @Operation(summary = "导出操作日志")
    @GetMapping("/export")
    @OperationLog(moduleTitle = "导出操作日志", businessType = BusinessType.EXPORT)
    public void export(SysOperateLogDTO dto, HttpServletResponse response) {
        sysOperateLogService.export(dto, response);
    }
}
