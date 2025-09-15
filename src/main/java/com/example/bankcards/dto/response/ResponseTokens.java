package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "сущность для двух токенов")
public class ResponseTokens {
    @Schema(description = "acess токен")
    private String accessToken;
    @Schema(description = "refresh токен")
    private  String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ResponseTokens(){}

    public ResponseTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


}
