package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/** * Dto для получения данных карты* */
public class CardDtoReque {
    /** Номер карты {@link CardDtoReque#number} */
    @NotNull(message = "Номер карты не должен быть пустым")
    @Positive
    @Length(max = 16, message = "Не полный номер карты, длина должна быть 16 символов")
    @Getter @Setter
    private String number;
    /** Имя владельца {@link CardDtoReque#nameUser} */
    @NotNull(message = "Имя пользовтеля не должно быть пустым")
    @Length(min = 2, max = 70, message = "Имя должно быть до 70 символов")
    private String nameUser;
    /** Дата истечения срока карты {@link CardDtoReque#endDate} */
    @NotNull(message = "Дата окончания не должна быть пустым")
    private LocalDate endDate;
    /** Статус карты {@link CardDtoReque#status} */
    @NotNull(message = "Статус карты не должен быть пустым")
    @Getter @Setter
    private String status;
    /** Баланс карты {@link CardDtoReque#balance} */
    @NotNull(message = "Баланс карты не должен быть пустым")
    private Double balance;

    /**
     *Конструктор имеет параметры:
     * @param number номер карты
     * @param nameUser имя владельца
     * @param endDate дата истечения срока карты
     * @param status статус карты
     * @param balance баланс карты
     */
    public CardDtoReque(String number, String nameUser,
                        LocalDate endDate, String status, Double balance) {
        this.number = number;
        this.nameUser = nameUser;
        this.endDate = endDate;
        this.status = status;
        this.balance = balance;
    }


}
