package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Dto для User с полными данными карты
 */
@Schema(description = "Сущность пользователя")
@Getter @Setter
public class UserDtoResp {
    /** id пользователя */
    @Schema(description = "id пользователя")
    private Integer id;
    /** имя пользователя пользователя */
    @Schema(description = "имя пользователя")
    private String name;
    /** роль пользователя пользователя для авторизации*/
    @Schema(description = "role пользователя")
    private String role;
    /** Карты пользователя пользователя */
    @Schema(description = "карты пользователя")
    private Set<CardDtoResp> cards;

    /**
     * Конструктор с параметрами:
     * @param name имя пользователя
     * @param role роль пользователя
     * @param cards карты пользователя
     */
    public UserDtoResp(String name, String role, Set<CardDtoResp> cards) {
        this.name = name;
        this.role = role;
        this.cards = cards;
    }

    /**
     * Конструктор с параметрами:
     * @param id id пользователя
     * @param name имя пользователя
     * @param role роль пользователя
     * @param cards карты пользователя
     */
    public UserDtoResp(Integer id, String name, String role, Set<CardDtoResp> cards) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.cards = cards;
    }

}
