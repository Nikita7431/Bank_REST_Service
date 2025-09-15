package com.example.bankcards.controller;

import com.example.bankcards.dto.response.CardDtoResp;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;


@RestController
public class AdminController {

    private final UserService userService;
    private final CardService cardService;

    public AdminController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }
    @Operation(summary = "Получение администратором всех пользователей и их карт",
            description = "Возвращает List с картами")
    @GetMapping("/admin/")
    public ResponseEntity allUsersWithCards(){
        return ResponseEntity.ok(userService.findAll());
    }
    @Operation(summary = "Получение администратором всех карт пользователей",
            description = "Возвращает Page с картами")
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<Page<CardDtoResp>> allCards(@PathVariable Integer adminId,
                                                      int page, int size) {
        try {
            return ResponseEntity.ok(cardService.getUserCardsForAdmin(adminId, page,size));
        }catch(RoleNotFoundException ex){
            throw new IllegalArgumentException(ex.getMessage()); //TODO Сделать кастомные Exception
        }
    }
}
