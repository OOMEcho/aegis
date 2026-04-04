package com.aegis.common.exception;

import com.aegis.common.result.Result;
import com.aegis.common.result.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author xuesong.lei
 * @since 2025-08-21
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBusinessException(HttpServletRequest request, BusinessException e) {
        logError(request, e);
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("参数[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        logError(request, e);
        return Result.error(ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(v -> String.format("参数[%s] %s", v.getPropertyPath(), v.getMessage()))
                .collect(Collectors.joining("; "));
        logError(request, e);
        return Result.error(ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBindException(HttpServletRequest request, BindException e) {
        String message = e.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return String.format("参数[%s]的值[%s]格式不正确",
                                fieldError.getField(),
                                fieldError.getRejectedValue());
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining("; "));

        logError(request, e);
        return Result.error(ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理其他校验异常
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(HttpServletRequest request, ValidationException e) {
        logError(request, e);
        return Result.error(ResultCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(HttpServletRequest request, Exception e) {
        logError(request, e);
        return Result.error(ResultCodeEnum.ERROR);
    }

    private void logError(HttpServletRequest request, Exception e) {
        log.error("[{}] {} [ex] {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage(),
                e);
    }
}
