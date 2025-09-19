package com.example.bankcards.exception;

import com.example.bankcards.dto.response.ExceptionDtoResp;
import com.example.bankcards.dto.response.PublicKeyResp;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import javax.management.relation.RoleNotFoundException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
public class CommandExceptionAdvise {
    /**
     *Обрабатывает {@link HttpMessageConversionException} - для некорректного json, по структуре
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link PublicKeyResp}&gt;
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<Map<String,String>> handleHttpMessageConversionException(HttpMessageConversionException ex){
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Некорректный JSON"));
    }

    /**
     *Обрабатывает {@link ConstraintViolationException}
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link ExceptionDtoResp}&gt;
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDtoResp>handleConstraintViolationException(ConstraintViolationException ex){
        String message = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getMessage()) // берем текст ошибки аннотации
                .findFirst()
                .orElse("Valid error");
        return ResponseEntity.badRequest()
                .body(new ExceptionDtoResp(message));
    }

    /**
     * Обрабатывает {@link MethodArgumentNotValidException}
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link ExceptionDtoResp}&gt;
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDtoResp> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getDefaultMessage())
                .findFirst()
                .orElse("Valid error");
        return ResponseEntity.badRequest()
                        .body(new ExceptionDtoResp(message));
    }

    /**
     *Обрабатывает {@link HttpMessageNotReadableException} - для некорректного json, по содержанию
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link ExceptionDtoResp}&gt;
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDtoResp> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){

        return ResponseEntity.badRequest()
                .body(new ExceptionDtoResp("Тело запроса отсутствует или некорректно"));
    }

    /**
     * Обрабатывает {@link NoSuchElementException}
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link ExceptionDtoResp}&gt;
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDtoResp> handleNoSuchElementException(NoSuchElementException ex)
    {
        return ResponseEntity.badRequest()
                .body(new ExceptionDtoResp(ex.getMessage()));
    }

    /**
     * Обрабатывет {@link IllegalArgumentException}
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link ExceptionDtoResp}&gt;
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDtoResp> handleIllegalArgumentException(IllegalArgumentException ex){
        return ResponseEntity.badRequest()
                .body(new ExceptionDtoResp(ex.getMessage()));
    }

    /**
     * Обрабатывает {@link  NullPointerException}
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link ExceptionDtoResp}&gt;
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>>handleNullPointerException(NullPointerException ex){
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Обрабатывает {@link NoSuchElementException}
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link ExceptionDtoResp}&gt;
     */
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionDtoResp>handleRoleNotFoundException(RoleNotFoundException ex){
        return ResponseEntity.badRequest()
                .body(new ExceptionDtoResp(ex.getMessage()));
    }

    /**
     * Обрабатывает любой {@link Exception}
     * @param ex обрабатываемое исключение
     * @return {@link ResponseEntity}&lt;{@link ExceptionDtoResp}&gt;
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDtoResp> handleException(Exception ex){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ExceptionDtoResp());
    }

}
