package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserLoginDtoReque;
import com.example.bankcards.dto.response.PublicKeyResp;
import com.example.bankcards.dto.response.ResponseTokens;
import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.service.RegistrationUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для регистрации, аутентификации и авторизации пользователя,
 * а также позволяет отзывать jwt токен, обновлять его и получать ключ для его проверки
 */
@RestController
public class AuthController {

    /**
     * Сервис по работе с {@link com.example.bankcards.entity.User}
     */
    private final RegistrationUser registrationUser;

    /**
     * Конструктор с параметрами(зависимости):
     * @param registrationUser сервис по работе с {@link com.example.bankcards.entity.User}
     */
    public AuthController(RegistrationUser registrationUser) {
        this.registrationUser = registrationUser;
    }

    /**
     * Эндпоинт для регистрации пользователя по его login-у и паролю - {@link UserDtoReque}
     * @param user данные пользователя: login и пароль
     * @return {@link ResponseEntity}&lt;{@link ResponseTokens}&gt;
     */
    @Operation(summary = "Регистрация",
            description = "Возвращает пустой ResponseTokens")
    @PostMapping("/auth/reg/user")
    public ResponseEntity<ResponseTokens> regUser(@RequestBody @Valid
                                      @Parameter(description = "данные пользователя: логин и пароль") UserDtoReque user) {
        registrationUser.register(user);
        return ResponseEntity.ok(new ResponseTokens());
    }

    /**
     * Аутентификация без jwt, с их получением
     * @param user данные пользователя
     * @return {@link ResponseEntity}&lt;{@link ResponseTokens}&gt;
     */
    @Operation(summary = "Аутентификация без токена",
            description = "Возвращает ResponseTokens с refresh токеном и null в место acess")
    @PostMapping("/auth/login")
    public ResponseEntity<ResponseTokens> login(@RequestBody @Valid
                                                    @Parameter(description = "данные пользователя: логин и пароль")
                                                UserLoginDtoReque user) {
        ResponseTokens responseTokens = registrationUser.login(user);
        return ResponseEntity.ok(responseTokens);
    }

    /**
     * Обновление токенов по refresh токену
     * @param token refresh токен
     * @return {@link ResponseEntity}&lt;{@link ResponseTokens}&gt;
     */
    @Operation(summary = "Обновление токенов по refresh токену",
            description = "Возвращает ResponseTokens с токенами")
    @PostMapping("/loginref")
    public ResponseEntity<ResponseTokens> loginByRefresh(@RequestBody @Parameter(description = "refresh-jwt для обновления токенов") String token) {
        ResponseTokens responseTokens = registrationUser.loginByRefresh(token);
        return ResponseEntity.ok(responseTokens);
    }

    /**
     *Отзыв токена
     * @param token токен для отзыва
     * @return {@link ResponseEntity}&lt;{@link ResponseTokens}&gt;
     */
    @Operation(summary = "Отзыв токена",
            description = "Возвращает пустой ResponseTokens")
    @DeleteMapping("/dellogin")
    public ResponseEntity<ResponseTokens> delRefresh(@RequestBody @Valid @Parameter(description = "refresh-jwt для отзыва токена") String token) {
        registrationUser.deleteRefresh(token);
        return ResponseEntity.ok(new ResponseTokens());
    }

    /**
     * Предоставление публичного ключа для проверки токена
     * @return {@link ResponseEntity}&lt;{@link PublicKeyResp}&gt;
     */
    @Operation(summary = "Получение публичного ключа для проверки токена",
            description = "Возвращает PublicKey")
    @GetMapping("/key")
    public ResponseEntity<PublicKeyResp> getKey() {
        return ResponseEntity.ok(registrationUser.getKey());
    }


}