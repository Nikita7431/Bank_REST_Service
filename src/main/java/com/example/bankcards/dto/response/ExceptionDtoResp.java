package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Сущность для отправки информации об ошибке на клиент
 */
@Schema(description = "Сущность для обработки исключений")
public class ExceptionDtoResp {
    /**Тип ошибки, по умолчанию "error"*/
    @Schema(description = "тип ошибки")
    private String errorMessage;
    /**Сообщение от исключения"*/
    @Schema(description = "сообщение об ощибке")
    private String exceptionMessage;

    /**
     * Конструктор с параметрами:
     * @param errorMessage тип ощибки
     * @param exceptionMessage сообщение об ошибке
     */
    public ExceptionDtoResp(String errorMessage, String exceptionMessage) {
        this.errorMessage = errorMessage;
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * Конструктор без передачи типа ошибки {@link ExceptionDtoResp#errorMessage}, по умолчанию error
     * @param exceptionMessage сообщение исключения
     */
    public ExceptionDtoResp(String exceptionMessage){
        this("error",exceptionMessage);
    }

    /**
     *Конструктор без передачи {@link  ExceptionDtoResp#exceptionMessage}
     *
     */
    public ExceptionDtoResp(){
        this("Ошибка сервера");
    }
}
