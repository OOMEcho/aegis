package com.aegis.modules.log.service.impl;

import cn.idev.excel.FastExcel;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.log.domain.dto.SysLoginLogDTO;
import com.aegis.modules.log.domain.entity.SysLoginLog;
import com.aegis.modules.log.mapper.SysLoginLogMapper;
import com.aegis.modules.log.service.SysLoginLogService;
import com.aegis.utils.PageUtils;
import com.aegis.utils.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/7 16:28
 * @Description: 登录业务实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl implements SysLoginLogService {

    private final SysLoginLogMapper sysLoginLogMapper;

    @Override
    public PageVO<SysLoginLog> pageList(SysLoginLogDTO dto) {
        LambdaQueryWrapper<SysLoginLog> queryWrapper = getQueryWrapper(dto);
        return PageUtils.of(dto).paging(sysLoginLogMapper, queryWrapper);
    }

    @Override
    public void export(SysLoginLogDTO dto, HttpServletResponse response) {
        ResponseUtils.setExcelResponse(response);
        LambdaQueryWrapper<SysLoginLog> queryWrapper = getQueryWrapper(dto);
        try {
            FastExcel.write(response.getOutputStream(), SysLoginLog.class).sheet("登录日志").doWrite(sysLoginLogMapper.selectList(queryWrapper));
        } catch (IOException e) {
            log.error("登录日志导出失败", e);
            throw new RuntimeException("登录日志导出失败", e);
        }
    }

    private LambdaQueryWrapper<SysLoginLog> getQueryWrapper(SysLoginLogDTO dto) {
        LambdaQueryWrapper<SysLoginLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getLoginLocal()), SysLoginLog::getLoginLocal, dto.getLoginLocal())
                .like(StringUtils.isNotBlank(dto.getLoginUsername()), SysLoginLog::getLoginUsername, dto.getLoginUsername())
                .eq(StringUtils.isNotBlank(dto.getLoginStatus()), SysLoginLog::getLoginStatus, dto.getLoginStatus())
                .between(ObjectUtils.isNotEmpty(dto.getBeginTime()) && ObjectUtils.isNotEmpty(dto.getEndTime()), SysLoginLog::getLoginTime, dto.getBeginTime(), dto.getEndTime())
                .orderByDesc(SysLoginLog::getLoginTime);
        return queryWrapper;
    }
}
