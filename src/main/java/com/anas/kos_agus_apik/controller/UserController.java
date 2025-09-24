package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.model.WebResponse;
import com.anas.kos_agus_apik.model.request.CreateUserRequest;
import com.anas.kos_agus_apik.model.response.CreateUserResponse;
import com.anas.kos_agus_apik.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/kos-agus/users/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    private WebResponse<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        CreateUserResponse createUserResponse = userService.create(request);

        return WebResponse.<CreateUserResponse>builder().data(createUserResponse).build();
    }

}
