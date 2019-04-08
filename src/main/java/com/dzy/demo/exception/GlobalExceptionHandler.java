package com.dzy.demo.exception;

import com.dzy.demo.result.CodeMsg;
import com.dzy.demo.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
        e.printStackTrace();
        if(e instanceof GlobalException){
            GlobalException ex=(GlobalException)e;
            return Result.error(ex.getCm());
        }
        if(e instanceof BindException){
            BindException ex=(BindException)e;
            List<ObjectError> errors=ex.getAllErrors();
            ObjectError error=errors.get(0);
            String msg=error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else {
            return Result.error(CodeMsg.SEVER_ERROR);
        }
    }
}
