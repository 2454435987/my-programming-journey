package com.eldercare.config;

import com.eldercare.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse handleIllegalArgument(IllegalArgumentException e) {
        return ApiResponse.fail(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        log.error("系统异常", e);
        return ApiResponse.fail(500, "系统繁忙: " + e.getMessage());
    }
}
