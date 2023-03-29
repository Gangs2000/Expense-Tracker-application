package com.expensetracker.restapi.Exception;

public class CustomizedException extends RuntimeException {
    public CustomizedException(String message){
        super(message);
    }
}
