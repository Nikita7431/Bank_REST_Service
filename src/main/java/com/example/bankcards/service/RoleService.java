package com.example.bankcards.service;

import com.example.bankcards.entity.Role;

/**
 * Интерфейс описывающий функционал по работе с {@link Role}
 */
public interface RoleService {
    /**
     * Поиск роли по её названию
     * @param role название роли
     * @return
     */
    public Role findByRole(String role);
}
