package com.springboot.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Entity.User;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

public class CyptroUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] SECRET_KEY = "1234567890123456".getBytes();
    private static final byte[] IV = "1234567890123456".getBytes();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //Convert into readable format
    public static String decrypt(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    //Convert into un-readable format
    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        // Convert the plain text to bytes
        byte[] plainBytes = plainText.getBytes("UTF-8");
        // Encrypt the bytes
        byte[] encryptedBytes = cipher.doFinal(plainBytes);
        // Encode encrypted bytes to Base64 string
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}