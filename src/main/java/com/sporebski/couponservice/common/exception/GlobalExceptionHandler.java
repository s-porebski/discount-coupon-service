package com.sporebski.couponservice.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message("Invalid request data.")
                        .path(request.getRequestURI())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .details(errors)
                        .timestamp(System.currentTimeMillis())
                        .build()
                );
    }

    @ExceptionHandler({ApiBusinessException.class})
    public ResponseEntity<ErrorResponse> handleApiBusinessException(ApiBusinessException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(exception.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .error(exception.getErrorCode().getStatus().getReasonPhrase())
                        .message(exception.getErrorCode().getMessage())
                        .path(request.getRequestURI())
                        .status(exception.getErrorCode().getStatus().value())
                        .timestamp(System.currentTimeMillis())
                        .build()
                );
    }
}
