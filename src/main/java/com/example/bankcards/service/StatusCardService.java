package com.example.bankcards.service;

import com.example.bankcards.entity.StatusCard;
/**Данный интерфейс представляет функционал по работе с сущьностью {@link StatusCard} */
public interface StatusCardService {
    /**
     * Осуществляет поиск статуса по его названию (строковому представлению)
     * @param status название
     * @return {@link StatusCard}
     */
    public StatusCard findStatus(String status);
}
