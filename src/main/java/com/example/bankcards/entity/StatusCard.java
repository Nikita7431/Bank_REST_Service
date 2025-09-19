package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * Модель для StatusCard
 */
@Entity
public class StatusCard {
    /** id статуса */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    /** статус (пример: ACTIVE)*/
    @Column
    @Getter
    private String status;

    /**
     * Конструктор без параметров
     */
    public StatusCard() {
    }

    /**
     * Конструктор с параметрами:
     * @param id id статуса
     * @param status статус (пример: ACTIVE)
     */
    public StatusCard(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

}
