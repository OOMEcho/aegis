package com.aegis.common.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 滑块验证码VO
 *
 * @author xuesong.lei
 * @since 2025/8/31 20:10
 */
@Data
@Schema(description = "滑块验证码VO")
public class CaptchaVO {

    /**
     * 验证码key
     */
    @Schema(description = "验证码key")
    private String captchaKey;

    /**
     * 背景图
     */
    @Schema(description = "背景图")
    private String backgroundImage;

    /**
     * 滑块图
     */
    @Schema(description = "滑块图")
    private String sliderImage;

    /**
     * 滑块Y轴位置
     */
    @Schema(description = "滑块Y轴位置")
    private Integer sliderY;

    public CaptchaVO(String captchaKey, String backgroundImage, String sliderImage, Integer sliderY) {
        this.captchaKey = captchaKey;
        this.backgroundImage = backgroundImage;
        this.sliderImage = sliderImage;
        this.sliderY = sliderY;
    }
}
