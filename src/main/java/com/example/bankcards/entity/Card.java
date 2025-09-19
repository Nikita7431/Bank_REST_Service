package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.String;
import java.time.LocalDate;

/**
 * Модель для Card
 */
@Entity
@Getter @Setter
public class Card {
    /** id карты */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** Номер карты */
    @Column(nullable = false)
    private String number;
    /** Вледалец карты, cвязь многие к одному по user_id */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /** Дата окончания срока действия карты */
    @Column
    private LocalDate endDate;
    /** Статус карты, связь многие к одному по status_card_id*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_card_id", nullable = false)
    private StatusCard statusCard;
    /** Баланс карты */
    @Column
    private Double balance;

    /**
     * Консруктор с параметрами:
     * @param user владелец карты
     * @param localDate дата окончания срока действия карты
     * @param status статус карты
     */
    public Card(User user, LocalDate localDate, StatusCard status) {
    }

    /**
     * Конструктор с параметрами:
     * @param user владелец карты
     * @param endDate дата окончания срока действия карты
     * @param statusCard статус карты
     * @param balance баланс карты
     */
    public Card(User user, LocalDate endDate, StatusCard statusCard, Double balance) {
        this.user = user;
        this.endDate = endDate;
        this.statusCard = statusCard;
        this.balance = balance;
    }

    /**
     * Конструктор без параметров
     */
    public Card() {

    }


}
