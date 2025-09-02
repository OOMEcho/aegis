package com.aegis.modules.user.controller;

import com.aegis.common.captcha.CaptchaDTO;
import com.aegis.common.captcha.CaptchaVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.utils.CaptchaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class SlideCaptchaController {

    private final CaptchaUtils captchaUtils;

    /**
     * 生成验证码
     */
    @GetMapping("/generate")
    public CaptchaVO generateCaptcha() {
        return captchaUtils.generateCaptcha();
    }

    /**
     * 验证滑动位置
     */
    @PostMapping("/verify")
    public String verifyCaptcha(@RequestBody CaptchaDTO captchaDTO) {
        boolean isValid = captchaUtils.verifyCaptcha(captchaDTO.getCaptchaKey(), captchaDTO.getSlideX());
        if (isValid) {
            return "验证成功";
        } else {
           throw new BusinessException("验证失败");
        }
    }
}
