package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.ResponseFactory;
import com.anas.kos_agus_apik.model.web_response.WebResponse;
import com.anas.kos_agus_apik.model.request.LoginUserRequest;
import com.anas.kos_agus_apik.model.response.TokenResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponseSuccess;
import com.anas.kos_agus_apik.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/kos-agus/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponseSuccess<TokenResponse>> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseFactory.ok(tokenResponse);
    }

    @DeleteMapping(
            path = "/kos-agus/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponseSuccess<Object>> logOut(User user, HttpServletRequest request) {
        String tokenValue = (String) request.getAttribute("activeTokenValue");
        authService.logOut(
                tokenValue,
                user
        );
        return ResponseFactory.ok(null);
    }

//    @DeleteMapping(
//            path = "/kos-agus/api/auth/logout-all-device",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public WebResponse<String> logOutAllDevice(User user) {
//        authService.logOutAllDevice(user);
//        return WebResponse.<String>builder().status(String.valueOf(HttpStatus.OK.value() + " " + HttpStatus.OK.name())).data("Deleted data success").build();
//    }

    @DeleteMapping("/kos-agus/api/auth/logout-all-device")
    public ResponseEntity<WebResponseSuccess<String>> logOutAllDevice(User user) {
        authService.logOutAllDevice(user);
        return ResponseFactory.ok("Deleted data success");
    }

}
