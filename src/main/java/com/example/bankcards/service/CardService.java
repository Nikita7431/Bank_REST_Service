package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardDtoReque;
import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;

import javax.management.relation.RoleNotFoundException;
/**Данный интерфейс представляет функционал по работе с {@link Card} */
public interface CardService {
    /**Обновляет статус карты по истечению её срока действия */
    public void updateExpiredCards();

    /**
     * Блокирует карту
     * @param cardId id карты
     * @param userId id пользователя у которого блокирует карту
     */
    public void block(Integer cardId, Integer userId);

    /**
     * Создаёт карту
     * @param userId id пользователь для создания карты
     *               (может быть null/0, если необходимо создать без указания владельца)
     * @return {@code cardId} id созданной карты
     */
    public Integer createCard(Integer userId);

    /**
     * Обновляет данные карты
     * @param cardId id карты
     * @param cardDtoReque - данные карты от пользователя
     */
    public void updateCard(Integer cardId, CardDtoReque cardDtoReque);

    /**
     * Удаляет карту по id
     * @param cardId id карты
     */
    public void delCard(Integer cardId);

    /**
     * Осуществляет перевод с между картами пользователя
     * @param idUser id пользователя
     * @param numberFirstCard номер карты пользователя c {@code idUser}, с которой осуществляется перевод
     * @param numberSecondCard номер карты пользователя c {@code idUser}, на которую осуществляется перевод
     * @param sum сумма перевода
     */
    public void transfer(Integer idUser, String numberFirstCard, String numberSecondCard, Double sum);

    /**
     * Поиск карты по id
     * @param id id карты
     * @return {@link Card}
     */
    public Card findCardById(Integer id);

    /**
     * Поиск карт пользователя, с постраничным предоставлением (пагинация)
     * @param userId id пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return {@link Page}&lt;{@link CardDtoResp}&gt;
     */
    public Page<CardDtoResp> getUserCards(Integer userId, int page, int size);

    /**
     * Поиск карт пользователей для администратора, с постраничным предоставлением (пагинация)
     * @param adminId id администратора
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return {@link Page}&lt;{@link CardDtoResp}&gt;
     */
    public Page<CardDtoResp> getUserCardsForAdmin(Integer adminId, int page, int size);
}
