package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    public User findById(Integer id);
    public boolean existsUser(String login, String password);
    public List<User> findAll();
    }
