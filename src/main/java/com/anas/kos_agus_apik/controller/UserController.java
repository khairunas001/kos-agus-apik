package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.ResponseFactory;
import com.anas.kos_agus_apik.model.request.CreateUserRequest;
import com.anas.kos_agus_apik.model.request.UsersUpdateRequest;
import com.anas.kos_agus_apik.model.response.CreateUserResponse;
import com.anas.kos_agus_apik.model.response.UsersResponse;
import com.anas.kos_agus_apik.model.response.UsersUpdateResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponseSuccess;
import com.anas.kos_agus_apik.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
        path = "/kos-agus/users/register",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponseSuccess<CreateUserResponse>> create(
        @RequestBody CreateUserRequest request
    ) {
        CreateUserResponse createUserResponse = userService.create(request);

        return ResponseFactory.created(createUserResponse);
    }

    @GetMapping(
        path = "/kos-agus/users/current",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponseSuccess<UsersResponse>> getUser(
        User user
    ) {
        UsersResponse usersResponse = userService.get(user);
        return ResponseFactory.ok(usersResponse);
    }

    @GetMapping(
        path = "/kos-agus/users",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponseSuccess<List<UsersResponse>>> getAllUsers(
        User user
    ) {
        List<UsersResponse> usersResponse = userService.getAllUsers();
        return ResponseFactory.ok(usersResponse);
    }

    // unit test have not created
    @PatchMapping(
        path = "/kos-agus/users/current",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponseSuccess<UsersUpdateResponse>> updateUser(
        User user,
        @RequestBody UsersUpdateRequest request
    ) {
        UsersUpdateResponse usersUpdateResponse = userService.updateUser(
            user,
            request
        );
        return ResponseFactory.created(usersUpdateResponse);
    }
}
