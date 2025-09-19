package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserLoginDtoReque;
import com.example.bankcards.dto.response.PublicKeyResp;
import com.example.bankcards.dto.response.ResponseTokens;
import com.example.bankcards.dto.request.UserDtoReque;

import java.security.interfaces.RSAPublicKey;
/**Данный интерфейс представляет функционал по работе с регистрацией и аутентификацией */
public interface RegistrationUser {
    /**
     * Регистрирует пользователя
     * @param user {@link UserDtoReque} - данные пользователя для регистрации
     */
    public void register(UserDtoReque user);

    /**
     * Аутентифицирует пользователя после регистрации без jwt
     * @param user {@link UserLoginDtoReque} - данные пользователя для регистрации
     * @return {@link ResponseTokens} - Access и Refresh токены
     */
    public ResponseTokens login(UserLoginDtoReque user);

    /**
     *Продлевает Access токен по Refresh токену
     * @param refToken Refresh токен
     * @return {@link ResponseTokens} - Access и Refresh токены
     */
    public ResponseTokens loginByRefresh(String refToken);

    /**
     * Отзывает токен, удаляя из БД
     * @param refToken Refresh токен
     */
    public void deleteRefresh(String refToken);

    /**
     * Предоставляет публичный ключ клиенту для проерки токена
     * @return {@link PublicKeyResp} - публичный ключ
     */
    public PublicKeyResp getKey();

}
