package com.example.bankcards.service;


import com.example.bankcards.dto.response.ResponseTokens;
import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.entity.Token;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.PeopleRepository;
import com.example.bankcards.repository.TokenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.interfaces.RSAPublicKey;
import java.util.NoSuchElementException;

@Service
public class RegistrationService implements RegistrationUser {
    private final PeopleRepository peopleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, TokenRepository tokenRepository,
                               PasswordEncoder passwordEncoder,
                               UserServiceImpl userService, JWTService jwtService, ModelMapper modelMapper) {
        this.peopleRepository = peopleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void register(UserDtoReque user) {
        if (user == null)
            throw new IllegalArgumentException("Нет пользователя");
        if (!userService.existsUser(user.getLogin(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            peopleRepository.save(modelMapper.map(user, User.class));
        } else {
            throw new IllegalArgumentException("Такой пользователь уже существует");
        }
    }

    @Transactional
    public ResponseTokens login(UserDtoReque userDTOReque) {
        if (userDTOReque == null)
            throw new IllegalArgumentException("Переданный пользователь отсутствует");
        if (userService.existsUser(userDTOReque.getLogin(), userDTOReque.getPassword())) {
            try {
                return jwtService.generateTokens(userDTOReque);
            }catch (Exception ex){
                throw new IllegalArgumentException("Переданный пользователь отсутствует");
            }
        } else {
            throw new IllegalArgumentException("Такого пользователя не существует");
        }
    }

    @Transactional
    public ResponseTokens loginByRefresh(String refToken) {

        if (refToken == null || refToken.isBlank()) {
            throw new NullPointerException("Токен не должен быть пустым");
        }
        UserDtoReque userDTOReque = jwtService.validateTokenAndSyncClaim(refToken);
        Token token = tokenRepository.findByToken(refToken)
                .orElseThrow(() -> new IllegalArgumentException("Сессии нет или она истекла"));
        if (userService.existsUser(userDTOReque.getLogin(), userDTOReque.getPassword())) {
            try {
                ResponseTokens responseTokens = jwtService.generateTokens(userDTOReque);
                token.setToken(responseTokens.getRefreshToken());
                return responseTokens;
            }catch (Exception ex){
                throw new IllegalArgumentException("Сессии нет или она истекла");
            }

        }else{
            throw new IllegalArgumentException("Сессии нет или она истекла");
        }
    }

    @Transactional
    public void deleteRefresh(String refToken) {
        if (refToken == null || refToken.isBlank()) {
            throw new NullPointerException("Токен не должен быть пустым");
        }
        Token token = tokenRepository.findByToken(refToken)
                .orElseThrow(() -> new NoSuchElementException("Сессии нет или она истекла"));
        tokenRepository.delete(token);


    }

    public RSAPublicKey getKey() {
        return jwtService.getPublicKey();
    }
}
