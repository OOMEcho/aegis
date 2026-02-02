package com.aegis.modules.common.service;

import com.aegis.modules.common.domain.vo.AccessTrendVO;
import com.aegis.modules.common.domain.vo.DashboardVO;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/2 15:17
 * @Description: 首页业务层
 */
public interface DashboardService {

    /**
     * 获取统计卡片数据
     *
     * @return 统计数据VO
     */
    DashboardVO getStatistics();

    /**
     * 获取访问趋势数据
     *
     * @param days 天数（7或30）
     * @return 访问趋势列表
     */
    List<AccessTrendVO> getAccessTrend(Integer days);
}
