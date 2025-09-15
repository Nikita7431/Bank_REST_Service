package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public class CardDtoReque {
    @NotNull(message = "Номер карты не должен быть пустым")
    @Positive
    @Length(max = 16, message = "Не полный номер карты, длина должна быть 16 символов")
    private String number;
    @NotNull(message = "Имя пользовтеля не должно быть пустым")
    @Length(min = 2, max = 70, message = "Имя должно быть до 70 символов")
    private String nameUser;
    @NotNull(message = "Дата окончания не должна быть пустым")
    private LocalDate endDate;
    @NotNull(message = "Статус карты не должен быть пустым")
    private String status;
    @NotNull(message = "Баланс карты не должен быть пустым")
    private Double balance;

    public CardDtoReque(String number, String nameUser, LocalDate endDate, String status, Double balance) {
        this.number = number;
        this.nameUser = nameUser;
        this.endDate = endDate;
        this.status = status;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
