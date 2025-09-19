package com.example.bankcards.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/** * Dto для регистрации пользователя */
@Getter @Setter
public class UserDtoReque {
    /** имя пользователя {@link UserDtoReque#name} */
    @NotNull(message = "имя не должно быть пустым")
    private String name;
    /** Логин пользователя {@link UserDtoReque#login} */
    @NotNull(message = "Логин не должен быть пустым")
    @Length(max = 20, message = "логин слишком большой(до 20 символов)")
    private String login;
    /** Пароль пользователя {@link UserDtoReque#password} */
    @NotNull(message = "Пароль не должен быть пустым")
    @Length(min = 5, max = 10, message = "Пароль должен быть от 5 до 10 символов")
    private String password;

    /**
     * Конструктор имеет параметры:
     * @param login логин пользователя
     * @param password пароль пользователя
     * @param name имя пользователя
     */
    public UserDtoReque(String login, String password,String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }


}
