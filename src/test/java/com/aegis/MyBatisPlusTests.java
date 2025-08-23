package com.aegis;

import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.modules.log.mapper.SysOperateLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/23 14:56
 * @Description: MP测试类
 */
@MybatisPlusTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MyBatisPlusTests {

    @Autowired
    private SysOperateLogMapper sysOperateLogMapper;

    @Test
    void testSysOperateLogMapper() {
        LambdaQueryWrapper<SysOperateLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SysOperateLog::getId, 1L);
        List<SysOperateLog> sysOperateLogs = sysOperateLogMapper.selectList(queryWrapper);
        System.out.println(sysOperateLogs);

        LambdaUpdateWrapper<SysOperateLog> updateWrapper = new LambdaUpdateWrapper<>();
    }
}
