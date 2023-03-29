package com.expensetracker.restapi.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiError {
    private HttpStatus httpStatus;
    private String errorMessage;
    private LocalDateTime localDateTime;
    private int errorCode;
}
