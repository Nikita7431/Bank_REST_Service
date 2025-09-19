package com.example.bankcards.repository;


import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Репозиторий для User
 */
@Repository
public interface PeopleRepository extends JpaRepository<User, Integer> {
    /**
     * Поиск пользователя по его id
     * @param id id пользователя, которого ищем
     * @return {@link Optional}&lt;{@link User}&gt;
     */
    Optional<User>findById(Integer id);

    /**
     * Поиск пользователя по его login-у
     * @param login login, искуемого пользователя
     * @return {@link Optional}&lt;{@link User}&gt;
     */
    Optional<User> findByLogin(String login);

    /**
     * Поиск всех пользователей
     * @return {@link List}&lt;{@link User}&gt;
     */
    List<User> findAll();

}
