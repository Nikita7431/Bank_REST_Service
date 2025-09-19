package com.example.bankcards.service;

import com.example.bankcards.entity.Role;
import com.example.bankcards.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Сервис предоставляющий функицонал по работе с {@link Role}
 */
@Service
public class RoleUserService implements RoleService{

    /**
     * Репозиторий по работе с {@link Role}
     */
    private final RoleRepository roleRepository;

    /**
     * Конструктор с параметрами(зависимости):
     * @param roleRepository репозиторий по работе с {@link Role}
     */
    public RoleUserService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Поиск роли по её названию(пример: USER)
     * @param role название роли
     * @return {@link Role} - роль
     */
    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role)
                .orElseThrow(()-> new NoSuchElementException("Такой роли не существует"));
    }
}
