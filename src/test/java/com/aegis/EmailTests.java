package com.aegis;

import com.aegis.utils.EmailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/24 17:23
 * @Description: 邮箱测试
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
