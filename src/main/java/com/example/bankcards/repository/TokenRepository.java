package com.example.bankcards.repository;

import com.example.bankcards.entity.Token;
import com.example.bankcards.entity.Token;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Репозиторий для Token
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    /**
     * Поиск Токена по токену
     * @param token токен
     * @return {@link Optional}&lt;{@link Token}&gt;
     */
    Optional<Token> findByToken(String token);

}
