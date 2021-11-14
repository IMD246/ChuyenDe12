package com.example.EnglishBeginner.DTO;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class HashPass {
    final static private String AES = "AES";
    @RequiresApi(api = Build.VERSION_CODES.O)
    final public static String encryptPass(String uidUser,String passWord) throws Exception{
        SecretKeySpec key = generateKey(uidUser);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte [] encVal = cipher.doFinal(passWord.getBytes());
        String encryptedValue = Base64.getEncoder().encodeToString(encVal);
        return encryptedValue;
    }
    final private static SecretKeySpec generateKey(String uidUser) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte [] bytes = uidUser.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,AES);
        return secretKeySpec;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    final public static String decryptPass(String outPutString, String uidUser) throws Exception{
        SecretKeySpec keySpec = generateKey(uidUser);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE,keySpec);
        byte [] decrypt = Base64.getDecoder().decode(outPutString);
        byte [] decValue = cipher.doFinal(decrypt);
        String decryptValue = new String(decValue);
        return  decryptValue;
    }
}
