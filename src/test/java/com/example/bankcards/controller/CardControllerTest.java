package com.example.bankcards.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.example.bankcards.dto.request.CardDtoReque;
import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.security.JWTFilter;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.JWTService;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CardController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)

class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CardService cardService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JWTFilter jwtFilter;


    @Test
     void getCards_ReturnPageUserCards() throws Exception {
        Page<CardDtoResp> page = new PageImpl<>(
                List.of(new CardDtoResp("123456781234567812345678",
                        "Nikita",
                        LocalDate.now().plusDays(1),
                        "ACTIVE" ,10000D))
        );
        when(cardService.getUserCards(1, 1, 5)).thenReturn(page);

        mockMvc.perform(get("/users/1")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void createCard_ReturnCreatedCardId_201() throws Exception {
        when(cardService.createCard(1)).thenReturn(2);

        mockMvc.perform(post("/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("2"));
        verify(cardService).createCard(1);
    }
    @Test
    void updateCard_ReturnNoContent_204() throws Exception {
        mockMvc.perform(put("/cards/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "number": "1234567812345678",
                               "nameUser": "Nikita",
                               "endDate": "2030-01-01",
                               "status": "ACTIVE",
                               "balance": 10000.0
                            }
                            """))
                .andExpect(status().isNoContent());

        verify(cardService).updateCard(eq(4), any(CardDtoReque.class));
    }

    @Test
    void deCard_ReturnNoContent_204() throws Exception {
        mockMvc.perform(delete("/cards/4"))
                .andExpect(status().isNoContent());

        verify(cardService).delCard(4);
    }

    @Test
    void blockCard_shouldReturnNoContent_204() throws Exception {
        mockMvc.perform(post("/cards/2/1"))
                .andExpect(status().isNoContent());

        verify(cardService).block(2, 1);
    }

    @Test
    void transfer_ReturnOk_200() throws Exception {
        mockMvc.perform(get("/cards/transfer/1")
                        .param("numberFirstCard", "11111111222222223333333344444444")
                        .param("numberSecondCard", "11111121222222323333334344444454")
                        .param("sum", "1000.0"))
                .andExpect(status().isOk());

        verify(cardService).transfer(1,
                "11111111222222223333333344444444",
                "11111121222222323333334344444454",
                1000.0);
    }
}
