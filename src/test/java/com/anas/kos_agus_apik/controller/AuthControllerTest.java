package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.Token;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.WebResponse;
import com.anas.kos_agus_apik.model.request.LoginUserRequest;
import com.anas.kos_agus_apik.model.response.TokenResponse;
import com.anas.kos_agus_apik.repository.RoomRepository;
import com.anas.kos_agus_apik.repository.TokenRepository;
import com.anas.kos_agus_apik.repository.TransactionRepository;
import com.anas.kos_agus_apik.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {


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
        roomRepository.deleteAll();
        transactionRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("@Anas_Username");
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
    void loginFailedUserNotFound() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("@Anas_Username_salah");
        request.setPassword("anas_password_salah");

        mockMvc.perform(post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {

                    }
            );

            assertNotNull(response.getErrors());
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getErrors()));
        });
    }

    @Test
    void loginSuccess() throws Exception {

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("@Anas_Username");
        request.setPassword("anas_password");

        mockMvc.perform(post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {

                    }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getTokenExpiredAt());

            User userDb = userRepository.findByUsername("@Anas_Username").orElse(null);
            assertNotNull(userDb);

            // ambil token yang berkaitan dengan user
            Token tokenDb = tokenRepository.findByUser(userDb).orElse(null);
            assertNotNull(tokenDb);

            // pastikan nilai sama dengan response
            assertEquals(response.getData().getToken(), tokenDb.getToken());
            //            assertEquals(
            //                    response.getData().getTokenExpiredAt().truncatedTo(ChronoUnit.SECONDS),
            //                    tokenDb.getTokenExpiredAt().truncatedTo(ChronoUnit.SECONDS)
            //            );
            assertTrue(tokenDb.getTokenExpiredAt().isAfter(LocalDateTime.now()));


            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getData()));
        });


    }
}