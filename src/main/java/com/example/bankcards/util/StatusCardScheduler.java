package com.example.bankcards.util;

import com.example.bankcards.service.CardService;
import com.example.bankcards.service.StatusCardService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Sheduler для изменения статуса карт у которых истекает срок действия на EXPIRED
 */
@Component
public class StatusCardScheduler {
    /**
     * Сервис по работе с картами
     */
    private final CardService cardService;

    /**
     * Конструктор с параметрами(зависимости):
     * @param cardService сервис по работе с картами
     */
    public StatusCardScheduler(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Обновляет статус у всех истекающих карт на EXPIRED
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateExpiredCards() {
        cardService.updateExpiredCards();
    }

}

