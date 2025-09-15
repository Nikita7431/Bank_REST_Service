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

@Configuration
//@ComponentScan()
@EnableScheduling
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private final JWTFilter jwtFilter;
    @Autowired
    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
              .authorizeHttpRequests(
                      authorizeHttp -> {
                          authorizeHttp.requestMatchers("/").permitAll();
                          authorizeHttp.requestMatchers("/favicon.svg").permitAll();
                          authorizeHttp.requestMatchers("/css/*").permitAll();
                          authorizeHttp.requestMatchers("/error").permitAll();
                          authorizeHttp.requestMatchers("/reg/user").permitAll();
                          authorizeHttp.requestMatchers("/login").permitAll();
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
  @Bean
  public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
  }
  @Bean
  public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
