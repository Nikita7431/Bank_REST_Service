package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.String;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель для User
 */
@Entity
@Getter @Setter
@Table(name = "users")
public class User {
    /** id пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** имя пользователя */
    @Column(name = "name")
    private String name;
    /** login пользователя */
    @Column(name = "login", unique = true)
    private String login;
    /** пароль пользователя */
    @Column(name = "password")
    private java.lang.String password;
    /** роль пользователя, связь один к одному*/
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
    /** карты пользователя, связ один ко многим*/
    @Column
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)//    @JoinTable(
    private Set<Card> cards = new HashSet<>();




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

    /**
     * Конструктор без параметров
     */
    public User(){}

    /**
     * Конструктор с параметрами:
     * @param name имя пользователя
     * @param password пароль пользователя
     * @param login login пользователя
     */
    public User(String name, String password, String login){
        this.password = password;
        this.login = login;
//        this.email = email;
//        this.regAt = Date.from(ZonedDateTime.now().toInstant());
    }






}
