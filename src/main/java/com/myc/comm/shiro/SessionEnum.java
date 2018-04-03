package com.myc.comm.shiro;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/22 16:46
 */
public enum SessionEnum {
    SESSION_USER("userSession"),
    SESSION_USER_ID("userSessionId"),
    SESSION_TOKEN("shiro_jwt_token");

    SessionEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
