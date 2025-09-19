package com.example.bankcards.repository;

import com.example.bankcards.entity.StatusCard;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Репозиторий для StatusCard
 */
@Repository
public interface StatusCardRepository extends JpaRepository<StatusCard, Integer> {
    /**
     * Поиск статуса карты по его названию(статусу)
     * @param status название статуса(пример: ACTIVE)
     * @return {@link Optional}&lt;{@link StatusCard}&gt;
     */
    Optional<StatusCard> findByStatus(String status);
}
