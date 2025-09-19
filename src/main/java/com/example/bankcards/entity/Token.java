package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель для Token - токены пользователей
 */
@Entity
@Getter @Setter
public class Token {
    /** id токена*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    /** токен*/
    @Column(unique = true)
    private String token;


}
