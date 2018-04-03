package com.myc.comm;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/14 18:51
 */
public class Result<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="处理状态", required=true)
    private int status = HttpStatus.OK.value();

    @ApiModelProperty("消息提示")
    private String message;

    @ApiModelProperty(value="数据", required=true)
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
