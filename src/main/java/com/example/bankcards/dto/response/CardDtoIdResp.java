package com.example.bankcards.dto.response;

import com.example.bankcards.dto.request.CardDtoReque;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Dto для ответов после работы с картами
 */
@Schema(description = "Dto с id карты пользователя")
public class CardDtoIdResp {
    /** id карты {@link CardDtoIdResp#cardId} */
    @Schema(description = "id карты пользователя")
    @Getter
    private Integer cardId;

    /**
     * Пустой конструктор
     */
    public CardDtoIdResp() {
    }

    /**
     * Конструктор с параметром:
     * @param cardId id карты
     */
    public CardDtoIdResp(Integer cardId) {
        this.cardId = cardId;
    }

}
