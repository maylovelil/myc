package com.myc.controller;

import com.myc.comm.Result;
import com.myc.comm.utils.ResultUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/9 13:45
 */
@ControllerAdvice
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    // 捕捉shiro的异常
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public @ResponseBody
    Result handle401(ShiroException e) {
        logger.info("系统异常：{}", e);
        return ResultUtils.ERROR("没有操作权限", HttpStatus.UNAUTHORIZED);
    }

    // 捕捉UnauthorizedException
    //@ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public void handle401(HttpServletResponse response, UnauthorizedException e) {
        logger.info("系统异常：{}", e);
        try {
            PrintWriter out = null;
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            out.print("<script language=\"javascript\">alert('没有权限');</script>");
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String globalException(HttpServletRequest request, Throwable ex, HttpServletResponse response) {
        logger.info("系统异常：{}", ex);
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            out.print("<script language=\"javascript\">alert('系统异常');</script>");
            return "user/login";
        } catch (Exception e) {
            logger.info("系统异常：{}", ex);
        }
        return "user/login";
        //return new ResponseBean(getStatus(request).value(), ex.getMessage(), null);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
