package com.myc.comm.utils;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.myc.entity.User;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/8 18:57
 */
public class JWTUtils {
    // 过期时间5分钟
    private static final long EXPIRE_TIME = 5*60*1000;

    /**
     * 校验token是否正确
     * @param jwtToken 密钥
     * @param user 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String jwtToken,User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(user.getPassword());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", user.getUsername())
                    .withClaim("userId",user.getId())
                    .withClaim("user",JSONObject.toJSONString(user))
                    .build();
            DecodedJWT jwt = verifier.verify(jwtToken);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String jwtToken) {
        try {
            DecodedJWT jwt = JWT.decode(jwtToken);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUserId(String jwtToken) {
        try {
            DecodedJWT jwt = JWT.decode(jwtToken);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static User getUser(String jwtToken) {
        try {
            DecodedJWT jwt = JWT.decode(jwtToken);
            return JSONObject.parseObject(jwt.getClaim("user").asString(),User.class);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     * @param user 用户名
     * @return 加密的token
     */
    public static String sign(User user) {
        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(user.getPassword());
            // 附带username信息
            return JWT.create()
                    .withClaim("username", user.getUsername())
                    .withClaim("userId",user.getId())
                    .withClaim("user",JSONObject.toJSONString(user))
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.setUsername("123");
        user.setPassword("234");
        user.setId(1);
        String token = JWTUtils.sign(user);
        System.out.println(JWTUtils.getUserId(token));
        System.out.println(JWTUtils.verify(token,user));
    }
}
