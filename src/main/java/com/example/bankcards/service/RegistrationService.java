package com.example.bankcards.service;


import com.example.bankcards.dto.request.UserLoginDtoReque;
import com.example.bankcards.dto.response.PublicKeyResp;
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

/**
 * Сервис отвечающий за реализацию функционала по работе с регистрацией и аутентификацией:
 * <ul>
 *     <li>регистрирует пользователя {@link  RegistrationService#register(UserDtoReque)}</li>
 *    <li>аутентифицирует пользователя без jwt {@link RegistrationService#login(UserLoginDtoReque)}</li>
 *  <li>обновляет токены по {@link RegistrationService#loginByRefresh(String)}</li>
 *  <li>отзывает токен у пользователя {@link RegistrationService#deleteRefresh(String)}</li>
 *  <li>предоставляет публичный ключ клиенту для проверки токена {@link RegistrationService#getKey()}</li>
 *  </ul>
 */
@Service
public class RegistrationService implements RegistrationUser {

    /**
     * Репозиторий по работе с {@link Token}
     */
    private final TokenRepository tokenRepository;
    /**
     *Осуществляет шифрование пароля перед сохранением в бд и валидацию при логине
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Сервис по работе с {@link User}
     */
    private final UserServiceImpl userService;
    /**
     * Сервис по работе с jwt
     */
    private final JWTService jwtService;

    /**
     * Конструктор с параметрами (зависимости):
     * @param tokenRepository репозиторий по работе с {@link Token}
     * @param passwordEncoder осуществляет шифрование пароля перед сохранением в бд и валидацию при логине
     * @param userService сервис по работе с {@link User}
     * @param jwtService сервис по работе с jwt
     */
    public RegistrationService( TokenRepository tokenRepository,
                                PasswordEncoder passwordEncoder,
                                UserServiceImpl userService,
                                JWTService jwtService) {
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Регистрирует пользователя по login-у и паролю
     * @param user данные пользователя для регистрации
     */
    @Override
    @Transactional
    public void register(UserDtoReque user) {
        if (user == null) throw new IllegalArgumentException("Нет пользователя");
        /**
         * Проверяет наличие пользователя
         и если пользователь такой отстутствует регистрирует его, сохраняя в бд*/
        if (!userService.existsUser(user.getLogin(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
        } else {
            throw new IllegalArgumentException("Такой пользователь уже существует");
        }
    }
    /**
     * Аутентифицирует пользователя без jwt
     * @param userLoginDTOReque данные пользователя для регистрации
     * @return Access и Refresh токены
     */
    @Transactional
    public ResponseTokens login(UserLoginDtoReque userLoginDTOReque) {
        if (userLoginDTOReque == null) throw new IllegalArgumentException("Переданный пользователь отсутствует");
        if (userService.existsUser(userLoginDTOReque.getLogin(), userLoginDTOReque.getPassword())) {
            try {
                return jwtService.generateTokens(userLoginDTOReque);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Переданный пользователь отсутствует");
            }
        } else {
            throw new IllegalArgumentException("Такого пользователя не существует");
        }
    }
    /**
     *Продлевает Access токен по Refresh токену
     * @param refToken Refresh токен
     * @return Access и Refresh токены
     */
    @Transactional
    public ResponseTokens loginByRefresh(String refToken) {

        if (refToken == null || refToken.isBlank()) {
            throw new NullPointerException("Токен не должен быть пустым");
        }
        UserLoginDtoReque userDTOReque = jwtService.validateTokenAndSyncClaim(refToken);
        Token token = tokenRepository.findByToken(refToken).orElseThrow(() -> new IllegalArgumentException("Сессии нет"));
        if (userService.existsUser(userDTOReque.getLogin(), userDTOReque.getPassword())) {
            try {
                ResponseTokens responseTokens = jwtService.generateTokens(userDTOReque);
                token.setToken(responseTokens.getRefreshToken());
                return responseTokens;
            } catch (Exception ex) {
                throw new IllegalArgumentException("Сессии нет");
            }

        } else {
            throw new IllegalArgumentException("Сессии нет ");
        }
    }
    /**
     * Отзывает токен, удаляя из БД
     * @param refToken Refresh токен
     */
    @Transactional
    public void deleteRefresh(String refToken) {
        if (refToken == null || refToken.isBlank()) {
            throw new NullPointerException("Токен не должен быть пустым");
        }
        Token token = tokenRepository.findByToken(refToken).orElseThrow(() -> new NoSuchElementException("Сессии нет или она истекла"));
        tokenRepository.delete(token);


    }
    /**
     * Предоставляет публичный ключ клиенту для проерки токена
     * @return публичный ключ
     */
    public PublicKeyResp getKey() {
        return new PublicKeyResp(jwtService.getPublicKey());
    }
}
