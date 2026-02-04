package com.pu.exception;

//专门处理业务异常的类
public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}