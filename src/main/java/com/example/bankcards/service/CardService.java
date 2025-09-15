package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardDtoReque;
import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;

import javax.management.relation.RoleNotFoundException;

public interface CardService {
    public void updateExpiredCards();
    public void block(Integer cardId, Integer userId);
    public Integer createCard(Integer userId);
    public void updateCard(Integer cardId, CardDtoReque cardDtoReque);
    public void delCard(Integer cardId);
    public void transfer(Integer idUser, String numberFirstCard, String numberSecondCard, Double sum);
    public Card findCardById(Integer id);
    public Page<CardDtoResp> getUserCards(Integer userId, int page, int size);
    public Page<CardDtoResp> getUserCardsForAdmin(Integer adminId, int page, int size) throws RoleNotFoundException;
}
