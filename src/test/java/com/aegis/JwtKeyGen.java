package com.aegis;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/3 10:23
 * @Description: JWT 密钥生成器
 */
public class JwtKeyGen {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256); // HS256
        SecretKey secretKey = keyGenerator.generateKey();

        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(base64Key);
    }
}
