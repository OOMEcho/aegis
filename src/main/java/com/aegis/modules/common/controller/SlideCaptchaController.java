package com.aegis.modules.common.controller;

import com.aegis.common.domain.dto.CaptchaDTO;
import com.aegis.common.domain.vo.CaptchaVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.utils.CaptchaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/3 15:14
 * @Description: 滑块验证码接口
 */
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
