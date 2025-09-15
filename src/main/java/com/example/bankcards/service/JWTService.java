package com.example.bankcards.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.dto.response.ResponseTokens;
import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.dto.response.UserDtoResp;
import com.example.bankcards.util.AesUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Service
public class JWTService {
//    @Value("${jwt_secret}")
//    private String secret;

    @Value("${jwt.access_token_expiration_m}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh_token_expiration_m}")
    private long refreshTokenExpiration;

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;
    private final SecretKey secretKey;

    public JWTService() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        secretKey = AesUtil.generateAESKey();
    }


    private String generateToken(UserDtoReque user, long expiryTime) throws Exception {
        if (user == null) {
            throw new Exception("Пользователь не валиден");
        }

        String encryptedId = AesUtil.encrypt(user.getId().toString(), secretKey);
        String encryptedLogin = AesUtil.encrypt(user.getLogin(), secretKey);
        String encryptedPassword = AesUtil.encrypt(user.getPassword(), secretKey);

        Date expirationDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        try {
            return JWT.create()
                    .withSubject("User details")
                    .withClaim("id", encryptedId)
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

    public UserDtoReque validateTokenAndSyncClaim(String token) {
        if(token == null || token.isBlank()){
            throw new JWTVerificationException("Токен пуст");
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256(publicKey, null))
                .withSubject("User details")
                .withIssuer("USER")
                .build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        try {
            return new UserDtoReque(
                    AesUtil.decrypt(decodedJWT.getClaim("login").asString(), secretKey),
                    AesUtil.decrypt(decodedJWT.getClaim("password").asString(), secretKey)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public String generateAccessToken(UserDtoReque user) throws Exception {

        return generateToken(user, accessTokenExpiration);

    }

    public String generateRefreshToken(UserDtoReque user) throws Exception {

        return generateToken(user, refreshTokenExpiration);

    }

    public ResponseTokens generateTokens(UserDtoReque user) throws Exception {
        return new ResponseTokens(generateAccessToken(user), generateRefreshToken(user));

    }
//    public String getSigningKey() {
//        byte[] keyBytes =
//        return secret;
//    }


}
