package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.model.WebResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorController {

    // Handler untuk validasi @Valid di @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        WebResponse<?> response = WebResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .errors(errorMessage)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handler untuk validasi @Validated di method level
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<?>> handleConstraintViolation(ConstraintViolationException exception) {

        String errorMessage = exception.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + " " + cv.getMessage())
                .collect(Collectors.joining(", "));

        WebResponse<?> response = WebResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .errors(errorMessage)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handler untuk ResponseStatusException (manual throw)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<?>> handleApiException(ResponseStatusException exception) {
        WebResponse<?> response = WebResponse.builder()
                .status(exception.getStatusCode().toString())
                .errors(exception.getReason())
                .build();

        return ResponseEntity.status(exception.getStatusCode()).body(response);
    }

    // fallback untuk exception lainnya
    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<?>> handleGeneralException(Exception exception) {
        WebResponse<?> response = WebResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .errors(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
