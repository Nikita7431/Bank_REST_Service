package com.example.bankcards.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Утилита по созданию маски для номера карты
 */
@Component
@NoArgsConstructor
public class CardMaskUtil {
    /**
     * Формирует маску для номера карты в формате
     *
     * @param number номер карты
     * @return строка в формате:
     * <pre>**** **** **** 4321</pre>
     */
    public static String maskFromNumber(String number) {
        if (number == null) return null;
        String digits = number.replaceAll("\\s+", "");
        if (digits.length() <= 4) return "****";
        String last4 = digits.substring(digits.length() - 4);
        return "**** **** **** " + last4;
    }

}
