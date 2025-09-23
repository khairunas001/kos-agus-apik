package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.model.WebResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<String>>  handleMethodArgumentNotValid(MethodArgumentNotValidException exception){
        
    }
}
