package com.aegis.modules.log.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.log.domain.dto.SysLoginLogDTO;
import com.aegis.modules.log.domain.entity.SysLoginLog;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录日志业务层
 *
 * @author xuesong.lei
 * @since 2025/9/7 16:28
 */
public interface SysLoginLogService {

    /**
     * 分页查询登录日志
     *
     * @param dto 查询参数
     * @return 登录日志列表
     */
    PageVO<SysLoginLog> pageList(SysLoginLogDTO dto);

    /**
     * 导出登录日志
     *
     * @param dto      查询参数
     * @param response 响应对象
     */
    void export(SysLoginLogDTO dto, HttpServletResponse response);
}
