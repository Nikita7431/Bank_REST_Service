package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardDtoReque;
import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.dto.response.CardDtoIdResp;
import com.example.bankcards.dto.response.PageCardsDtoResp;
import com.example.bankcards.dto.response.UserDtoResp;
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

/**
 * Контроллер для присущих только администратору функций
 */

@RestController
@Tag(name = "Cards", description = "Операции с картами")
public class CardController {
    private final CardService cardService;

    /**Конструктор контроллера
     * @param cardService сервис для работы с картами
     * */
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**Эндпоинт для получения карт пользователя постранично
     * @param userId id пользователя
     * @param page номер страницы
     * @param size количество элементов на страницы
     * @return {@link ResponseEntity}&lt;{@link PageCardsDtoResp}&gt;
     **/
    @Operation(summary = "Постраничное дозирование пользовательских карт",
            description = "Возвращает страницу карт пользователя")
    @GetMapping("/users/{userId}")
    public ResponseEntity<PageCardsDtoResp> getCards(
            @PathVariable("userId") @NotNull @Positive @Parameter(description = "id пользователя") Integer userId,
            @RequestParam @Parameter(description = "Номер страницы для пагинации") Integer page,
            @RequestParam @Parameter(description = "Размер страницы для пагинации") Integer size ){
        return ResponseEntity.ok(new PageCardsDtoResp(cardService.getUserCards(userId,page,size)));
    }
    /**Эндпоинт для создания карты пользователя
     * @param userId id пользователя, кому карту создаём, в данной реализации может иметь null/0,
     *               карта создастся без пользователя
     * @return {@link ResponseEntity}@lt;{@link CardDtoIdResp}&gt;
     *{@link HttpStatus#CREATED}
     **/
    @PostMapping("/cards/{userId}")
    @Operation(summary = "Создание карты",
            description = "Возвращает id карты")
    public ResponseEntity<CardDtoIdResp> createCard(@PathVariable("userId") @Parameter(description = "id пользователя") Integer userId){

        Integer idCard = cardService.createCard(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CardDtoIdResp(idCard));
    }

    /**Эндпоинт для изменения карты
     * @param cardId id изменяемой карты
     * @param cardDtoReque новые данные
     * @return {@link ResponseEntity}&lt;{@link CardDtoIdResp}&gt; c cardId == 0
     * {@link HttpStatus#NO_CONTENT}
     **/
    @PutMapping("/cards/{cardId}")
    @Operation(summary = "Обновление карты",
            description = "Возвращает статус NO_CONTENT и пустой объект с полем id")
    public ResponseEntity<CardDtoIdResp> updateCard(@PathVariable("cardId") @NotNull @Positive
                                         @Parameter(description = "id карты") Integer cardId,
                                     @RequestBody @Valid @Parameter(description = "данные карты") CardDtoReque cardDtoReque){
        cardService.updateCard(cardId,cardDtoReque);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**Эндпоинт для удаления карты
     * @param cardId id изменяемой карты
     * @return {@link ResponseEntity}&lt;{@link CardDtoIdResp}&gt; c cardId == 0,
     * {@link HttpStatus#NO_CONTENT}
     **/
    @Operation(summary = "Удаление карты",
            description = "Возвращает статус NO_CONTENT и пустой объект с полем id")
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<CardDtoIdResp> delCard(@PathVariable("cardId")
                                      @Parameter(description = "id карты") Integer cardId){
        cardService.delCard(cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new CardDtoIdResp());
    }
    /**Эндпоинт для блокоровки карты
     * @param cardId id изменяемой карты
     * @param userId id пользователя,чью карту блокируем
     * @return {@link ResponseEntity}&lt;{@link CardDtoIdResp}&gt; c cardId == 0,
     * {@link HttpStatus#NO_CONTENT}
     **/
    @Operation(summary = "Блокировка карты",
            description = "Возвращает статус NO_CONTENT")
    @PostMapping("/cards/{cardId}/{userid}")
    public ResponseEntity<CardDtoIdResp> blockCard(@PathVariable("cardId") @NotNull @Positive
                                        @Parameter(description = "id карты") Integer cardId,
                                    @PathVariable("userId") @NotNull @Positive
                                        @Parameter(description = "id пользователя") Integer userId){
        cardService.block(cardId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new CardDtoIdResp());
    }
    /**Эндпоинт для удаления карты
     * @param userId id изменяемой карты
     * @param numberFirstCard номерь карты, с которой осуществляется перевод
     * @param numberSecondCard номер карты, на которую осуществляется перевод
     * @param sum размер переводимых средств
     * @return {@link ResponseEntity}&lt;{@link CardDtoIdResp}&gt; c cardId == 0,
     * {@link HttpStatus#NO_CONTENT}
     **/
    @Operation(summary = "Перевод между картами пользователя",
            description = "Возвращает статус 200 и пустой объект с полем id")
    @GetMapping("cards/transfer/{userId}")
    public ResponseEntity<CardDtoIdResp> transfer(@PathVariable("userId")
                                       @Parameter(description = "id пользователя") Integer userId,
                                   @Parameter(description = "номер карты с которой переводят") String numberFirstCard,
                                   @Parameter(description = "номер карты на которую переводят") String numberSecondCard,
                                   @Parameter(description = "размер переводимых средств") Double sum){
        cardService.transfer(userId, numberFirstCard, numberSecondCard, sum);
        return ResponseEntity.ok().body(new CardDtoIdResp());
    }
}
