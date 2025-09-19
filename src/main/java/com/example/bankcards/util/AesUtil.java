package com.example.bankcards.util;

import com.example.bankcards.entity.Card;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.List;

/**
 * Реализует функционал для AES шифрования и дешифрования данных
 * <p>Пример использования:</p>
 * <pre>
 *       SecretKey key = AesUtil.generateAESKey();
 *       String encrypted = AesUtil.encrypt("Text", key);
 *       String decrypted = AesUtil.decrypt(encrypted, key); //Text
 *   </pre>
 */
@NoArgsConstructor
public class AesUtil {
    /**
     *Генерирует AES ключ
     * @return {@link SecretKey} - сгенерированный ключ
     * @throws Exception исключения в процессе генерации ключа
     */
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

    /**
     * Шифрует строку и кодирует в Base64
     * @param data строка для шифрования
     * @param key ключ шифрования
     * @return зашифрованная строка
     * @throws Exception исключения в процессе шифрования
     */
    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    /**
     * Дешифрует строку и декодирует из Base64
     * @param encrypted зашифрованная и закодированная строка
     * @param key ключ шифрвания
     * @return строка раскодированная и расшифрованная
     * @throws Exception исключения в процессе дешифрования
     */
    public static String decrypt(String encrypted, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
    }
}
