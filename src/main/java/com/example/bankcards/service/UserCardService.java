package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardDtoReque;
import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.StatusCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CardMaskUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserCardService implements CardService {

    private final CardRepository cardRepository;
    private final StatusCardService statusCardService;
    private final UserService userService;
    private final ModelMapper modelMapper;
//    private final StatusCardRepository statusCardRepository;

    public UserCardService(CardRepository cardRepository, StatusCardService statusCardService, UserService userService, ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.statusCardService = statusCardService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Integer createCard(Integer userid) {
        User user;
        Card card;
        if (userid == null || userid == 0) {
            card = new Card(null,
                    LocalDate.now().plusYears(4),
                    statusCardService.findStatus("ACTIVE"));
        } else {

            try {
                user = userService.findById(userid);
            } catch (NoSuchElementException ex) {
                throw new NoSuchElementException("Пользователь с таким id не найден");
            }
            card = new Card(user,
                    LocalDate.now().plusYears(4),
                    statusCardService.findStatus("ACTIVE"));
        }
        cardRepository.save(card);
        return card.getId();
    }

    @Transactional
    public void transfer(Integer idUser, String numberFirstCard, String numberSecondCard, Double sum) {
        User user = userService.findById(idUser);

        Set<Card> cardsUser = user.getCards();
        Card cardFirst = findCardByNumber(numberFirstCard);
        Card cardSecond = findCardByNumber(numberSecondCard);

        if (cardsUser.contains(cardFirst) && cardsUser.contains(cardSecond)) {
            cardFirst.setBalance(cardFirst.getBalance() - sum);
            cardSecond.setBalance(cardSecond.getBalance() + sum);
        } else {
            throw new NoSuchElementException("У пользователя нет карт с такими номерами, для перевода");
        }
    }

    public void block(Integer cardId, Integer userId) throws NoSuchElementException {
        Card card = findCardById(cardId);
        User user = userService.findById(userId);
        if (card.getUser().getId() == userId && user.getCards().contains(card)) {
            changeCardStatus(card, "BLOCKED");
        }

    }

    @Transactional
    public void changeCardStatus(Card card, StatusCard statusCard) throws NoSuchElementException {
        card.setStatusCard(statusCard);
    }

    @Transactional
    public void changeCardStatus(Card card, String status) {
        StatusCard statusCard = statusCardService.findStatus(status);
        card.setStatusCard(statusCard);
    }

    @Transactional
    public void changeCardStatus(String number, String status) {
        Card card = cardRepository.findByNumber(number).get();
        StatusCard statusCard = statusCardService.findStatus(status);

        card.setStatusCard(statusCard);
    }

    @Transactional
    public void updateCard(Integer cardId, CardDtoReque cardDtoReque) {
        Card card = findCardById(cardId);
        modelMapper.map(cardDtoReque, card);
    }

    @Transactional
    public void delCard(Integer cardId) {
        Card card = findCardById(cardId);
        cardRepository.delete(card);
    }

    @Transactional
    public void updateExpiredCards() {
        List<Card> allUpdatedStatusCards = findCardsByEndDate(LocalDate
                .now()
                .minusDays(1));
        StatusCard statusCard = statusCardService.findStatus("EXPIRED");
        allUpdatedStatusCards.forEach(card -> changeCardStatus(card, statusCard));

        cardRepository.saveAll(allUpdatedStatusCards);
    }

    private List<Card> findCardsByEndDate(LocalDate date) {
        return cardRepository.findAllCardByEndDate(date)
                .orElseThrow(() -> new NoSuchElementException("Карты с такой датой окончания нет"));
    }

    @Override
    public Card findCardById(Integer id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Карты с таким id нет"));
    }

    private Card findCardByNumber(String number) {
        return cardRepository.findByNumber(number)
                .orElseThrow(() -> new NoSuchElementException("Карты с таким номером не существует"));
    }


    public Page<CardDtoResp> getUserCards(Integer userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        User user = userService.findById(userId);
        Page<Card> cardPage = cardRepository.findAllByUser(user, pageRequest);

        return cardPage.map(card -> {
            CardDtoResp dto = modelMapper.map(card, CardDtoResp.class);
            dto.setNumber(CardMaskUtil.maskFromNumber(card.getNumber()));
            return dto;
        });

    }

    public Page<CardDtoResp> getUserCardsForAdmin(Integer adminId, int page, int size) throws RoleNotFoundException {
        PageRequest pageRequest = PageRequest.of(page, size);
        String role = userService.findById(adminId).getRole().getRole();
        if(role == "ADMIN"){
            throw new RoleNotFoundException("Пользователь не обладает нужными правами");
        }

        Page<Card> cardPage = cardRepository.findAll(pageRequest);

        return cardPage.map(card -> {
            CardDtoResp dto = modelMapper.map(card, CardDtoResp.class);
            dto.setNumber(CardMaskUtil.maskFromNumber(card.getNumber()));
            return dto;
        });

    }

}
