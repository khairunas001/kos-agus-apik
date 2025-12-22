package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.Token;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.AvailabilityRoom;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.request.CreateTransactionsRequest;
import com.anas.kos_agus_apik.model.response.TransactionResponse;
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

// harusnya pakai ini katanya -import static org.springframework.test.web.servlet.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

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
        transactionRepository.deleteAll();
        roomRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();

        // -- setup admin--
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("@anas_test_room");
        user.setPassword(BCrypt.hashpw(
                "anas_password_room",
                BCrypt.gensalt()
        ));
        user.setName("Anas_Name_room");
        user.setNik("12837129312");
        user.setPhone("1082731283");
        user.setEmail("anas_room@example.com");
        user.setRoles(Role.admin);
        userRepository.save(user);

        // -- setup customers--
        User user2 = new User();
        user2.setId(UUID.randomUUID().toString());
        user2.setUsername("@anas_test_room_costumers");
        user2.setPassword(BCrypt.hashpw(
                "anas_password_room_costumers",
                BCrypt.gensalt()
        ));
        user2.setName("Anas_Name_room_costumers");
        user2.setNik("9128309128");
        user2.setPhone("1082731283");
        user2.setEmail("anas_room_costumers@example.com");
        user2.setRoles(Role.customers);
        userRepository.save(user2);

        // --- Setup token ---
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusHours(2);

        // --- setup token admin--
        Token token1 = new Token();
        token1.setId(UUID.randomUUID().toString());
        token1.setUser(user);
        token1.setToken("@anas_token_room");
        token1.setTokenExpiredAt(expiredAt);
        tokenRepository.save(token1);

        // --- setup token costumers---
        Token token2 = new Token();
        token2.setId(UUID.randomUUID().toString());
        token2.setUser(user2);
        token2.setToken("@anas_token_room_costumers");
        token2.setTokenExpiredAt(expiredAt);
        tokenRepository.save(token2);

        // ---setup room---
        Room room = new Room();
        room.setId("kamar 1");
        room.setUser(user);
        room.setTitle("rooom 12");
        room.setAvailability(AvailabilityRoom.available);
        room.setDetails("kamar mandi luar dalam");
        room.setPrice(900000L);
        room.setCreatedAt(LocalDateTime.now());
        roomRepository.save(room);

        // ---setup room booked---
        Room room2 = new Room();
        room2.setId("kamar 1");
        room2.setUser(user);
        room2.setTitle("rooom 12");
        room2.setAvailability(AvailabilityRoom.available);
        room2.setDetails("kamar mandi luar dalam");
        room2.setPrice(900000L);
        room2.setCreatedAt(LocalDateTime.now());
        roomRepository.save(room2);

    }

    @Test
    void createTransactionsSuccess() throws Exception {

        CreateTransactionsRequest request = new CreateTransactionsRequest();
        request.setRoomId("kamar 1");
        request.setDurationMonth(5);
        request.setPaymentMethod("cash");

        mockMvc.perform(post("/kos-agus/transactions/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(
                        "API-TOKEN-KOS-AGUS-APIK",
                        "@anas_token_room_costumers"
                )
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<TransactionResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNull(response.getErrors());
            assertEquals(900000L, response.getData().getRoom().getPrice());
            assertEquals(5*900000L, response.getData().getAmount());
        });

    }

    @Test
    void createTransactionsAlreadyBooked() {

    }

    @Test
    void createTransactionsBadRequest() {

    }

}