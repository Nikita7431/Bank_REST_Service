package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Dto для ответов с полными данными карты
 */
@Schema(description = "Сущность карты пользователя")
@Getter
@Setter
public class CardDtoResp {
    /** id карты */
    @Schema(description = "id карты пользователя")
    private Integer id;
    @Schema(description = "Номер карты пользователя")
    /** Номер карты */
    private String number;
    @Schema(description = "Имя пользователя")
    /** Имя владельца карты */
    private String nameUser;
    /** Дата окончания срока действия карты */
    @Schema(description = "Дата окончания службы карты пользователя")
    private LocalDate endDate;
    @Schema(description = "Статус карты пользователя")
    /** Статус карты */
    private String status;
    /** Баланс карты */
    @Schema(description = "Баланс карты пользователя")
    private Double balance;

    /**
     * Конструктор с параметрами:
     * @param number номер карты
     * @param nameUser имя владельца
     * @param endDate дата окончания срока действия карты
     * @param status статус карты
     * @param balance баланс карты
     */
    public CardDtoResp(String number, String nameUser, LocalDate endDate, String status, Double balance) {
        this.number = number;
        this.nameUser = nameUser;
        this.endDate = endDate;
        this.status = status;
        this.balance = balance;
    }


}
