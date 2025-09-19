package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для Card
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    /**
     * Поиск карты по id
     * @param id id карты
     * @return {@link Optional}&lt;{@link Card}&gt;
     */
    Optional<Card> findById(Integer id);

    /**
     * Поиск карты по номеру
     * @param number номер карты
     * @return {@link Optional}&lt;{@link Card}&gt;
     */
    Optional<Card> findByNumber(String number);

    /**
     * Поиск списка карт по дате истечения срока её срока действия
     * @param endDate дата истечения срока действия карты
     * @return {@link Optional}&lt;{@link List}&lt;{@link Card}&gt; &gt;
     */
    Optional<List<Card>> findAllCardByEndDate(LocalDate endDate);

    /**
     * Поиск всех карт пользователя, постранично(пагинация)
     * @param user пользователь, чьи карты ищем
     * @param pageable установки для пагинации
     * @return {@link Page}&lt;{@link Card}&gt;
     */
    Page<Card> findAllByUser(User user, Pageable pageable);

    /**
     *Поиск всех карт, постранично(пагинация)
     * @param pageable установки пагинации
     * @return {@link Page}&lt;{@link Card}&gt;
     */
    Page<Card> findAll(Pageable pageable);

}
