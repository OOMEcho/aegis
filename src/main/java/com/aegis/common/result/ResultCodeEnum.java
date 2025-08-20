package com.aegis.common.result;

/**
 * @Author: xuesong.lei
 * @Date: 2022/11/15 11:32
 * @Description: 状态码枚举
 */
public enum ResultCodeEnum implements ResultCodeInterface {

    // 成功
    SUCCESS(200, "成功"),
    NOT_LOGGED_IN(401, "用户未登录或登录过期"),
    // 当前没有权限访问
    LACK_OF_AUTHORITY(403, "当前没有权限访问"),
    // 出错
    ERROR(500, "系统异常,请稍后重试"),
    ;

    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
