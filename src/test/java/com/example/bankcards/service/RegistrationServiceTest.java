package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.dto.response.ResponseTokens;
import com.example.bankcards.entity.Token;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.PeopleRepository;
import com.example.bankcards.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.interfaces.RSAPublicKey;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private PeopleRepository peopleRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private JWTService jwtService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RegistrationService registrationService;

    private UserDtoReque userDto;
    private ResponseTokens tokens;

    @BeforeEach
    void beforeEachMethod() {
        userDto = new UserDtoReque("login" , "password");
        tokens = new ResponseTokens("accessToken", "refreshToken");
    }

    @Test
    void register_User_Success() {
        when(userService.existsUser("login", "password")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPass");
        when(modelMapper.map(userDto, User.class)).thenReturn(new User());

        registrationService.register(userDto);

        verify(peopleRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_UserAlreadyExists_IllegalArgumentException() {
        when(userService.existsUser("login", "password")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> registrationService.register(userDto));
        verify(peopleRepository, never()).save(any());
    }

    @Test
    void register_NullUser_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> registrationService.register(null));
    }

    @Test
    void login_User_ReturnTokens() throws Exception {
        when(userService.existsUser("login", "password")).thenReturn(true);
        when(jwtService.generateTokens(userDto)).thenReturn(tokens);

        ResponseTokens result = registrationService.login(userDto);

        assertEquals(tokens, result);
    }

    @Test
    void login_User_Invalid_IllegalArgumentException() {
        when(userService.existsUser("login", "password")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> registrationService.login(userDto));
    }

    @Test
    void login_User_Null_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> registrationService.login(null));
    }

    @Test
    void loginByRefresh_Token_ReturnTokens() throws Exception{
        String refresh = "refreshToken";
        UserDtoReque userDtoReque = new UserDtoReque("login", "password");

        Token tokenEntity = new Token();
        tokenEntity.setToken(refresh);

        when(jwtService.validateTokenAndSyncClaim(refresh)).thenReturn(userDtoReque);
        when(tokenRepository.findByToken(refresh)).thenReturn(Optional.of(tokenEntity));
        when(userService.existsUser("login", "password")).thenReturn(true);
        when(jwtService.generateTokens(userDtoReque)).thenReturn(tokens);

        ResponseTokens result = registrationService.loginByRefresh(refresh);

        assertEquals(tokens, result);
        assertEquals(tokens.getRefreshToken(), tokenEntity.getToken());
    }

    @Test
    void loginByRefresh_Token_Empty_NullPointerException() {
        assertThrows(NullPointerException.class, () -> registrationService.loginByRefresh(""));
    }

    @Test
    void loginByRefresh_Token_NotFound_IllegalArgumentException() {
        when(jwtService.validateTokenAndSyncClaim("refresh")).thenReturn(userDto);
        when(tokenRepository.findByToken("refresh")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> registrationService.loginByRefresh("refresh"));
    }

    @Test
    void deleteRefresh_Token() {
        Token tokenEntity = new Token();
        tokenEntity.setToken("refresh");

        when(tokenRepository.findByToken("refresh")).thenReturn(Optional.of(tokenEntity));

        registrationService.deleteRefresh("refresh");

        verify(tokenRepository, times(1)).delete(tokenEntity);
    }

    @Test
    void deleteRefresh_Token_Empty_NullPointerException() {
        assertThrows(NullPointerException.class, () -> registrationService.deleteRefresh(""));
    }

    @Test
    void deleteRefresh_Token_NotFound_NoSuchElementException() {
        when(tokenRepository.findByToken("refresh")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> registrationService.deleteRefresh("refresh"));
    }

    @Test
    void getKey_ReturnPublicKey() {
        RSAPublicKey key = mock(RSAPublicKey.class);
        when(jwtService.getPublicKey()).thenReturn(key);

        RSAPublicKey result = registrationService.getKey();

        assertEquals(key, result);
    }
}
