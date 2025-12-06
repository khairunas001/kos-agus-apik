package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.model.web_response.WebResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponseErrors;
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
    public ResponseEntity<WebResponseErrors<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        WebResponseErrors<?> response = WebResponseErrors.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .errors(errorMessage)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handler untuk validasi @Validated di method level
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponseErrors<?>> handleConstraintViolation(ConstraintViolationException exception) {

        String errorMessage = exception.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + " " + cv.getMessage())
                .collect(Collectors.joining(", "));

        WebResponseErrors<?> response = WebResponseErrors.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .errors(errorMessage)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handler untuk ResponseStatusException (manual throw)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponseErrors<?>> handleApiException(ResponseStatusException exception) {
        WebResponseErrors<?> response = WebResponseErrors.builder()
                .status(exception.getStatusCode().toString())
                .errors(exception.getReason())
                .build();

        return ResponseEntity.status(exception.getStatusCode()).body(response);
    }

    // fallback untuk exception lainnya
    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponseErrors<?>> handleGeneralException(Exception exception) {
        WebResponseErrors<?> response = WebResponseErrors.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .errors(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<WebResponse<String>> runtimError(RuntimeException ex){
        if(ex.getMessage().equals("room not found")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new WebResponse<>("404 NOT_FOUND", null, ex.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new WebResponse<>("500 INTERNAL_SERVER_ERROR", null, ex.getMessage()));
    }

    // この生活は、どうすれば良いんでしょうか？
    // 仕事はまだ持ってないのに、うまくになりたいけど、誰も時間を上げないのおおお
    // ま。。、　明日もいるしい、まだあけらめないよお、　未来は誰もしているなので。
    // 自分で信じている、あと少しだけ、美しい未来も来る :-)
    // 内定を貰って欲しいよ


}
