package com.martini.sparrow.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Map;

/**
 * @author martini at 2020/12/14 10:12
 */
@Slf4j
public class JwtUtils {

    /**
     * 验证token是否合法
     * @param token Headers中携带的验证信息
     * @param username 用户名
     * @param password 密码
     * @return 校验结果
     */
    public static boolean verify(String token, String username, String password) {
        try {
            // withClaim表示需要这个token中的某个字段存在并且值与某一个参数相等
            JWT.require(Algorithm.HMAC256(password)).withClaim("username", username).build().verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * password作为加密规则
     * @param nickname
     * @param password
     * @param id
     * @param avatar
     * @return
     */
    public static String sign(String password, Map<String, String> payload) {
        try {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DATE, 3);
            JWTCreator.Builder builder = JWT.create();
            payload.forEach(builder::withClaim);
            builder.withExpiresAt(instance.getTime());
            return builder.sign(Algorithm.HMAC256(password));
        } catch (IllegalArgumentException | JWTCreationException e) {
            log.error("error when create signature");
            return null;
        }
    }

}
