package com.example.bankcards.service;


import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.dto.response.UserDtoResp;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.PeopleRepository;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.security.PersonDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Реалиует функционал по работе с {@link User}
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * Репозиторий для работы с {@link User}
     */
    private final PeopleRepository peopleRepository;
    /**
     * Сервис для работы с {@link Role}
     */
    private final RoleService roleUserService;
    /**
     * Для кодирования пароля в бд и его сравнения
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Маппер объектов
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор с параметрамии(зависимости):
     *
     * @param peopleRepository {@link PeopleRepository} - репозиторий по работе с {@link User}
     * @param passwordEncoder  {@link PasswordEncoder}
     * @param modelMapper      {@link ModelMapper} - маппер объектов
     */
    public UserServiceImpl(PeopleRepository peopleRepository,
                           RoleService roleUserService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.peopleRepository = peopleRepository;
        this.roleUserService = roleUserService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод от {@link org.springframework.security.core.userdetails.UserDetailsService}
     * Осузествляет поиск пользователя по его login-у
     *
     * @param login login пользователя
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException исключение при ненайденном login-е пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = peopleRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Пользователь отсутсвует"));

        return new PersonDetails(user);
    }

    /**
     * Проверяет наличие пользователя в БД
     *
     * @param login    login пользователя
     * @param password пароль пользователя
     * @return {@code true}/{@code false} в зависимости от наличия
     */
    public boolean existsUser(String login, String password) {
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        try {
            User findedUser = peopleRepository.findByLogin(login)
                    .orElseThrow(() -> new NoSuchElementException());
            return passwordEncoder.matches(password, findedUser.getPassword());
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    /**
     * Ищет всех пользователей
     *
     * @return {@link List}@lt;{@link UserDtoResp}>@gt;
     */
    public List<UserDtoResp> findAll() { //TODO тесты
        return peopleRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDtoResp.class))
                .toList();
    }

    /**
     * Осуществляет поиск пользователя по его id
     *
     * @param id id пользователя
     * @return {@link User}
     */
    @Override
    public User findById(Integer id) {
        Objects.requireNonNull(id, "Id не должно быть пустым");
        Optional<User> userOpt = Optional.of(peopleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("пользователя с таким id не существует")));
        return userOpt.get();
    }
    /**
     * Сохраняет пользователя
     * @param user
     */
    private void save(User user) {
        peopleRepository.save(user);
    }
    /**
     * Маппит UserDtoReque в User и сохраняет его
     * @param user данные пользователя для сохранения
     */
    @Override
    @Transactional
    public void save(UserDtoReque user) {
        User newUser = modelMapper.map(user, User.class);
        roleUserService.findByRole("USER");
        save(modelMapper.map(user, User.class));
    }
}
