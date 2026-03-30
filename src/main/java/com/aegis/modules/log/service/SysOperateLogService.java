package com.aegis.modules.log.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.log.domain.dto.SysOperateLogDTO;
import com.aegis.modules.log.domain.entity.SysOperateLog;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统操作日志业务层
 *
 * @author xuesong.lei
 * @since 2025/8/23 14:50
 */
public interface SysOperateLogService {

    /**
     * 查询操作日志列表
     *
     * @param dto 查询参数
     * @return 操作日志列表
     */
    PageVO<SysOperateLog> pageList(SysOperateLogDTO dto);

    /**
     * 导出操作日志
     *
     * @param dto      查询参数
     * @param response 响应对象
     */
    void export(SysOperateLogDTO dto, HttpServletResponse response);
}
