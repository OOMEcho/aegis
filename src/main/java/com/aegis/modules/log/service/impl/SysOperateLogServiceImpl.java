package com.aegis.modules.log.service.impl;

import com.aegis.modules.log.mapper.SysOperateLogMapper;
import com.aegis.modules.log.service.SysOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/23 14:50
 * @Description: 系统操作日志业务实现层
 */
@Service
@RequiredArgsConstructor
public class SysOperateLogServiceImpl implements SysOperateLogService {

    private final SysOperateLogMapper sysOperateLogMapper;

}
