package com.aegis.modules.log.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.modules.log.domain.dto.SysOperateLogDTO;
import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.modules.log.service.SysOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 系统操作日志接口
 *
 * @author xuesong.lei
 * @since 2025/8/23 14:48
 */
@RestController
@Api(tags = "系统操作日志接口")
@RequiredArgsConstructor
@RequestMapping("/operateLog")
public class SysOperateLogController {

    private final SysOperateLogService sysOperateLogService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<SysOperateLog> pageList(SysOperateLogDTO dto) {
        return sysOperateLogService.pageList(dto);
    }

    @ApiOperation("导出操作日志")
    @GetMapping("/export")
    @OperationLog(moduleTitle = "导出操作日志", businessType = BusinessType.EXPORT)
    public void export(SysOperateLogDTO dto, HttpServletResponse response) {
        sysOperateLogService.export(dto, response);
    }
}
