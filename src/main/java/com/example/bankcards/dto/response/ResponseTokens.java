package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto для Access и Refresh токенов
 */
@Getter @Setter
@Schema(description = "сущность для двух токенов")
public class ResponseTokens {
    /** поле Access токена*/
    @Schema(description = "acess токен")
    private String accessToken;
    /** поле Refresh токена*/
    @Schema(description = "refresh токен")
    private  String refreshToken;

    /**
     * Конструктор без параметров
     */
    public ResponseTokens(){}

    /**
     * Конструктор с параметрами:
     * @param accessToken Access токен
     * @param refreshToken Refresh токен
     */
    public ResponseTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
