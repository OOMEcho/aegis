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
     * Access token session key 前缀
     */
    public static final String SESSION = "session:";

    /**
     * 用户当前 session 反向索引（用于单设备登录控制）
     */
    public static final String USER_SESSION = "user_session:";

    /**
     * Refresh token key 前缀
     */
    public static final String REFRESH = "refresh:";

    /**
     * 用户当前 refresh token 反向索引
     */
    public static final String USER_REFRESH = "user_refresh:";

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

    /**
     * 演示数据重置分布式锁 key
     */
    public static final String DEMO_RESET_LOCK_KEY = "lock:demo:reset";

    /**
     * 演示数据上次重置时间 key
     */
    public static final String DEMO_LAST_RESET_TIME = "demo:last_reset_time";
}
