package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.dto.response.UserDtoResp;
import com.example.bankcards.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Данный интерфейс представляет функционал для работы с пользователем
 * Он реализует {@link UserDetailsService}, который загружает данные о пользователе ввиде {@link org.springframework.security.core.userdetails.UserDetails},
 * которые в свою очередь мы загружаем в {@link org.springframework.security.core.context.SecurityContext},
 * что предоставляет нам аутентифицировать пользователя и авторизовать его
 * */
public interface UserService extends UserDetailsService {
    /**
     * Осуществляет поиск пользователя по его id
     * @param id id пользователя
     * @return {@link User}
     */
    public User findById(Integer id);

    /**
     * Наличие пользователя в БД
     * @param login login пользователя
     * @param password пароль пользователя
     * @return {@code true}/{@code false} в зависимости от наличия
     */
    public boolean existsUser(String login, String password);

    /**
     * Ищет всех пользователей
     *
     * @return {@link List}&lt;{@link UserDtoResp}&gt;
     */
    public List<UserDtoResp> findAll();

    /**
     * Сохраняет пользователя
     * @param user сохраняемый пользователь
     */
    public void save(UserDtoReque user);
    }
