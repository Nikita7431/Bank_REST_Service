package com.example.bankcards.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.bankcards.dto.request.UserLoginDtoReque;
import com.example.bankcards.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.bankcards.dto.request.UserDtoReque;
import com.example.bankcards.service.JWTService;
import com.example.bankcards.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для jwt
 *  <ul>
 *     <li>Извлекает заголовок {@code Authorization} из входящего запроса.</li>
 *     <li>Проверяет наличие токена(заголовок должен начинаться с {@code Bearer }).</li>
 *     <li>Валидирует токен через {@link JWTService}</li>
 *     <li>Получает пользователя - {@link UserDetails} через {@link UserService}.</li>
 *     <li>Создаёт {@link UsernamePasswordAuthenticationToken} и помещает его в {@link SecurityContextHolder}.</li>
 *     <li>В случае некорректного токена возвращает ошибку {@code 400 Bad Request}.</li>
 * </ul>
 * <p>
 * Наследуется от {@link OncePerRequestFilter}, поэтому вызывается только один раз на каждый запрос.
 */
@Component
public class JWTFilter extends OncePerRequestFilter {
    /**
     * Сервис для работы с jwt
     */
    private final JWTService jwtService;
    /**
     * Сервис для работы с пользователями
     */
    private final UserService userServiceImpl;

    /**
     * Конструктор с параметрами (внедрение зависимостей)
     *
     * @param jwtService      сервис для работы с jwt
     * @param userServiceImpl сервис для работы с пользователями
     */
    public JWTFilter(JWTService jwtService, @Lazy UserService userServiceImpl) {
        this.jwtService = jwtService;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException, ServletException {
        /**Извлекаем заголовок {@code Authorization} из входящего запроса*/
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            /**
             * Извлекаем токен после Bearer
             */
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    /**
                     * Валидация токена через JwtService и получение пользователя в {@link UserDetails}
                     */
                    UserLoginDtoReque userDTOReque = jwtService.validateTokenAndSyncClaim(jwt);
                    UserDetails userDetails = userServiceImpl.loadUserByUsername(userDTOReque.getLogin());
                    /**
                     * Формирование {@link UsernamePasswordAuthenticationToken}
                     */
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());
                    /**
                     * Перекладывание {@link UsernamePasswordAuthenticationToken} в {@link SecurityContextHolder}
                     */
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException exc) {
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                }
            }
        }
        /**
         * Передача управления следующему в цепочке
         */
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
