package com.aegis.common.constant;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/31 19:57
 * @Description: Redis常量
 */
public class RedisConstants {

    /**
     * redis中存储的菜单key
     */
    public static final String MENUS = "menus:";

    /**
     * redis中存储的白名单key
     */
    public static final String WHITELIST = "whitelist";

    /**
     * 刷新令牌 key 前缀
     */
    public static final String REFRESH_TOKEN = "refresh_token:";

    /**
     * 黑名单令牌 key 前缀
     */
    public static final String BLACKLIST_TOKEN = "blacklist_token:";

    /**
     * 滑块验证码 key
     */
    public static final String SLIDER_CAPTCHA_KEY = "captcha:";

    /**
     * 短信登录 key
     */
    public static final String SMS_LOGIN = "smsLogin:";

    /**
     * 短信登录错误次数 key
     */
    public static final String SMS_LOGIN_ERROR = "smsLoginError:";

    /**
     * 短信发送频率 key
     */
    public static final String SMS_SEND_FREQUENCY = "smsSendFrequency:";

    /**
     * 短信每日发送上限 key
     */
    public static final String SMS_DAILY_LIMIT = "smsDailyLimit:";

    /**
     * 邮箱登录 key
     */
    public static final String EMAIL_LOGIN = "emailLogin:";

    /**
     * 邮箱登录错误次数 key
     */
    public static final String EMAIL_LOGIN_ERROR = "emailLoginError:";

    /**
     * 邮箱发送频率 key
     */
    public static final String EMAIL_SEND_FREQUENCY = "emailSendFrequency:";

    /**
     * 邮箱每日发送上限 key
     */
    public static final String EMAIL_DAILY_LIMIT = "emailDailyLimit:";
}
