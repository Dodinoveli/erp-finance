package com.logikaintermedia.erp.encryption;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

/**
 * untuk enkripsi data sensitip seperti norek, nik, bank dll
 * 
 */
@Slf4j
public class EncryptionUtil {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    // Ambil key dari Environment Variable (Jangan di-hardcode!)
    // private static final String SECRET_KEY = System.getenv("DB_ENCRYPTION_KEY");
    // Pas 32 karakter
    private static final String SECRET_KEY = "89fbb7daba5bf679f32e5936d849599c";
    // private static final String SECRET_KEY =
    // "89fbb7daba5bf679f32e5936d849599c9cf2425dfb856975eccf3547e308f553";

    public static String encrypt(String strToEncrypt) {

        // 1. Tambahkan pengecekan ini di baris paling atas
        if (strToEncrypt == null || strToEncrypt.trim().isEmpty()) {
            return strToEncrypt;
        }

        try {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes());
            byte[] message = new byte[IV_LENGTH_BYTE + cipherText.length];
            System.arraycopy(iv, 0, message, 0, IV_LENGTH_BYTE);
            System.arraycopy(cipherText, 0, message, IV_LENGTH_BYTE, cipherText.length);

            return Base64.getEncoder().encodeToString(message);
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengenkripsi data", e);
        }
    }

    public static String decrypt(String strToDecrypt) {
        // Pintu darurat jika data di DB kosong
        if (strToDecrypt == null || strToDecrypt.trim().isEmpty()) {
            return strToDecrypt;
        }

        try {
            byte[] decoded = Base64.getDecoder().decode(strToDecrypt);
            byte[] iv = new byte[IV_LENGTH_BYTE];
            System.arraycopy(decoded, 0, iv, 0, IV_LENGTH_BYTE);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] cipherText = new byte[decoded.length - IV_LENGTH_BYTE];
            System.arraycopy(decoded, IV_LENGTH_BYTE, cipherText, 0, cipherText.length);

            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) {
            throw new RuntimeException("Gagal mendekripsi data", e);
        }
    }

    public static String decryptSafely(String encryptedText) {
        if (encryptedText == null || encryptedText.trim().isEmpty()) {
            return encryptedText;
        }
        try {
            return EncryptionUtil.decrypt(encryptedText);
        } catch (Exception e) {
            log.error("Gagal mendekripsi: {}", encryptedText, e);
            return encryptedText; // fallback ke nilai asli, TIDAK THROW!
        }
    }

}
