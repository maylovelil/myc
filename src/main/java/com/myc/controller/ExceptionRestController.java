package com.myc.controller;

import com.myc.comm.Result;
import com.myc.comm.utils.ResultUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/9 13:45
 */
@RestControllerAdvice
public class ExceptionRestController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionRestController.class);
    // 捕捉shiro的异常
   @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public @ResponseBody Result handle401(ShiroException e) {
        return  ResultUtils.ERROR("没有操作权限",HttpStatus.UNAUTHORIZED);
    }

    // 捕捉UnauthorizedException
   @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody Result handle401(HttpServletResponse response) {
       return  ResultUtils.ERROR("没有操作权限",HttpStatus.UNAUTHORIZED);
   }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Result globalException(HttpServletRequest request, Throwable ex, HttpServletResponse response) {
        logger.info("系统异常：{}",ex.getMessage());
        return  ResultUtils.ERROR("系统异常",HttpStatus.UNAUTHORIZED);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
