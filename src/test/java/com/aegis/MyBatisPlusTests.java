package com.aegis;

import com.aegis.common.page.vo.PageVO;
import com.aegis.config.mp.MybatisPlusConfig;
import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.modules.log.mapper.SysOperateLogMapper;
import com.aegis.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/23 14:56
 * @Description: MP测试类
 */
@MybatisPlusTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MybatisPlusConfig.class)
public class MyBatisPlusTests {

    @Autowired
    private SysOperateLogMapper sysOperateLogMapper;

    @Test
    void testSysOperateLogMapper() {
        LambdaQueryWrapper<SysOperateLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysOperateLog::getId, SysOperateLog::getOperateTime,SysOperateLog::getOperateStatus).like(SysOperateLog::getId, 1L);
        PageVO<SysOperateLog> paging = PageUtils.setPage(1, 10, "id", "desc").paging(sysOperateLogMapper, queryWrapper);
        System.out.println(paging);
    }
}
