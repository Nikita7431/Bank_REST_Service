package com.example.bankcards.controller;

import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.dto.response.UserDtoResp;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

/**
 * Контроллер для присущих только администратору функций
 */

@RestController
public class AdminController {

    private final UserService userService;
    private final CardService cardService;

    /**Конструктор контроллера
     * @param userService сервис для работы с пользователями
     * @param cardService сервис для работы с картами
     * */
    public AdminController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }
    /**Эндпоинт для получения списка пользователей и их карт
     * @return {@link ResponseEntity}&lt;{@link UserDtoResp}&gt;
     */
    @Operation(summary = "Получение администратором всех пользователей и их карт",
            description = "Возвращает List с картами")
    @GetMapping("/admin/")
    public ResponseEntity<List<UserDtoResp>> allUsersWithCards(){ //TODO можно пагинацию
        return ResponseEntity.ok(userService.findAll());
    }
    /**Эндпоинт для получения администратором списка всех карт пользователей постранично
     * @param adminId id администратора
     * @param page номер страницы
     * @param size число объектов на странице
     * @return {@link ResponseEntity}&lt;{@link CardDtoResp}&gt;
     *
     **/
    @Operation(summary = "Получение администратором всех карт пользователей",
            description = "Возвращает Page с картами")
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<Page<CardDtoResp>> allCards(@PathVariable Integer adminId,
                                                      int page, int size) {
        return ResponseEntity.ok(cardService.getUserCardsForAdmin(adminId, page,size));
    }
}
