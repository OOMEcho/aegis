package com.aegis.common.captcha;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/31 20:10
 * @Description: 验证码VO
 */
@Data
public class CaptchaVO {

    private String captchaKey;
    private String backgroundImage;
    private String sliderImage;
    private Integer sliderY;

    public CaptchaVO(String captchaKey, String backgroundImage, String sliderImage, Integer sliderY) {
        this.captchaKey = captchaKey;
        this.backgroundImage = backgroundImage;
        this.sliderImage = sliderImage;
        this.sliderY = sliderY;
    }
}
