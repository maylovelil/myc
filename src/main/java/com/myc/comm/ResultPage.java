package com.myc.comm;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/14 18:51
 */
public class ResultPage<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="处理状态", required=true)
    private int status = HttpStatus.OK.value();

    @ApiModelProperty("消息提示")
    private String message;

    private Long total;
    private List<T> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

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

    public static ResultPage sendOk(PageInfo pageInfo){
        ResultPage resultPage = new ResultPage();
        resultPage.setTotal(pageInfo.getTotal());
        resultPage.setRows(pageInfo.getList());
        return resultPage;
    }
}
