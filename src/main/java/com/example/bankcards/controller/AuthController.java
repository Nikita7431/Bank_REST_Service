package com.example.bankcards.controller;

import com.example.bankcards.dto.response.ResponseTokens;
import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.service.RegistrationUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final RegistrationUser registrationUser;

    @Autowired
    public AuthController(RegistrationUser registrationUser) {
        this.registrationUser = registrationUser;
    }
    @Operation(summary = "Регистрация",
            description = "Возвращает пустой ResponseTokens")
    @PostMapping("/reg/user")
    public ResponseEntity regUser(@RequestBody @Valid
                                      @Parameter(description = "данные пользователя: логин и пароль") UserDtoReque user) {
        registrationUser.register(user);
        return ResponseEntity.ok(new ResponseTokens());
    }
    @Operation(summary = "Аутентификация по refresh токену",
            description = "Возвращает ResponseTokens с refresh токеном и null в место acess")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid @Parameter(description = "данные пользователя: логин и пароль") UserDtoReque user) {
        ResponseTokens responseTokens = registrationUser.login(user);
        return ResponseEntity.ok(responseTokens);
    }
    @Operation(summary = "Обновление токенов по refresh токену",
            description = "Возвращает ResponseTokens с токенами")
    @PostMapping("/loginref")
    public ResponseEntity loginByRefresh(@RequestBody @Parameter(description = "refresh-jwt для обновления токенов") String token) {
        ResponseTokens responseTokens = registrationUser.loginByRefresh(token);
        return ResponseEntity.ok(responseTokens);
    }
    @Operation(summary = "Получение публичного ключа для расшифровки токена",
            description = "Возвращает пустой ResponseTokens")
    @DeleteMapping("/dellogin")
    public ResponseEntity delRefresh(@RequestBody @Valid @Parameter(description = "refresh-jwt для отзыва токена") String token) {
        registrationUser.deleteRefresh(token);
        return ResponseEntity.ok(new ResponseTokens());
    }
    @Operation(summary = "Получение публичного ключа для расшифровки токена",
            description = "Возвращает PublicKey")
    @GetMapping("/key")
    public ResponseEntity getKey() {
        return ResponseEntity.ok().body(registrationUser.getKey().toString());
    }


}