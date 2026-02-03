package com.aegis.modules.common.service;

import com.aegis.modules.common.domain.vo.DemoResetVO;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/3 11:57
 * @Description: 演示数据重置服务
 */
public interface DemoResetService {

    /**
     * 获取倒计时信息
     *
     * @return 倒计时VO
     */
    DemoResetVO countdown();

    /**
     * 执行数据重置
     *
     * @return 重置结果信息
     */
    String resetData();
}
