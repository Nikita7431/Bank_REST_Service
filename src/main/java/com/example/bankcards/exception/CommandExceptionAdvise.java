package com.example.bankcards.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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

@RestControllerAdvice
public class CommandExceptionAdvise {

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<Map<String,String>> handleHttpMessageConversionException(HttpMessageConversionException ex){
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Некорректный JSON"));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>>handleConstraintViolationException(ConstraintViolationException ex){
        String message = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getMessage()) // берем только текст ошибки аннотации
                .findFirst()
                .orElse("Valid error");
        return ResponseEntity.badRequest()
                .body(Map.of("error", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getDefaultMessage())
                .findFirst()
                .orElse("Valid error");
        return ResponseEntity.badRequest()
                        .body(Map.of("error", message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){

        return ResponseEntity.badRequest()
                .body(Map.of("error", "Тело запроса отсутствует или некорректно"));
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex)
    {
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> handleIllegalArgumentException(IllegalArgumentException ex){
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>>handleNullPointerException(NullPointerException ex){
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String,String>>handleRoleNotFoundException(RoleNotFoundException ex){
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e){
        return new ResponseEntity<>(HttpStatusCode.valueOf(500));
    }

}
