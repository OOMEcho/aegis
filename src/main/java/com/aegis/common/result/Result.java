package com.aegis.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 13:08
 * @Description: 统一返回格式
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 6738387175874422264L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    private Result(ResultCodeEnum resultCode, T data) {
        this.timestamp = System.currentTimeMillis();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    private Result(Integer code, String message, T data) {
        this.timestamp = System.currentTimeMillis();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCodeEnum.SUCCESS, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCodeEnum.SUCCESS, data);
    }

    public static <T> Result<T> success(ResultCodeEnum resultCodeEnum, T data) {
        return new Result<>(resultCodeEnum, data);
    }

    public static <T> Result<T> failure() {
        return new Result<>(ResultCodeEnum.ERROR, null);
    }

    public static <T> Result<T> failure(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum, null);
    }

    public static <T> Result<T> failure(ResultCodeEnum resultCodeEnum, T data) {
        return new Result<>(resultCodeEnum, data);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(ResultCodeEnum resultCode) {
        return new Result<>(resultCode, null);
    }
}
