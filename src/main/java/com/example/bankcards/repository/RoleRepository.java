package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий по работе с Role
 */
public interface RoleRepository extends JpaRepository<Role,Integer> {
    /**
     * Ищет роле по её названию(например: USER)
     * @param role название роли
     * @return {@link Optional}&lt;{@link Role}&gt;
     */
    Optional<Role> findByRole(String role);
}
