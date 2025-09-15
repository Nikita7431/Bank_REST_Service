package com.example.bankcards.entity;

import jakarta.persistence.*;

import java.lang.String;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String number;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column
    private LocalDate endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_card_id", nullable = false)
    private StatusCard statusCard;
    @Column
    private Double balance;

    public Card(User user, LocalDate localDate, StatusCard status) {
    }

    public Card(User user, LocalDate endDate, StatusCard statusCard, Double balance) {
        this.user = user;
        this.endDate = endDate;
        this.statusCard = statusCard;
        this.balance = balance;
    }

    public Card() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public StatusCard getStatusCard() {
        return statusCard;
    }

    public void setStatusCard(StatusCard statusCard) {
        this.statusCard = statusCard;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double sum) {
        this.balance = sum;
    }

}
