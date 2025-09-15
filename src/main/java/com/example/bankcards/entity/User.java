package com.example.bankcards.entity;

import jakarta.persistence.*;

import java.lang.String;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    private java.lang.String password;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)//    @JoinTable(
    private Set<Card> cards = new HashSet<>();

    public void setId(Integer id) {
        this.id = id;
    }
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }


    //
//    public Set<String> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<String> roles) {
//        this.roles = roles;
//    }
//
//    @Column(name = "string")
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "string",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<String> roles = new HashSet<>();
//    @Column(name = "udatedAt")
//    private Date updatedAt;
//
//    @Column(name = "regAt")
//    private Date regAt;
//
//    public Date getRegAt() {
//        return regAt;
//    }
//
//
//    public Date getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(Date updatedAt) {
//        this.updatedAt = updatedAt;
//    }

    public User(){}

    public User(java.lang.String password) {
        this(password,null,null);
    }

    public User(java.lang.String name, java.lang.String password, java.lang.String email){
        this.password = password;
        this.login = login;
//        this.email = email;
//        this.regAt = Date.from(ZonedDateTime.now().toInstant());
    }

    public int getId() {
        return id;
    }

    public java.lang.String getLogin() {
        return login;
    }

    public void setLogin(java.lang.String login) {
        this.login = login;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }
}
