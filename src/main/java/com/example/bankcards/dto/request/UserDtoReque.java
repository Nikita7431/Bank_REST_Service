package com.example.bankcards.dto.request;


import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserDtoReque {
    @NotNull(message = "id не должно быть пустым")
    private Integer id;
    @NotNull(message = "Логин не должен быть пустым")
    @Length(max = 20, message = "логин слишком большой(до 20 символов)")
    private String login;
    @NotNull(message = "Пароль не должен быть пустым")
    @Length(min = 5, max = 10, message = "Пароль должен быть от 5 до 10 символов")
    private String password;

    public UserDtoReque(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setName(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
