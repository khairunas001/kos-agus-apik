package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.Token;
import com.anas.kos_agus_apik.entity.Transaction;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.AvailabilityRoom;
import com.anas.kos_agus_apik.entity.enum_class.PaymentStatus;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.request.CreateTransactionsRequest;
import com.anas.kos_agus_apik.model.request.UpdatePaymentConfirmationRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

        Room room3 = new Room();
        room3.setId("kamar 90");
        room3.setUser(user);
        room3.setTitle("rooom xx");
        room3.setAvailability(AvailabilityRoom.available);
        room3.setDetails("kamar mandi luar");
        room3.setPrice(740000L);
        room3.setCreatedAt(LocalDateTime.now());
        roomRepository.save(room3);

        // ---setup room booked---
        Room room2 = new Room();
        room2.setId("kamar 2");
        room2.setUser(user);
        room2.setTitle("rooom 22");
        room2.setAvailability(AvailabilityRoom.booked);
        room2.setDetails("kamar mandi luar dalam");
        room2.setPrice(700000L);
        room2.setCreatedAt(LocalDateTime.now());
        roomRepository.save(room2);

        //--- setup transaction---
        Transaction transaction1 = new Transaction();
        transaction1.setId("transaksi 69");
        transaction1.setUser(user);
        transaction1.setRoom(room3);
        transaction1.setAmount(5 * room3.getPrice());
        transaction1.setPeriod(LocalDate.now().plusMonths(5));
        transaction1.setPaymentDate(LocalDateTime.now());
        transaction1.setPaymentStatus(PaymentStatus.pending);
        transaction1.setPaymentMethod("debit");
        transactionRepository.save(transaction1);


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
            assertEquals(5 * 900000L, response.getData().getAmount());
        });

    }

    @Test
    void createTransactionsAlreadyBooked() throws Exception {

        CreateTransactionsRequest request = new CreateTransactionsRequest();
        request.setRoomId("kamar 2");
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
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<TransactionResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());
            assertEquals("Room already booked", response.getErrors());
        });

    }

    @Test
    void createTransactionsNotFound() throws Exception {

        CreateTransactionsRequest request = new CreateTransactionsRequest();
        request.setRoomId("kamar 99");
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
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<TransactionResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());
            assertEquals("Room not found", response.getErrors());
            System.out.println(response.getData());
        });

    }


    @Test
    void updatePaymentConfirmationSuccess() throws Exception {

        UpdatePaymentConfirmationRequest request = new UpdatePaymentConfirmationRequest();
        request.setPaymentStatus(PaymentStatus.paid);

        mockMvc.perform(
                patch("/kos-agus/transactions/update/transaksi 69")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "@anas_token_room"
                        )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TransactionResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNull(response.getErrors());
            assertEquals(AvailabilityRoom.booked, response.getData().getRoom().getAvailability());
            assertEquals(PaymentStatus.paid, response.getData().getPaymentStatus());

            System.out.println(response.getData());
        });

    }

    @Test
    void updatePaymentConfirmationCanceled() throws Exception {

        UpdatePaymentConfirmationRequest request = new UpdatePaymentConfirmationRequest();
        request.setPaymentStatus(PaymentStatus.cancelled);

        mockMvc.perform(
                patch("/kos-agus/transactions/update/transaksi 69")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "@anas_token_room"
                        )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TransactionResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNull(response.getErrors());
            assertEquals(AvailabilityRoom.available, response.getData().getRoom().getAvailability());
            assertEquals(PaymentStatus.cancelled, response.getData().getPaymentStatus());

            System.out.println(response.getData());
        });

    }

    @Test
    void updatePaymentConfirmationFailed() throws Exception {

        UpdatePaymentConfirmationRequest request = new UpdatePaymentConfirmationRequest();
        request.setPaymentStatus(PaymentStatus.pending);

        mockMvc.perform(
                patch("/kos-agus/transactions/update/transaksi 69")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "@anas_token_room"
                        )
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<TransactionResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());
            assertEquals("admin can only confirm payment to PAID or CANCELED", response.getErrors());

            System.out.println(response);
        });

    }

    @Test
    void getAllTransactions() throws Exception {

        mockMvc.perform(
                get("/kos-agus/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "@anas_token_room"
                        )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<TransactionResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertInstanceOf(List.class, response.getData());

            System.out.println(response.getData());
        });
    }

    @Test
    void getAllTransactionsFailed() throws Exception {

        mockMvc.perform(
                get("/kos-agus/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "token-salah"
                        )
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<List<TransactionResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());

            System.out.println(response);
        });
    }

    @Test
    void getAllTransactionsHistories() throws Exception {

        //use helper functions
        createTransactions("kamar 1");
        createTransactions("kamar 90");

        mockMvc.perform(
                get("/kos-agus/transactions/histories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "@anas_token_room_costumers"
                        )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<TransactionResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertInstanceOf(List.class, response.getData());

            System.out.println(response.getData());
        });
    }

    // helper class
    private void createTransactions(String roomId) throws Exception {

        CreateTransactionsRequest request = new CreateTransactionsRequest();
        request.setRoomId(roomId);
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
        ).andExpect(status().isCreated());
    }

}