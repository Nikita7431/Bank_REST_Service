package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

/**
 *Dto для листа - страницы с картами для (Pagination)
 */
@Schema(description = "Сущность для Page из Card (Пагинация)")
public class PageCardsDtoResp {
    @Schema(description = "Page из card")
    private Page<CardDtoResp> cardDtoResps;

    /**
     * Конструктор без параметров
     */
    public PageCardsDtoResp() {
    }

    /**
     * Конструктор с параметром:
     * @param cardDtoResps {@link CardDtoResp} - dto для Card со всеми полями
     */
    public PageCardsDtoResp(Page<CardDtoResp> cardDtoResps) {
        this.cardDtoResps = cardDtoResps;
    }

}
