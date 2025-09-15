package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Сущность пользователя")
public class UserDtoResp {
    @Schema(description = "id пользователя")
    private Integer id;
    @Schema(description = "имя пользователя")
    private String name;
    @Schema(description = "role пользователя")
    private String role;
    @Schema(description = "карты пользователя")
    private Set<CardDtoResp> cards;

    public UserDtoResp(String name, String role, Set<CardDtoResp> cards) {
        this.name = name;
        this.role = role;
        this.cards = cards;
    }

    public UserDtoResp(Integer id, String name, String role, Set<CardDtoResp> cards) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.cards = cards;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public java.lang.String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String string) {
        this.role = string;
    }

    public Set<CardDtoResp> getCards() {
        return cards;
    }

    public void setCards(Set<CardDtoResp> cards) {
        this.cards = cards;
    }
}
