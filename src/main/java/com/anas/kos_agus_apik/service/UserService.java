package com.anas.kos_agus_apik.service;


import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.request.CreateUserRequest;
import com.anas.kos_agus_apik.model.request.UsersUpdateRequest;
import com.anas.kos_agus_apik.model.response.CreateUserResponse;
import com.anas.kos_agus_apik.model.response.UsersResponse;
import com.anas.kos_agus_apik.model.response.UsersUpdateResponse;
import com.anas.kos_agus_apik.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.PublicKey;
import java.util.List;
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
        userRepository.save(user);

        return CreateUserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public List<UsersResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> UsersResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .roles(user.getRoles())
                        .build()
                )
                .toList();
    }

    @Transactional
    public UsersResponse get(User user) {

        return UsersResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    @Transactional
    public UsersUpdateResponse updateUser(User user, UsersUpdateRequest request) {

        validationService.validate(request);

        User updateTarget = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        updateTarget.setUsername(request.getUsername());
        updateTarget.setPassword(BCrypt.hashpw(
                request.getPassword(),
                BCrypt.gensalt()
        ));
        updateTarget.setName(request.getName());
        updateTarget.setPhone(request.getPhone());
        updateTarget.setEmail(request.getEmail());
        updateTarget.setRoles(request.getRoles());
        userRepository.save(updateTarget);

        return UsersUpdateResponse.builder()
                .id(user.getId())
                .username(request.getUsername())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .roles(request.getRoles())
                .build();

    }



}

