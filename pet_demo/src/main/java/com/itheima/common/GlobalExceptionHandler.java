package com.itheima.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //自定义业务异常处理
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e){
        log.error("业务异常:{}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
    //参数校验异常处理
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e){
        String msg=e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error(ResultCdoeEnum.PARAM_ERROR.getCode(), msg);
    }

    //兜底异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("系统异常", e);
        return Result.error(ResultCdoeEnum.FAIL.getCode(), e.getMessage());
    }
}