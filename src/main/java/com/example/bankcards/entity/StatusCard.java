package com.example.bankcards.entity;

import jakarta.persistence.*;

@Entity
public class StatusCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String status;

    public StatusCard() {
    }

    public StatusCard(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
