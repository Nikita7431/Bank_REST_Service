package com.example.bankcards.config;

import com.example.bankcards.security.JWTFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Конфигурация для Security
 */
@Configuration
@EnableScheduling
@EnableWebSecurity
public class SecurityConfig {
    /**
     * jwt фильтр
     */
    private final JWTFilter jwtFilter;

    /**
     * Конструктор с параметрами(зависимости)
     * @param jwtFilter jwt фильтр
     */
    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Конвейер фильтров
     * @param http {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     *
     */
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

              .authorizeHttpRequests(
                      authorizeHttp -> {
                          authorizeHttp.requestMatchers("/").permitAll();
                          authorizeHttp.requestMatchers("/favicon.svg").permitAll();
                          authorizeHttp.requestMatchers("/css/*").permitAll();
                          authorizeHttp.requestMatchers("/error").permitAll();
                          authorizeHttp.requestMatchers("/auth/**").permitAll();
                          authorizeHttp.requestMatchers("/loginref").permitAll();
                          authorizeHttp.requestMatchers("/v3/api-docs.yaml").permitAll();

                          authorizeHttp.requestMatchers("/admin/**").hasRole("ADMIN");
                          authorizeHttp.anyRequest().authenticated();
                      }
              )
              .formLogin(l -> l.defaultSuccessUrl("/private"))
              .logout(l -> l.logoutSuccessUrl("/"))
              .addFilterBefore(jwtFilter, AuthenticationFilter.class)
              .build();
  }

    /**
     * Корс конфигурация
     * @return {@link CorsConfigurationSource}
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Осуществляет шифрование пароля перед сохранением в бд
     * @return {@link PasswordEncoder}
     */
  @Bean
  public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
  }

    /**
     * Маппер объектов
     * @return {@link ModelMapper}
     */
  @Bean
  public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
