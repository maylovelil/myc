package com.myc.comm.jwt;

import com.myc.comm.shiro.SessionEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/9 13:48
 */
public class JWTToken implements AuthenticationToken {

    public static String AUTH_TOKEN = "Authorization";

    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public static void clear() {
        Session session = SecurityUtils.getSubject().getSession();
        String userId = String.valueOf(session.getAttribute(SessionEnum.SESSION_USER_ID.getValue()));
        session.removeAttribute(SessionEnum.SESSION_USER.getValue() + "_" + userId);
        session.removeAttribute(SessionEnum.SESSION_USER_ID.getValue());
        session.removeAttribute(SessionEnum.SESSION_TOKEN.getValue());
    }
}