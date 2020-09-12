package com.ebay.demo.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private HttpStatus status;
    private String message;
    private LocalDateTime time;
    private String exceptionType;
}
