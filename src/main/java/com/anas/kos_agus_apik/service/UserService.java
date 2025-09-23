package com.anas.kos_agus_apik.service;


import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.request.CreateUserRequest;
import com.anas.kos_agus_apik.model.response.CreateUserResponse;
import com.anas.kos_agus_apik.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;


    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {

        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "username already registered"
            );
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(
                request.getPassword(),
                BCrypt.gensalt()
        ));
        user.setName(request.getName());
        user.setNik(request.getNik());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRoles(request.getRoles());

        return CreateUserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }


}

