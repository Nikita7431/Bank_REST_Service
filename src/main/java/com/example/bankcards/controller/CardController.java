package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardDtoReque;
import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Cards", description = "Операции с картами")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    @Operation(summary = "Постраничное дозирование пользовательских карт",
            description = "Возвращает страницу карт пользователя")
    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<CardDtoResp>> getCards(
            @PathVariable("userId") @NotNull @Positive @Parameter(description = "id пользователя") Integer userId,
            @RequestParam @Parameter(description = "Номер страницы для пагинации") Integer page,
            @RequestParam @Parameter(description = "Размер страницы для пагинации") Integer size ){
        return ResponseEntity.ok(cardService.getUserCards(userId,page,size));
    }

    @PostMapping("/cards/{userId}")
    @Operation(summary = "Создание карты",
            description = "Возвращает id карты")
    public ResponseEntity createCard(@PathVariable("userId") @Parameter(description = "id пользователя") Integer userId){

        Integer idCard = cardService.createCard(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(idCard.toString());
    }

    @PutMapping("/cards/{cardId}")
    @Operation(summary = "Обновление карты",
            description = "Возвращает только статус NO_CONTENT")
    public ResponseEntity updateCard(@PathVariable("cardId") @NotNull @Positive
                                         @Parameter(description = "id карты") Integer cardId,
                                     @RequestBody @Valid @Parameter(description = "данные карты") CardDtoReque cardDtoReque){
        cardService.updateCard(cardId,cardDtoReque);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Удаление карты",
            description = "Возвращает только статус NO_CONTENT")
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity delCard(@PathVariable("cardId")
                                      @Parameter(description = "id карты") Integer cardId){
        cardService.delCard(cardId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Блокировка карты",
            description = "Возвращает только статус NO_CONTENT")
    @PostMapping("/cards/{cardId}/{userid}")
    public ResponseEntity blockCard(@PathVariable("cardId") @NotNull @Positive
                                        @Parameter(description = "id карты") Integer cardId,
                                    @PathVariable("userId") @NotNull @Positive
                                        @Parameter(description = "id пользователя") Integer userId){
        cardService.block(cardId, userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Перевод между картами пользователя",
            description = "Возвращает только статус 200")
    @GetMapping("cards/transfer/{userId}")
    public ResponseEntity transfer(@PathVariable("userId")
                                       @Parameter(description = "id пользователя") Integer userId,
                                   @Parameter(description = "номер карты с которой переводят") String numberFirstCard,
                                   @Parameter(description = "номер карты на которую переводят") String numberSecondCard,
                                   @Parameter(description = "размер переводимых средств") Double sum){
        cardService.transfer(userId, numberFirstCard, numberSecondCard, sum);
        return new ResponseEntity(HttpStatus.OK);
    }
}
