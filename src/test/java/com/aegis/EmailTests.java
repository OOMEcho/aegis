package com.aegis;

import com.aegis.utils.EmailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 邮箱测试
 *
 * @author xuesong.lei
 * @since 2025-09-23
 */
@SpringBootTest
@ActiveProfiles("dev")
public class EmailTests {

    @Autowired
    private EmailUtils emailUtils;

    @Test
    public void sendSimpleEmail() {
        emailUtils.sendVerificationCode("228389787@qq.com", "123456",5);
    }
}
