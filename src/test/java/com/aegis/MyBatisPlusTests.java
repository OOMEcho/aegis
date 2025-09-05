package com.aegis;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.config.mp.MPMetaObjectHandler;
import com.aegis.config.mp.MybatisPlusConfig;
import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.modules.log.mapper.SysOperateLogMapper;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.mapper.UserMapper;
import com.aegis.modules.whitelist.domain.entity.Whitelist;
import com.aegis.modules.whitelist.mapper.WhitelistMapper;
import com.aegis.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/23 14:56
 * @Description: MP测试类
 */
@MybatisPlusTest
@Rollback(false)// 设置为false可以查看测试数据
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({MybatisPlusConfig.class, MPMetaObjectHandler.class})
public class MyBatisPlusTests {

    @Autowired
    private SysOperateLogMapper sysOperateLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WhitelistMapper whitelistMapper;

    @Test
    void testSysOperateLogMapper() {
        LambdaQueryWrapper<SysOperateLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysOperateLog::getId, SysOperateLog::getOperateTime, SysOperateLog::getOperateStatus).like(SysOperateLog::getId, 1L);
        PageVO<SysOperateLog> paging = PageUtils.setPage(1, 10, "id", "desc").paging(sysOperateLogMapper, queryWrapper);
        System.out.println(paging);
    }

    @Test
    void testWhitelistMapper() {
        Whitelist whitelist = new Whitelist();
        whitelist.setRequestMethod("GET");
        whitelist.setRequestUri("/email/sendRegisterCode");
        whitelist.setDescription("发送邮箱验证码");
        whitelistMapper.insert(whitelist);
    }

    @Test
    void testUserMapper() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user.setNickname("管理员");
        user.setEmail("228389787@qq.com");
        user.setPhone("18888888888");
        userMapper.insert(user);
    }
}
