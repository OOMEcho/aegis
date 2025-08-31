package com.aegis.common.captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/captcha")
public class SlideCaptchaController {

    @Autowired
    private SlideCaptchaService slideCaptchaService;

    /**
     * 生成验证码
     */
    @GetMapping("/generate")
    public Map<String, Object> generateCaptcha() {
        Map<String, Object> result = new HashMap<>();
        try {
            CaptchaVO captcha = slideCaptchaService.generateCaptcha();
            result.put("success", true);
            result.put("data", captcha);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "生成验证码失败");
        }
        return result;
    }

    /**
     * 验证滑动位置
     */
    @PostMapping("/verify")
    public Map<String, Object> verifyCaptcha(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            String captchaKey = (String) params.get("captchaKey");
            Integer slideX = (Integer) params.get("slideX");

            boolean isValid = slideCaptchaService.verifyCaptcha(captchaKey, slideX);
            result.put("success", isValid);
            result.put("message", isValid ? "验证通过" : "验证失败");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "验证过程发生错误");
        }
        return result;
    }
}
