package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Обёртка над {@link User} implements {@link UserDetails}, представляющая Spring Security
 * необходимые данные о пользователе
 * <ul>
 * <li>login</li>
 * <li>Пароль</li>
 * <li>Роль пользователя, {@link GrantedAuthority} в данной реализации {@link SimpleGrantedAuthority}</li>
 * </ul>
 */
public class PersonDetails implements UserDetails {
    /**
     * Пользователь
     */
    @Getter
    private final User user;
    /**
     * Роль пользователя
     */
    private final SimpleGrantedAuthority role;

    /**
     * Конструктор с параметрами:
     *
     * @param user пользователь проходящий аутентификацию
     *             <p>также инициализирует role
     */
    public PersonDetails(User user) {
        this.user = user;
        role = new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole());
    }


    /**
     * @return {@code SingletonList} с ролью пользователя
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
