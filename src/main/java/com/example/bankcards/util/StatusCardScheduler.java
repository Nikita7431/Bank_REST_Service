package com.example.bankcards.util;

import com.example.bankcards.service.CardService;
import com.example.bankcards.service.StatusCardService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class StatusCardScheduler {

    private final CardService cardService;

    public StatusCardScheduler(CardService cardService, StatusCardService statusCardService) {
        this.cardService = cardService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateExpiredCards() {
        cardService.updateExpiredCards();
    }

}

