package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.StatusCard;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.StatusCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Сервис реализующий функционал по работе с сущьностью {@link StatusCard} */

@Service
public class StatusUserCardService implements StatusCardService{
    /**
     * Репозиторий по работе с {@link StatusCard}
     */
    public final StatusCardRepository statusCardRepository;

    /**
     * Конструктор с параметрами(зависимости):
     * @param statusCardRepository {@link StatusCardRepository} - Репозиторий по работе с {@link StatusCard}
     */
    public StatusUserCardService(StatusCardRepository statusCardRepository) {
        this.statusCardRepository = statusCardRepository;
    }

    /**
     * Осуществляет поиск по названию статуса
     * @param status название
     * @return {@link StatusCard}
     */
    public StatusCard findStatus(String status){
        return statusCardRepository
                .findByStatus(status)
                .orElseThrow(()-> new NoSuchElementException("Такого статуса не существует"));
    }


}
