package com.marlonb.book_catalog_api.exception;


import com.marlonb.book_catalog_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.book_catalog_api.exception.custom.ResourceNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // HTTP STATUS: 500 — INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsDto> handleServerExceptions (Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorDetailsDto(LocalDateTime.now(),
                                   HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                   ex.getMessage()));
    }

    // Handles Database related issue
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDetailsDto> handlesDataAccessException
            (DataAccessException ex){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorDetailsDto(LocalDateTime.now(),
                                   HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                   "Database Error" +
                                   ex.getMostSpecificCause().getMessage()));
    }

    // Handles duplication of a unique resource attribute (e.g., category name)
    // HTTP STATUS 409: CONFLICT
    @ExceptionHandler(DuplicateResourceFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handlesDuplicateResourceExceptions
    (DuplicateResourceFoundException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDetailsDto(LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage()));
    }

    // HTTP STATUS 404: NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handlesNotFoundExceptions
                    (ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorDetailsDto(LocalDateTime.now(),
                                  HttpStatus.NOT_FOUND.value(),
                                  ex.getMessage()));
    }

    // Handles validation related issues of entity attributes
    // HTTP STATUS: 400 — BAD REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailsDto> handlesMethodArgumentExceptions
    (MethodArgumentNotValidException ex) {

        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err ->
                        err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDetailsDto(LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        errorMessages));
    }

    // Handles missing or malformed JSON request body
    // HTTP Status: 400 BAD REQUEST
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetailsDto> handlesNonReadableExceptions
            (HttpMessageNotReadableException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorDetailsDto(LocalDateTime.now(),
                                                    HttpStatus.BAD_REQUEST.value(),
                                                    ex.getMessage()));
    }
}
