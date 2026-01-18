package com.aegis.common.constant;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/31 19:57
 * @Description: Redis常量
 */
public class RedisConstants {

    /**
     * 防止重复提交 key 前缀
     */
    public static final String REPEAT_SUBMIT = "repeat_submit:";

    /**
     * 限流 key 前缀
     */
    public static final String RATE_LIMIT = "rate_limit:";

    /**
     * redis中存储的资源key（用于鉴权）
     */
    public static final String RESOURCES = "resources";

    /**
     * redis中存储的白名单key
     */
    public static final String WHITELIST = "whitelist";

    /**
     * 黑名单令牌 key 前缀
     */
    public static final String BLACKLIST_TOKEN = "blacklist_token:";

    /**
     * 用户当前 access_token 的 jti
     */
    public static final String USER_TOKEN_JTI = "user_token_jti:";

    /**
     * 用户当前 refresh_token 的 jti
     */
    public static final String USER_REFRESH_JTI = "user_refresh_jti:";

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

    /**
     * 资源分布式锁 key
     */
    public static final String RESOURCE_LOCK_KEY = "lock:security:resources";

    /**
     * 白名单分布式锁 key
     */
    public static final String WHITELIST_LOCK_KEY = "lock:security:whitelist";
}
