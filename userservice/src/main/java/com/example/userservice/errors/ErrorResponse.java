package com.example.userservice.errors;

import lombok.Data;
import java.util.Map;

@Data
public class ErrorResponse {
    private String message;
    private Map<String, String> errors;
    private int status;

    public ErrorResponse(String message, Map<String, String> errors, int status) {
        this.message = message;
        this.errors = errors;
        this.status = status;
    }
}