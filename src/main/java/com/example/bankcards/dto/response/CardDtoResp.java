package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
@Schema(description = "Сущность карты пользователя")
public class CardDtoResp {
    @Schema(description = "id карты пользователя")
    private Integer id;
    @Schema(description = "Номер карты пользователя")
    private String number;
    @Schema(description = "Имя пользователя")
    private String nameUser;
    @Schema(description = "Дата окончания службы карты пользователя")
    private LocalDate endDate;
    @Schema(description = "Статус карты пользователя")
    private String status;
    @Schema(description = "Баланс карты пользователя")
    private Double balance;

    public CardDtoResp(String number, String nameUser, LocalDate endDate, String status, Double balance) {
        this.number = number;
        this.nameUser = nameUser;
        this.endDate = endDate;
        this.status = status;
        this.balance = balance;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
