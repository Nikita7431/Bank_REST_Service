package com.example.bankcards.repository;

import com.example.bankcards.entity.StatusCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusCardRepository extends JpaRepository<StatusCard, Integer> {

    Optional<StatusCard> findByStatus(String status);
}
