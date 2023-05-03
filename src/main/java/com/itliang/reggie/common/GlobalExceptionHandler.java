package com.itliang.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * 全局异常处理器
 */

@RestControllerAdvice(annotations = {RestController.class, Controller.class})//拦截
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 进行异常处理
     *
     * @param es
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException es) {
        log.error(es.getMessage());
        //根据报错信息得知：包含这条信息 则用户名重复
         if (es.getMessage().contains("Duplicate entry")) {
            String[] split = es.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 进行异常处理
     *
     * @param ce
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ce) {
        log.error(ce.getMessage());
        return R.error(ce.getMessage());
    }
}
