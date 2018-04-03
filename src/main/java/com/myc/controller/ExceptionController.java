package com.myc.controller;

import com.myc.comm.ResponseBean;
import com.myc.comm.Result;
import com.myc.comm.utils.ResultUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/9 13:45
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class})
public class ExceptionController {

    // 捕捉shiro的异常
   // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public @ResponseBody Result handle401(ShiroException e) {
        return  ResultUtils.ERROR("没有操作权限",HttpStatus.UNAUTHORIZED);
    }

    // 捕捉UnauthorizedException
   @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody Result handle401() {
        return ResultUtils.ERROR("没有操作权限",HttpStatus.UNAUTHORIZED);
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ResponseBean globalException(HttpServletRequest request, Throwable ex) {
        return new ResponseBean(getStatus(request).value(), ex.getMessage(), null);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
