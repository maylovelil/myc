package com.myc.comm.utils;

import com.alibaba.fastjson.JSONObject;
import com.myc.comm.Result;
import org.springframework.http.HttpStatus;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/14 18:58
 */
public class ResultUtils {
    public static Result SUCCESS = SUCCESS();
    public static Result ERROR = ERROR();

    private static Result  SUCCESS(){
        return makeResult("操作成功",null,HttpStatus.OK);
    }

    private static Result  ERROR(){
        return makeResult("操作失败",null,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result  SUCCESS(Object data){
        return makeResult("操作失败",data,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result  ERROR(Object data){
        return makeResult("操作成功",data,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result  SUCCESS(String msg,Object data){
        return makeResult(msg,data,HttpStatus.OK);
    }

    public static Result  ERROR(String msg,Object data){
        return makeResult(msg,data,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result  SUCCESS(String msg){
        return makeResult(msg,null,HttpStatus.OK);
    }

    public static Result  ERROR(String msg){
        return makeResult(msg,null,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static Result  SUCCESS(String msg,HttpStatus httpStatus){
        return makeResult(msg,null,httpStatus);
    }

    public static Result  ERROR(String msg,HttpStatus httpStatus){
        return makeResult(msg,null,httpStatus);
    }


    private static Result makeResult(String msg,Object data,HttpStatus httpStatus){
        Result result = new Result();
        result.setStatus(httpStatus.value());
        result.setMessage(msg);
        result.setData(JSONObject.toJSON(data));
        return  result;
    }

}
