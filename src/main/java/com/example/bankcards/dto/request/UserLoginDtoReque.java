package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/** * Dto для аутентификации пользователя */
@Getter @Setter
public class UserLoginDtoReque {

    /** Логин пользователя {@link UserLoginDtoReque#login} */
    @NotNull(message = "Логин не должен быть пустым")
    @Length(max = 20, message = "логин слишком большой(до 20 символов)")
    private String login;
    /** Пароль пользователя {@link UserLoginDtoReque#password} */
    @NotNull(message = "Пароль не должен быть пустым")
    @Length(min = 5, max = 10, message = "Пароль должен быть от 5 до 10 символов")
    private String password;

    /**
     * Конструктор с параметрами:
     * @param login login пользователя
     * @param password пароль пользователя
     */
    public UserLoginDtoReque(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
