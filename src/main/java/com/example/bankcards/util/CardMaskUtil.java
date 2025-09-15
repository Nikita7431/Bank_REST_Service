package com.example.bankcards.util;

import org.springframework.stereotype.Component;

@Component
public class CardMaskUtil {

    public static String maskFromNumber(String number) {
        if (number == null) return null;
        String digits = number.replaceAll("\\s+", "");
        if (digits.length() <= 4) return "****";
        String last4 = digits.substring(digits.length() - 4);
        return "**** **** **** " + last4;
    }

}
