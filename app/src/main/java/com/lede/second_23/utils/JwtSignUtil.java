package com.lede.second_23.utils;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;

import java.util.Map;

/**
 * Created by lacoo on 2016/11/1.
 */

public class JwtSignUtil {

    private JwtSignUtil(){}

    private final static String secret = "wm";

    public static String signer(Map<String, Object> map) throws Exception {
        JWTSigner jwtSigner = new JWTSigner(secret);
//        Map<String, Object> map = new HashMap<>();
//        map.put("data",obj);
        return jwtSigner.sign(map);
    }

    public static Map<String, Object> verify(String token) throws Exception {
        JWTVerifier jwtVerifier = new JWTVerifier(secret);
        return jwtVerifier.verify(token);
    }
}
