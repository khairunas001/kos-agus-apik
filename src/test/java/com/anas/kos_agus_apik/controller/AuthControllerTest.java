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

        mockMvc.perform(post("/kos-agus/api/auth/login")
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

        mockMvc.perform(post("/kos-agus/api/auth/login")
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
            assertEquals(
                    response.getData().getToken(),
                    tokenDb.getToken()
            );
            //            assertEquals(
            //                    response.getData().getTokenExpiredAt().truncatedTo(ChronoUnit.SECONDS),
            //                    tokenDb.getTokenExpiredAt().truncatedTo(ChronoUnit.SECONDS)
            //            );
            assertTrue(tokenDb.getTokenExpiredAt().isAfter(LocalDateTime.now()));


            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getData()));
        });

    }

    @Test
    void logOutFailed() throws Exception {

        mockMvc.perform(delete("/kos-agus/api/auth/logout")
                                .contentType(MediaType.APPLICATION_JSON)
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
    void logOutSuccess() throws Exception {

        // --- Setup user unik ---
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("user_" + UUID.randomUUID().toString().substring(
                0,
                8
        ));
        user.setPassword(BCrypt.hashpw(
                "anas_password",
                BCrypt.gensalt()
        ));
        user.setName("Anas Test User");
        user.setNik(UUID.randomUUID().toString().substring(
                0,
                12
        )); // unik, 12 digit cukup
        user.setPhone("08" + (long) (Math.random() * 1000000000L)); // random nomor
        user.setEmail("anas_" + UUID.randomUUID().toString().substring(
                0,
                6
        ) + "@example.com");
        user.setRoles(Role.admin);
        userRepository.save(user);

        // --- Setup token ---
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusHours(2);

        Token token = new Token();
        token.setId(UUID.randomUUID().toString());
        token.setUser(user);
        token.setToken(newToken);
        token.setTokenExpiredAt(expiredAt);
        tokenRepository.save(token);

        mockMvc.perform(delete("/kos-agus/api/auth/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        newToken
                                )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertEquals(
                    "oke",
                    response.getData()
            );

            // pastikan data terhapus
            assertFalse(tokenRepository.findFirstByToken(newToken).isPresent());

            System.out.println("✅ Logout success test passed for user: " + user.getUsername());
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        });
    }

    @Test
    void logOutAllDeviceFailed() throws Exception {
        mockMvc.perform(delete("/kos-agus/api/auth/logout-all-device")
                                .contentType(MediaType.APPLICATION_JSON)
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
    void logOutAllDevice() throws Exception {


        // --- Setup user unik ---
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("user_" + UUID.randomUUID().toString().substring(
                0,
                8
        ));
        user.setPassword(BCrypt.hashpw(
                "anas_password",
                BCrypt.gensalt()
        ));
        user.setName("Anas Test User");
        user.setNik(UUID.randomUUID().toString().substring(
                0,
                12
        )); // unik, 12 digit cukup
        user.setPhone("08" + (long) (Math.random() * 1000000000L)); // random nomor
        user.setEmail("anas_" + UUID.randomUUID().toString().substring(
                0,
                6
        ) + "@example.com");
        user.setRoles(Role.admin);
        userRepository.save(user);

        // --- Setup multiple tokens ---
        LocalDateTime expiredAt = LocalDateTime.now().plusHours(2);
        String activeTokenValue = "active_" + UUID.randomUUID();

        for (int i = 0; i < 10; i++) {
            Token token = new Token();
            token.setId(i + "_" + UUID.randomUUID().toString());
            token.setUser(user);
            token.setToken("token_" + i + "_" + UUID.randomUUID());
            token.setTokenExpiredAt(expiredAt);
            tokenRepository.save(token);
        }

        Token activeToken = new Token();
        activeToken.setId(UUID.randomUUID().toString());
        activeToken.setUser(user);
        activeToken.setToken(activeTokenValue);
        activeToken.setTokenExpiredAt(expiredAt);
        tokenRepository.save(activeToken);

        mockMvc.perform(delete("/kos-agus/api/auth/logout-all-device")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        activeTokenValue
                                )

        ).andExpectAll(
                status().isOk()
        ).andExpectAll(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());

            // pastikan data terhapus
            assertFalse(tokenRepository.findFirstByToken(activeTokenValue).isPresent());

            System.out.println("✅ Logout all device success test passed for user: " + user.getUsername());
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));

        });

    }
}