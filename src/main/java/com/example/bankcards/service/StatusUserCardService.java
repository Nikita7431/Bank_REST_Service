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

@Service
public class StatusUserCardService implements StatusCardService{

    public final StatusCardRepository statusCardRepository;

    public StatusUserCardService(StatusCardRepository statusCardRepository) {
        this.statusCardRepository = statusCardRepository;
    }

    public StatusCard findStatus(String status){
        return statusCardRepository
                .findByStatus(status)
                .orElseThrow(()-> new NoSuchElementException("Такого статуса не существует"));
    }


}
