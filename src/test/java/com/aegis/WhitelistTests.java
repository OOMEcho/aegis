package com.aegis;

import com.aegis.config.mp.MPMetaObjectHandler;
import com.aegis.config.mp.MybatisPlusConfig;
import com.aegis.modules.whitelist.domain.entity.Whitelist;
import com.aegis.modules.whitelist.mapper.WhitelistMapper;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/4 13:49
 * @Description: 白名单测试
 */
@MybatisPlusTest
@Rollback(false)// 设置为false可以查看测试数据
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({MybatisPlusConfig.class, MPMetaObjectHandler.class})
public class WhitelistTests {

    @Autowired
    private WhitelistMapper whitelistMapper;

    @Test
    void testWhitelistMapper() {
        Whitelist whitelist = new Whitelist();
        whitelist.setRequestMethod("GET");
        whitelist.setUrlPattern("/email/sendRegisterCode");
        whitelist.setDescription("发送邮箱验证码");
        whitelistMapper.insert(whitelist);
    }
}
