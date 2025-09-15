package com.example.bankcards.service;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.PeopleRepository;
import com.example.bankcards.security.PersonDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private PeopleRepository peopleRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void beforeEachMethod() {
        peopleRepository = mock(PeopleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(peopleRepository, passwordEncoder);
    }

    @Test
    void loadUserByUsername_UserIsExistis_ReturnPersonDetails() {
        User user = new User();
        user.setLogin("login");
        user.setRole(new Role(1, "ADMIN"));
        when(peopleRepository.findByLogin("login")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("login");
        assertNotNull(result);
        assertEquals("login", ((PersonDetails) result).getUser().getLogin());
    }

    @Test
    void loadUserByUsername_UserIsNotExists_UsernameNotFoundException() {
        when(peopleRepository.findByLogin("login")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("login"));
    }

    @Test
    void existsUser_LoginAndPasswodIsGoodIsBlank_ReturnTrue() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("ePass");

        when(peopleRepository.findByLogin("login")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "ePass")).thenReturn(true);

        boolean result = userService.existsUser("login", "password");
        assertTrue(result);

        verify(peopleRepository).findByLogin("login");
        verify(passwordEncoder).matches("password", "ePass");
    }

    @Test
    void existsUser_PasswordIsBlankButLogindNonBlank_ReturnFals() {
        boolean result = userService.existsUser("", "password");
        assertFalse(result);
        verifyNoInteractions(peopleRepository, passwordEncoder);
    }

    @Test
    void existsUser_UserNotFound_ReturnFalse() {
        when(peopleRepository.findByLogin("login")).thenReturn(Optional.empty());

        boolean result = userService.existsUser("login", "password");
        assertFalse(result);
    }

    @Test
    void existsUser_PasswordNonMatch_ReturnFalse() {
        User user = new User();
        user.setPassword("ePass");

        when(peopleRepository.findByLogin("login")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "ePass")).thenReturn(false);

        boolean result = userService.existsUser("login", "password");
        assertFalse(result);
    }

    @Test
    void findAll_ReturnUsersList() {
        List<User> users = List.of(new User(), new User());

        when(peopleRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();
        assertEquals(2, result.size());
        verify(peopleRepository).findAll();
    }
    @Test
    void findById_UserExists_ReturnUser() {
        User user = new User();
        user.setId(4);

        when(peopleRepository.findById(4)).thenReturn(Optional.of(user));

        User result = userService.findById(4);
        assertNotNull(result);
        assertEquals(4, result.getId());
    }

    @Test
    void findById_UserNotExists_NoSuchElementException() {
        when(peopleRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findById(4));
    }

    @Test
    void findById_IdIsNull_NullPointerException() {
        assertThrows(NullPointerException.class, () -> userService.findById(null));
    }
}
