package com.aegis.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 滑块验证码VO
 *
 * @author xuesong.lei
 * @since 2025-08-31
 */
@Data
@ApiModel("滑块验证码VO")
public class CaptchaVO {

    /**
     * 验证码key
     */
    @ApiModelProperty("验证码key")
    private String captchaKey;

    /**
     * 背景图
     */
    @ApiModelProperty("背景图")
    private String backgroundImage;

    /**
     * 滑块图
     */
    @ApiModelProperty("滑块图")
    private String sliderImage;

    /**
     * 滑块Y轴位置
     */
    @ApiModelProperty("滑块Y轴位置")
    private Integer sliderY;

    public CaptchaVO(String captchaKey, String backgroundImage, String sliderImage, Integer sliderY) {
        this.captchaKey = captchaKey;
        this.backgroundImage = backgroundImage;
        this.sliderImage = sliderImage;
        this.sliderY = sliderY;
    }
}
