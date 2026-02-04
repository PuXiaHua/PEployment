package com.pu.handler;


import com.pu.epojo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice/*@RestControllerAdvice = @ControllerAdvice + @ResponseBody
@ControllerAdvice 让异常处理器“全局生效” 扫描所有Controller让内部的@ExceptionHandler能处理所有Controller的异常 即“全局异常处理” @ResponseBody让异常处理返回 JSON*/
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)/*@ExceptionHandler声明异常处理方法*/
    public Result handleException(Exception e) {
        log.error("出现异常{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler
    public Result handleException(DuplicateKeyException e) {
        log.error(e.getMessage());
        String message = e.getMessage().substring(e.getMessage().indexOf("Duplicate entry"));
        String errMessage = message.split(" ")[2]+"已存在";
        return Result.error(errMessage);
    }
}
