package com.aegis.common.result;

import lombok.Getter;

/**
 * 状态码枚举
 *
 * @author xuesong.lei
 * @since 2025-08-21
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),
    LOGIN_EXPIRE(401, "登录过期"),
    ACCOUNT_ERROR(402, "账户或凭证错误"),
    LACK_OF_AUTHORITY(403, "当前没有权限访问"),
    NOT_FOUND(404, "请求资源不存在"),
    USER_IS_DISABLE(405, "账户已被禁用"),
    NOT_LOGGED_IN(406, "账户未登录,请先登录"),
    ERROR(500, "系统异常,请稍后重试"),
    ;

    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
