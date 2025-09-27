package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.WebResponse;
import com.anas.kos_agus_apik.model.request.CreateUserRequest;
import com.anas.kos_agus_apik.model.response.CreateUserResponse;
import com.anas.kos_agus_apik.repository.RoomRepository;
import com.anas.kos_agus_apik.repository.TokenRepository;
import com.anas.kos_agus_apik.repository.TransactionRepository;
import com.anas.kos_agus_apik.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
        transactionRepository.deleteAll();
        roomRepository.deleteAll();

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("@anas_test");
        user.setPassword(BCrypt.hashpw(
                "anas_password",
                BCrypt.gensalt()
        ));
        user.setName("Anas_Name");
        user.setNik("9128309128");
        user.setPhone("1082731283");
        user.setEmail("anas@example.com");
        user.setRoles(Role.admin);
        userRepository.save(user);

    }

    @Test
    void testBadRequest() throws Exception{
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("");
        request.setPassword("");

        mockMvc.perform(post("/kos-agus/users/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );
            assertNotNull(response.getErrors());
            System.out.println(response.getErrors());
        });

    }

    @Test
    void testSuccess() throws Exception{
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("@anas_username_request");
        request.setPassword(BCrypt.hashpw(
                "anas_password_request",
                BCrypt.gensalt()
        ));
        request.setName("Anas_name_request");
        request.setNik("921830918");
        request.setPhone("1231231");
        request.setEmail("anas_request@example.cpm");
        request.setRoles(Role.customers);

        mockMvc.perform(post("/kos-agus/users/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<CreateUserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );
            assertNull(response.getErrors());
            assertEquals("@anas_username_request", response.getData().getUsername());
            assertEquals("Anas_name_request", response.getData().getName());
            System.out.println(response.getData());
        });

    }
}