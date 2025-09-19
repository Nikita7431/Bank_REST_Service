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

/**
 * Данный сервис реализует функционал по работе с {@link Card}
 */
@Service
public class UserCardService implements CardService {
    /**
     * Репозиторий для работы с {@link Card}
     */
    private final CardRepository cardRepository;
    /**
     * Сервис для работы с {@link StatusCard}
     */
    private final StatusCardService statusCardService;
    /**
     * Сервис для работы с {@link User}
     */
    private final UserService userService;
    /**
     * Преобразует объекты
     */
    private final ModelMapper modelMapper;
//    private final StatusCardRepository statusCardRepository;

    /**
     * Конструктор с параметрами(зависимости):
     *
     * @param cardRepository    репозиторий для работы с {@link Card}
     * @param statusCardService сервис для работы с {@link StatusCard}
     * @param userService       сервис для работы с {@link User}
     * @param modelMapper       преобразует объекты
     */
    public UserCardService(CardRepository cardRepository, StatusCardService statusCardService, UserService userService, ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.statusCardService = statusCardService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Создаёт карту пользователю по его id
     * Если id == null/0, карта не привязывается ни к какому-либол пользователю
     *
     * @param userid id пользователь для создания карты
     *               (может быть null/0, если необходимо создать без указания владельца)
     * @return {@code cardId} созданной карты
     */
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

    /**
     * Осуществляет перевод с одной карты пользователя на другую
     *
     * @param idUser           id пользователя
     * @param numberFirstCard  номер карты пользователя c {@code idUser}, с которой осуществляется перевод
     * @param numberSecondCard номер карты пользователя c {@code idUser}, на которую осуществляется перевод
     * @param sum сумма перевода
     */
    @Transactional
    public void transfer(Integer idUser, String numberFirstCard, String numberSecondCard, Double sum) {
        User user = userService.findById(idUser);

        Set<Card> cardsUser = user.getCards();
        Card cardFirst = findCardByNumber(numberFirstCard);
        Card cardSecond = findCardByNumber(numberSecondCard);

        if (cardsUser.contains(cardFirst) && cardsUser.contains(cardSecond)) {
            if (cardFirst.getStatusCard().getStatus() != "BLOCKED" && cardSecond.getStatusCard().getStatus() != "BLOCKED") {
                if (cardSecond.getStatusCard().getStatus() != "EXPIRED") {
                    cardFirst.setBalance(cardFirst.getBalance() - sum);
                    cardSecond.setBalance(cardSecond.getBalance() + sum);
                }
            }
        } else {
            throw new NoSuchElementException("У пользователя нет карт с такими номерами, для перевода");
        }
    }

    /**
     * Осуществляет блокировку карты пользователя по
     *
     * @param cardId id карты
     * @param userId id пользователя у которого блокирует карту
     * @throws NoSuchElementException исключение ненайденной карты
     */
    public void block(Integer cardId, Integer userId) throws NoSuchElementException {
        Card card = findCardById(cardId);
        User user = userService.findById(userId);
        if (card.getUser().getId() == userId && user.getCards().contains(card)) {
            changeCardStatus(card, "BLOCKED");
        }

    }

    /**
     * Изменяет статус карты
     *
     * @param card карта
     * @param statusCard новый статус
     * @throws NoSuchElementException исключение ненайденной карты
     */
    @Transactional
    public void changeCardStatus(Card card, StatusCard statusCard) throws NoSuchElementException {
        card.setStatusCard(statusCard);
    }

    /**
     * Изменяет статус карты
     * @param card карта
     * @param status новый статус
     */
    @Transactional
    public void changeCardStatus(Card card, String status) {
        StatusCard statusCard = statusCardService.findStatus(status);
        card.setStatusCard(statusCard);
    }

    /**
     * Обновляет данные карты
     * @param cardId id карты
     * @param cardDtoReque - данные карты от пользователя
     */
    @Transactional
    public void updateCard(Integer cardId, CardDtoReque cardDtoReque) {
        Card card = findCardById(cardId);
        modelMapper.map(cardDtoReque, card);
    }

    /**
     * Удаляет карту
     * @param cardId id карты
     */
    @Transactional
    public void delCard(Integer cardId) {
        Card card = findCardById(cardId);
        cardRepository.delete(card);
    }

    /**
     *Обновляет статус карты на EXPIRED, для карт с истекшим сроком дейтсвия
     */
    @Transactional
    public void updateExpiredCards() {
        List<Card> allUpdatedStatusCards = findCardsByEndDate(LocalDate
                .now()
                .minusDays(1));
        StatusCard statusCard = statusCardService.findStatus("EXPIRED");
        allUpdatedStatusCards.forEach(card -> changeCardStatus(card, statusCard));

        cardRepository.saveAll(allUpdatedStatusCards);
    }

    /**
     * Ищет карты по дате истечения
     * @param date дата
     * @return {@link List}&lt;{@link Card}&gt;
     */
    private List<Card> findCardsByEndDate(LocalDate date) {
        return cardRepository.findAllCardByEndDate(date)
                .orElseThrow(() -> new NoSuchElementException("Карты с такой датой окончания нет"));
    }

    /**
     * Ищет карту по id
     * @param id id карты
     * @return {@link Card}
     */
    @Override
    public Card findCardById(Integer id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Карты с таким id нет"));
    }

    /**
     * Ищет карту по номеру
     * @param number номер карты
     * @return {@link Card}
     */
    private Card findCardByNumber(String number) {
        return cardRepository.findByNumber(number)
                .orElseThrow(() -> new NoSuchElementException("Карты с таким номером не существует"));
    }

    /**
     * Предоставляет карты пользователя постранично(пагинация)
     * @param userId id пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return {@link Page}&lt;{@link CardDtoResp}&gt;
     */
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

    /**
     * Предоставляет карты пользователей для администратора по странично(пагинация)
     * @param adminId id администратора
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return {@link Page}&lt;{@link CardDtoResp}&gt;
     */
    public Page<CardDtoResp> getUserCardsForAdmin(Integer adminId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Card> cardPage = cardRepository.findAll(pageRequest);

        return cardPage.map(card -> {
            CardDtoResp dto = modelMapper.map(card, CardDtoResp.class);
            dto.setNumber(CardMaskUtil.maskFromNumber(card.getNumber()));
            return dto;
        });

    }

}
