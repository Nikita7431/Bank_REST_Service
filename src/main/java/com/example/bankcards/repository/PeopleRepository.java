package com.example.bankcards.repository;


import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<User, Integer> {

    Optional<User>findById(Integer id);

    Optional<User> findByLogin(String login);

    List<User> findAll();

}
