package com.myc.comm;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/27 16:51
 */
public class ApiResultModel<T> {
    //返回码，0=成功，1=失败
    private String code;
    //返回消息
    private String msg;
    //返回数据
    private T data;

    public ApiResultModel() {
    }

    public ApiResultModel(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResultModel(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功默认返回结果
     *
     * @return
     */
    public static ApiResultModel SUCCESS() {
        return new ApiResultModel("0", "success");
    }

    /**
     * 成功 -- 返回结果
     *
     * @return
     */
    public static ApiResultModel SUCCESS(Object data) {
        return new ApiResultModel("1","success", data);
    }

    public static ApiResultModel SUCCESS(String code, Object data) {
        return new ApiResultModel(code, "success", data);
    }

    /**
     * 失败默认返回结果
     *
     * @return
     */
    public static ApiResultModel ERROR() {
        return new ApiResultModel("0","系统错误，请重试");
    }

    /**
     * 失败时，根据异常类型返回结果
     *
     * @return
     */
    public static ApiResultModel SERVICE_ERROR(Exception e) {
        if (e instanceof ServiceException) {
            return new ApiResultModel("0", e.getMessage());
        } else {
            return ERROR();
        }
    }

    /**
     * 失败默认返回结果
     *
     * @return
     */
    public static ApiResultModel ERROR(String message) {
        return new ApiResultModel("0", message);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



    public static ApiResultModel SUCCESS(String format, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, objects);
        return new ApiResultModel("0", ft.getMessage());
    }

    public static ApiResultModel ERROR(String format, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, objects);
        return new ApiResultModel("0", ft.getMessage());
    }
    
}
