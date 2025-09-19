package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.security.interfaces.RSAPublicKey;

/**
 * Dto для PublicKey
 */
@Schema(description = "Сущность для PublicKey")
public class PublicKeyResp {
    /**
     * Публичный ключ для клиента
     */
    @Getter
    @Schema(description = "PublicKey")
    private RSAPublicKey publicKey;

    /**
     * Конструктор с параметром:
     * @param publicKey публичный ключ: {@link RSAPublicKey}
     */
    public PublicKeyResp(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

}
