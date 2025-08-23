package com.aegis.modules.log.controller;

import com.aegis.modules.log.service.SysOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/23 14:48
 * @Description: 系统操作日志接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/operateLog")
public class SysOperateLogController {

    private final SysOperateLogService sysOperateLogService;
}
