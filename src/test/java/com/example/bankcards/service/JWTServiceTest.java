package com.example.bankcards.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.bankcards.dto.request.UserDtoReque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    void beforeEachMethod() throws Exception {
        jwtService = new JWTService();
    }

    @Test
    void generateToken_User_returnsToken() throws Exception {
        UserDtoReque user = new UserDtoReque("login", "password");
        user.setId(4);
        String token = jwtService.generateAccessToken(user);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void generateToken_UserIsNull_Exception() {
        Exception ex = assertThrows(Exception.class,
                () -> jwtService.generateAccessToken(null));

        assertEquals("Пользователь не валиден", ex.getMessage());
    }

    @Test
    void validateTokenAndSyncClaim_Token_ReturnUser() throws Exception {
        UserDtoReque user = new UserDtoReque("login", "password");
        user.setId(4);
        String token = jwtService.generateAccessToken(user);
        UserDtoReque userDtoReque = jwtService.validateTokenAndSyncClaim(token);

        assertNotNull(userDtoReque);
        assertEquals("login", userDtoReque.getLogin());
        assertEquals("password", userDtoReque.getPassword());
    }

    @Test
    void validateTokenAndSyncClaim_TokenDotBlank_Exception() {
        assertThrows(JWTVerificationException.class,
                () -> jwtService.validateTokenAndSyncClaim(" "));
    }

    @Test
    void validateTokenAndSyncClaim_TokenIsNull_JWTVerificationException() {
        assertThrows(JWTVerificationException.class,
                () -> jwtService.validateTokenAndSyncClaim(null));
    }
}

