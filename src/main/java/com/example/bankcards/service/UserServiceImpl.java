package com.example.bankcards.service;


import com.example.bankcards.entity.User;
import com.example.bankcards.repository.PeopleRepository;
import com.example.bankcards.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user =  peopleRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Пользователь отсутсвует"));

        return new PersonDetails(user);
    }

    public boolean existsUser(String login, String password){
        if(login == null || login.isBlank() || password == null || password.isBlank()){
            return false;
        }
            try {
                User findedUser = peopleRepository.findByLogin(login)
                        .orElseThrow(()-> new NoSuchElementException());
                return passwordEncoder.matches(password, findedUser.getPassword());
            } catch (NoSuchElementException e) {
                return false;
            }

    }

    public List<User> findAll(){
        return peopleRepository.findAll();
    }

    @Override
    public User findById(Integer id){
        Objects.requireNonNull(id, "Id не должно быть пустым");
        Optional<User> userOpt = Optional.of(peopleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("пользователя с таким id не существует")));
        return userOpt.get();
    }
}
