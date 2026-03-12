package com.provizit.Services;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class CryptoService {

    private static final String PUBLIC_KEY =
            "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjLQwij3HsU2eeCFWhjDw\n" +
                    "pkLyBu3tFujUyn+ZRUYdraI+ro9ZTOMN00xOpbPSfcxU1hKDGbyefsNlPoMYTWY4\n" +
                    "cD8o/lafo1VacbbupOiNHZ+FMuwROFn4HTezE3RG02DhPo5gA3CUZUdeSchYF7rF\n" +
                    "vHw1V5ELFvwHntFNwwoOCn9Uzozp7oWTnEO/nJJWOVOrpM4Ke6kqLBB1hXiT2ul/\n" +
                    "5XnWNiIlSW17HAHbwXEOa7kr2zF62JC7pp8VgzHWR4I6SdysJvDok3TQoOz74mPT\n" +
                    "iI+U9IGHrw2Y7ZbmPcPGha3UjIbnD2UiQYFMGGPF04M2124BdHTvIxd8NukR+W55\n" +
                    "PwIDAQAB\n" +
                    "-----END PUBLIC KEY-----";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encrypt(String data) {
        try {
            PublicKey publicKey = loadPublicKey(PUBLIC_KEY);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    private PublicKey loadPublicKey(String key) throws Exception {

        String cleaned = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(cleaned);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}