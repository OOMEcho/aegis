package com.aegis.common.captcha;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/31 20:10
 * @Description: 滑块验证码VO
 */
@Data
public class CaptchaVO {

    /**
     * 验证码key
     */
    private String captchaKey;

    /**
     * 背景图
     */
    private String backgroundImage;

    /**
     * 滑块图
     */
    private String sliderImage;

    /**
     * 滑块Y轴位置
     */
    private Integer sliderY;

    public CaptchaVO(String captchaKey, String backgroundImage, String sliderImage, Integer sliderY) {
        this.captchaKey = captchaKey;
        this.backgroundImage = backgroundImage;
        this.sliderImage = sliderImage;
        this.sliderY = sliderY;
    }
}
