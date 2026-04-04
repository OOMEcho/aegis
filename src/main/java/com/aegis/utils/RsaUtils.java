package com.aegis.utils;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

/**
 * RSA加密工具类
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
@Slf4j
@Component
public final class RsaUtils {

    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_CIPHER_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final int RSA_KEY_SIZE = 2048;

    /**
     * 使用 AtomicReference 保证密钥轮换的原子性
     */
    private static final AtomicReference<RsaKeyPair> RSA_KEY_PAIR_REF = new AtomicReference<>();

    /**
     * 获取当前密钥对
     */
    public static RsaKeyPair getKeyPair() {
        return RSA_KEY_PAIR_REF.get();
    }

    /**
     * 私钥解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String text) {
        return decryptByPrivateKey(getKeyPair().getPrivateKey(), text);
    }

    /**
     * 公钥加密
     *
     * @param text 待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String text) {
        return encryptByPublicKey(getKeyPair().getPublicKey(), text);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text             待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyString, String text) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(text));
            return new String(result);
        } catch (Exception e) {
            log.error("RSA私钥解密失败", e);
            throw new RuntimeException("RSA解密失败", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param publicKeyString 公钥
     * @param text            待解密的信息
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyString, String text) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(text));
            return new String(result);
        } catch (Exception e) {
            log.error("RSA公钥解密失败", e);
            throw new RuntimeException("RSA解密失败", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param privateKeyString 私钥
     * @param text             待加密的信息
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String privateKeyString, String text) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            log.error("RSA私钥加密失败", e);
            throw new RuntimeException("RSA加密失败", e);
        }
    }

    /**
     * 公钥加密
     *
     * @param publicKeyString 公钥
     * @param text            待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyString, String text) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            log.error("RSA公钥加密失败", e);
            throw new RuntimeException("RSA加密失败", e);
        }
    }

    /**
     * 构建RSA密钥对 — 原子替换保证并发安全
     */
    @PostConstruct
    @Scheduled(cron = "0 0 0 ? * 1")
    public void generateKeyPair() {
        try {
            log.info("Regenerate an RSA key pair (keySize={})", RSA_KEY_SIZE);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(RSA_KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            String publicKeyString = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
            String privateKeyString = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
            // 原子替换密钥对，避免竞态条件
            RSA_KEY_PAIR_REF.set(new RsaKeyPair(publicKeyString, privateKeyString));
        } catch (Exception e) {
            log.error("RSA密钥对生成失败", e);
            throw new RuntimeException("RSA密钥对生成失败", e);
        }
    }

    /**
     * RSA密钥对对象（不可变）
     */
    @Data
    @AllArgsConstructor
    public static class RsaKeyPair {
        /**
         * 公钥
         */
        private final String publicKey;

        /**
         * 私钥
         */
        private final String privateKey;
    }
}
