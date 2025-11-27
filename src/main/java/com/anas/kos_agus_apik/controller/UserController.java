package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.ResponseFactory;
import com.anas.kos_agus_apik.model.response.UsersResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponse;
import com.anas.kos_agus_apik.model.request.CreateUserRequest;
import com.anas.kos_agus_apik.model.response.CreateUserResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponseSuccess;
import com.anas.kos_agus_apik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/kos-agus/users/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    private WebResponseSuccess<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        CreateUserResponse createUserResponse = userService.create(request);

        return ResponseFactory.created(createUserResponse).getBody();
    }


    @GetMapping(
            path = "/kos-agus/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponseSuccess<UsersResponse> getUser(User user) {
        UsersResponse usersResponse = userService.get(user);
        return ResponseFactory.ok(usersResponse).getBody();
    }

    // unit test have not created
    @GetMapping(
            path = "/kos-agus/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponseSuccess<List<UsersResponse>> getAllUsers(User user) {
        List<UsersResponse> usersResponse = userService.getAllUsers();
        return ResponseFactory.ok(usersResponse).getBody();
    }

}
