package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель для Role
 */
@Entity
@Getter @Setter
public class Role {
    /** id роли */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    /**Роль (пример: ADMIN)*/
    @Column
    String role;

    /**
     * Конструктор без параметров
     */
    public Role() {
    }

    /**
     * Конструктор с параметрами:
     * @param id id роли
     * @param role роль (пример: ADMIN)
     */
    public Role(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

}
