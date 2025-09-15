package com.example.bankcards.service;

import com.example.bankcards.dto.response.ResponseTokens;
import com.example.bankcards.dto.request.UserDtoReque;

import java.security.interfaces.RSAPublicKey;

public interface RegistrationUser {

    public void register(UserDtoReque user);
    public ResponseTokens login(UserDtoReque user);
    public ResponseTokens loginByRefresh(String refToken);
    public void deleteRefresh(String refToken);
    public RSAPublicKey getKey();

}
