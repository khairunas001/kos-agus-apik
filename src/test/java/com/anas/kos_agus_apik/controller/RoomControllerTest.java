package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.Token;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.AvailabilityRoom;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.request.CreateRoomRequest;
import com.anas.kos_agus_apik.model.response.RoomResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponse;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

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
        user.setUsername("@anas_test_room");
        user.setPassword(BCrypt.hashpw(
                "anas_password_room",
                BCrypt.gensalt()
        ));
        user.setName("Anas_Name_room");
        user.setNik("9128309128");
        user.setPhone("1082731283");
        user.setEmail("anas_room@example.com");
        user.setRoles(Role.admin);
        userRepository.save(user);

        // --- Setup token ---
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusHours(2);

        Token token = new Token();
        token.setId(UUID.randomUUID().toString());
        token.setUser(user);
        token.setToken("@anas_token_room");
        token.setTokenExpiredAt(expiredAt);
        tokenRepository.save(token);
    }

    @Test
    void createRoomBadRequest() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest();
        request.setTitle("");

        mockMvc.perform(
                post("/kos-agus/rooms/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "@anas_token_room"
                        )
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<RoomResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());

            System.out.println(response);

        });


    }

    @Test
    void createRoomSuccesss() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest();
        request.setTitle("kamar 69");
        request.setAvailability(AvailabilityRoom.available);
        request.setDetails("Kamar mandi dalam");
        request.setPrice(15000000L);

        Room room = new Room();
        room.setId(UUID.randomUUID().toString());
        room.setUser(user);
        room.setTitle(request.getTitle());
        room.setAvailability(request.getAvailability());
        room.setDetails(request.getDetails());
        room.setPrice(request.getPrice());
        room.setCreatedAt(LocalDateTime.now());
        roomRepository.save(room);


        mockMvc.perform(
                post("/kos-agus/rooms/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "@anas_token_room"
                        )
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<RoomResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());

            assertEquals(
                    "CREATED 201",
                    response.getStatus()
            );

            System.out.println(response);

        });


    }

}