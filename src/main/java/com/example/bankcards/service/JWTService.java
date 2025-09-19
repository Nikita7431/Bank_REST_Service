package com.example.bankcards.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.bankcards.dto.request.UserLoginDtoReque;
import com.example.bankcards.dto.response.ResponseTokens;
import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.util.AesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Сервис по работе с jwt
 */
@Service
public class JWTService {
//    @Value("${jwt_secret}")
//    private String secret;
    /**
     * Длительность Access токена
     */
    @Value("${jwt.access_token_expiration_m}")
    private long accessTokenExpiration;
    /**
     * Длительность Refresh токена
     */
    @Value("${jwt.refresh_token_expiration_m}")
    private long refreshTokenExpiration;
    /**
     * Публичный ключ
     */
    private final RSAPublicKey publicKey;
    /**
     * Приватный ключ
     */
    private final RSAPrivateKey privateKey;
    /**
     * Ключ для шифрования
     */
    private final SecretKey secretKey;

    /**
     * Конструктор без параметров
     * Создаёт пару: публичный и приватный ключ
     * Генерирует {@link JWTService#secretKey}
     * @throws Exception
     */
    public JWTService() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        secretKey = AesUtil.generateAESKey();
    }

    /**
     * Генерирует токен:
     * @param user данные пользователя для аутентификации
     * @param expiryTime время жизни токена
     * @return токен {@link String}
     * @throws Exception исключение при генерации токена
     */
    private String generateToken(UserLoginDtoReque user, long expiryTime) throws Exception {
        if (user == null) {
            throw new Exception("Пользователь не валиден");
        }

        String encryptedLogin = AesUtil.encrypt(user.getLogin(), secretKey);
        String encryptedPassword = AesUtil.encrypt(user.getPassword(), secretKey);

        Date expirationDate = Date.from(Instant.now().plus(expiryTime, ChronoUnit.HOURS));
        try {
            return JWT.create()
                    .withSubject("User details")
                    .withClaim("login", encryptedLogin)
                    .withClaim("password", encryptedPassword)
                    .withIssuedAt(new Date())
                    .withIssuer("USER")
                    .withExpiresAt(expirationDate)
                    .sign(Algorithm.RSA256(publicKey, privateKey));
        } catch (Exception ex) {
            throw new Exception("Пользователь не валиден");
        }
    }

    /**
     * Валидация токена и получение его содержимого
     *
     * @param token валидируемый токен
     * @return {@link UserLoginDtoReque} данные пользователя
     */
    public UserLoginDtoReque validateTokenAndSyncClaim(String token) {
        if(token == null || token.isBlank()){
            throw new JWTVerificationException("Токен пуст");
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256(publicKey, null))
                .withSubject("User details")
                .withIssuer("USER")
                .build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        try {
            return new UserLoginDtoReque(
                    AesUtil.decrypt(decodedJWT.getClaim("login").asString(), secretKey),
                    AesUtil.decrypt(decodedJWT.getClaim("password").asString(), secretKey)
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *Геттер для публичного ключа
     * @return publicKey публичный ключ
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * создаёт Access токен, вызовом {@link JWTService#generateToken(UserLoginDtoReque, long)}
     * @param user данные пользователя
     * @return токен {@link String} из {@link JWTService#generateToken(UserLoginDtoReque, long)}
     * @throws Exception исключение при генерации токена
     */
    public String generateAccessToken(UserLoginDtoReque user) throws Exception {

        return generateToken(user, accessTokenExpiration);

    }
    /**
     * создаёт Refresh токен, вызовом {@link JWTService#generateToken(UserLoginDtoReque, long)}
     * @param user данные пользователя
     * @return токен {@link String} из {@link JWTService#generateToken(UserLoginDtoReque, long)}
     * @throws Exception исключение при генерации токена
     */
    public String generateRefreshToken(UserLoginDtoReque user) throws Exception {

        return generateToken(user, refreshTokenExpiration);

    }

    /**
     * генерирует оба: Access и Refresh токены
     * @param user данные пользователя
     * @return {@link ResponseTokens}
     * @throws Exception исключение при генерации токенов
     */
    public ResponseTokens generateTokens(UserLoginDtoReque user) throws Exception {
        return new ResponseTokens(generateAccessToken(user), generateRefreshToken(user));

    }


}
