package com.ebay.demo.controller;

import com.ebay.demo.exception.AuctionException;
import com.ebay.demo.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { AuctionException.class, Exception.class })
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {

        logger.error("{}", ex);

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        apiError.setTime(LocalDateTime.now());

        return handleExceptionInternal(ex, apiError,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}