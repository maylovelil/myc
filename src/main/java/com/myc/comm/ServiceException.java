package com.myc.comm;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/27 17:02
 */
public class ServiceException extends RuntimeException {
    private String code;

    public String getCode() {
        return code;
    }

    private static final long serialVersionUID = 1L;

    public ServiceException(Throwable e) {
        super(e);
    }

    public ServiceException(String msg, Throwable e) {
        super(msg, e);
    }

    public ServiceException(String code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}