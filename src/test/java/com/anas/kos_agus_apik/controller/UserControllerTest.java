package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.Token;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.request.LoginUserRequest;
import com.anas.kos_agus_apik.model.request.UsersUpdateRequest;
import com.anas.kos_agus_apik.model.response.TokenResponse;
import com.anas.kos_agus_apik.model.response.UsersResponse;
import com.anas.kos_agus_apik.model.response.UsersUpdateResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponse;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private User user;

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();
        transactionRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();

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

        // --- Setup token ---
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusHours(2);

        Token token = new Token();
        token.setId(UUID.randomUUID().toString());
        token.setUser(user);
        token.setToken("@anas_token");
        token.setTokenExpiredAt(expiredAt);
        tokenRepository.save(token);

    }

    @Test
    void testRegisterBadRequest() throws Exception {
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
    void testRegisterSuccess() throws Exception {
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
                status().isCreated()
        ).andDo(result -> {
            WebResponse<CreateUserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertEquals(
                    "201 CREATED",
                    response.getStatus()
            );

            assertNull(response.getErrors());
            assertEquals(
                    "@anas_username_request",
                    response.getData().getUsername()
            );
            assertEquals(
                    "Anas_name_request",
                    response.getData().getName()
            );
            System.out.println(response.getData());
        });

    }

    @Test
    void getUsersUnauthorized() throws Exception {

        mockMvc.perform(get("/kos-agus/users/current")
                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UsersResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {

                    }
            );

            assertNotNull(response.getErrors());
            assertEquals(
                    "401 UNAUTHORIZED",
                    response.getStatus()
            );
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getErrors()));
        });
    }

    @Test
    void getUsersSuccess() throws Exception {

        mockMvc.perform(get("/kos-agus/users/current")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        "@anas_token"
                                )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UsersResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {

                    }
            );

            assertNull(response.getErrors());
            assertEquals(
                    "200 OK",
                    response.getStatus()
            );
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getErrors()));
        });
    }

    @Test
    void getAllUsersUnauthorized() throws Exception {

        mockMvc.perform(get("/kos-agus/users")
                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UsersResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {

                    }
            );

            assertNotNull(response.getErrors());
            assertEquals(
                    "401 UNAUTHORIZED",
                    response.getStatus()
            );
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getErrors()));
        });
    }

    @Test
    void getAllUsersSuccess() throws Exception {

        mockMvc.perform(get("/kos-agus/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                       "@anas_token"
                                )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<UsersResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {

                    }
            );

            assertNull(response.getErrors());
            assertEquals(
                    "200 OK",
                    response.getStatus()
            );
            assertTrue(response.getData() instanceof List);
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getErrors()));
        });
    }


    @Test
    void updateUserSuccess() throws Exception {

        UsersUpdateRequest request = new UsersUpdateRequest();
        request.setUsername("bambang");
        request.setPassword(BCrypt.hashpw(
                "bambang_password",
                BCrypt.gensalt()
        ));
        request.setName("bambang_lipuro");
        request.setPhone("9102930192309");
        request.setEmail("bambang@lipuro.com");
        request.setRoles(Role.customers);

        mockMvc.perform(patch("/kos-agus/users/current")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        "@anas_token"
                                )
                                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<UsersUpdateResponse> response =
                    objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );

            assertEquals(
                    "201 CREATED",
                    response.getStatus()
            );

            Optional<User> user = Optional.ofNullable(userRepository.findByUsername("bambang")
                                                              .orElseThrow(() -> new RuntimeException("User not found")));

            assertEquals(
                    request.getUsername(),
                    user.get().getUsername()
            );
        });
    }

}