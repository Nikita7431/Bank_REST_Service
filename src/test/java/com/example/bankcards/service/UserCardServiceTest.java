package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardDtoReque;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.StatusCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCardServiceTest {

    private CardRepository cardRepository;
    private StatusCardService statusCardService;
    private UserService userService;
    private ModelMapper modelMapper;
    private UserCardService userCardService;

    @BeforeEach
    void beforeEachMethod() {
        cardRepository = mock(CardRepository.class);
        statusCardService = mock(StatusCardService.class);
        userService = mock(UserService.class);
        modelMapper = mock(ModelMapper.class);

        userCardService = new UserCardService(cardRepository, statusCardService, userService, modelMapper);
    }

    @Test
    void createCard_User_IdNull_ResultCreatedCardNonUser() {
        StatusCard activeStatus = new StatusCard();
        when(statusCardService.findStatus("ACTIVE")).thenReturn(activeStatus);

        Integer id = userCardService.createCard(null);

        assertNull(id); // id не установлен до save
        verify(cardRepository).save(any(Card.class));
        verify(statusCardService).findStatus("ACTIVE");
    }

    @Test
    void createCard_User_IdisValid_ResultCreatedCardNonUser() {
        User user = new User();
        user.setId(1);

        StatusCard statusCardActive = new StatusCard();
        when(userService.findById(1)).thenReturn(user);
        when(statusCardService.findStatus("ACTIVE")).thenReturn(statusCardActive);

        Integer id = userCardService.createCard(1);

        assertNull(id);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void createCard_User_IdNotValid_NoSuchElementException() {
        when(userService.findById(4)).thenThrow(new NoSuchElementException("Пользователь с таким id не найден"));

        assertThrows(NoSuchElementException.class, () -> userCardService.createCard(4));
    }

    @Test
    void transfer_Cards_ResultTransferMoney() {
        User user = new User();
        user.setId(1);

        Card first = new Card();
        first.setBalance(100.0);
        Card second = new Card();
        second.setBalance(50.0);

        user.setCards(new HashSet<>(Arrays.asList(first, second)));

        when(userService.findById(1)).thenReturn(user);
        when(cardRepository.findByNumber("1111222233334444")).thenReturn(Optional.of(first));
        when(cardRepository.findByNumber("4444333322221111")).thenReturn(Optional.of(second));

        userCardService.transfer(1, "1111222233334444", "4444333322221111", 30.0);

        assertEquals(70.0, first.getBalance());
        assertEquals(80.0, second.getBalance());
    }

    @Test
    void transfer_UserNotThisCards_NoSuchElementException() {
        User user = new User();
        user.setId(1);
        user.setCards(new HashSet<>());
        Card first = new Card();
        Card second = new Card();

        when(userService.findById(1)).thenReturn(user);
        when(cardRepository.findByNumber("1111222233334444")).thenReturn(Optional.of(first));
        when(cardRepository.findByNumber("2222")).thenReturn(Optional.of(second));

        assertThrows(NoSuchElementException.class, () -> userCardService.transfer(1,
                "1111222233334444", "4444333322221111", 30.0));
    }

    @Test
    void block_Card_ResultNewStatusBLOCKED() {
        User user = new User();
        user.setId(1);

        Card card = new Card();
        card.setId(4);
        card.setUser(user);
        user.setCards(Set.of(card));
        StatusCard blocked = new StatusCard(1, "BLOCKED");

        when(cardRepository.findById(4)).thenReturn(Optional.of(card));
        when(userService.findById(1)).thenReturn(user);
        when(statusCardService.findStatus("BLOCKED")).thenReturn(blocked);

        userCardService.block(4, 1);
        assertEquals("BLOCKED", card.getStatusCard().getStatus());
    }

    @Test
    void block_UserNotIsCard_NoSuchElementException() {
        User user = new User();
        user.setId(1);
        user.setCards(Set.of());

        Card card = new Card();
        card.setId(4);
        card.setUser(user);

        when(cardRepository.findById(4)).thenReturn(Optional.of(card));
        when(userService.findById(1)).thenReturn(user);

        assertDoesNotThrow(() -> userCardService.block(4, 1));
        assertNull(card.getStatusCard());
    }

    @Test
    void updateCard_ResultUpdateCar() {
        Card card = new Card();
        card.setId(4);

        CardDtoReque dto = new CardDtoReque("String number",
                "String nameUser", LocalDate.now().plusDays(1),
                "String status", 41D);

        when(cardRepository.findById(4)).thenReturn(Optional.of(card));
        userCardService.updateCard(4, dto);
        verify(modelMapper).map(dto, card);
    }

    @Test
    void updateCard_CardIsNot_NoSuchElementException() {
        when(cardRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userCardService.updateCard(4,
                new CardDtoReque("String number",
                "String nameUser", LocalDate.now().plusDays(1),
                "String status", 41D)));
    }
}

