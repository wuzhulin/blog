package com.wuzhulin.handler;

import com.wuzhulin.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//拦截所有controller层的异常 AOP原理
@ControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception ex) {
        ex.printStackTrace();
        return Result.fail(999,"系统异常");
    }
}
