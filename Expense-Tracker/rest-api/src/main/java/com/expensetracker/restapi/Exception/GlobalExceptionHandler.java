package com.expensetracker.restapi.Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired ApiError apiError;
    @ExceptionHandler(value = CustomizedException.class)
    public ResponseEntity<ApiError> globalExceptionHandler(CustomizedException customizedException){
        apiError.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
        apiError.setErrorMessage(customizedException.getMessage());
        apiError.setErrorCode(HttpStatus.EXPECTATION_FAILED.value());
        apiError.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.EXPECTATION_FAILED);
    }
}
