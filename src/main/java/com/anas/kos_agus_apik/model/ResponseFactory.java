package com.anas.kos_agus_apik.model;

import com.anas.kos_agus_apik.model.web_response.WebResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponseSuccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {

    public static <T> WebResponseSuccess<T> build(HttpStatus status, T data) {
        return WebResponseSuccess.<T>builder()
                .status(status.name() + " " + status.value())
                .data(data)
                .build();
    }


    public static <T> ResponseEntity<WebResponseSuccess<T>> ok(T data) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(build(
                        HttpStatus.OK,
                        data
                ));
    }


    public static <T> ResponseEntity<WebResponseSuccess<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(build(
                        HttpStatus.CREATED,
                        data
                ));
    }

}
