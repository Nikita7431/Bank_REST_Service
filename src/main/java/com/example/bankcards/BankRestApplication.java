package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в приложение
 */
@SpringBootApplication(scanBasePackages = "com.example.bankcards")
public class BankRestApplication {
    /**
     * Main
     * @param args аргументы
     */
    public static void main(String[] args) {
        SpringApplication.run(BankRestApplication.class, args);
    }
}