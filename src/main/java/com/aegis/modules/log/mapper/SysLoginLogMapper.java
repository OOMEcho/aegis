package com.aegis.modules.log.mapper;

import com.aegis.modules.common.domain.vo.AccessTrendVO;
import com.aegis.modules.log.domain.entity.SysLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:48:46
 * @Description: 针对表【t_sys_login_log(登录日志表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.log.domain.entity.SysLoginLog
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    /**
     * 按天统计访问量
     *
     * @param days 天数
     * @return 访问趋势列表
     */
    List<AccessTrendVO> selectAccessTrend(@Param("days") Integer days);
}




