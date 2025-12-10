package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.Token;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.AvailabilityRoom;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.request.CreateRoomRequest;
import com.anas.kos_agus_apik.model.request.UpdateRoomRequest;
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
import java.util.List;
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
    void createRoomSuccess() throws Exception {
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
                    "201 CREATED",
                    response.getStatus()
            );

            System.out.println(response);

        });


    }

    @Test
    void deleteRoomBadRequest() throws Exception {
        mockMvc.perform(
                delete("/kos-agus/rooms/delete/1231231")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "API-TOKEN-KOS-AGUS-APIK",
                                "@anas_token_room"
                        )
        ).andExpectAll(
                status().isNotFound()
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
    void deleteRoomSuccess() throws Exception {

        Room room = new Room();
        room.setId("room-keluarga-joko");
        room.setUser(user);
        room.setTitle("Room 1");
        room.setAvailability(AvailabilityRoom.available);
        room.setDetails("kamar mandi dalam");
        room.setPrice(8000000L);
        room.setCreatedAt(LocalDateTime.now());
        roomRepository.save(room);

        mockMvc.perform(delete("/kos-agus/rooms/delete/" + room.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        "@anas_token_room"
                                )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());

            assertEquals(
                    "200 OK",
                    response.getStatus()
            );

            System.out.println(response);

        });


    }


    @Test
    void getAllRoomUnauthorized() throws Exception {

        mockMvc.perform(get("/kos-agus/rooms")
                                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());

            assertEquals(
                    "401 UNAUTHORIZED",
                    response.getStatus()
            );

            System.out.println(response);

        });


    }

    @Test
    void getAllRoomSuccess() throws Exception {

        Room room = new Room();
        for (int i = 1; i < 10; i++) {
            room.setId("room-keluarga" + UUID.randomUUID().toString());
            room.setUser(user);
            room.setTitle("Room " + i);
            room.setAvailability(AvailabilityRoom.available);
            String kamarMandi = "Kamar mandi dalam";
            if (i % 2 == 0) {
                kamarMandi = "Kamar mandi Luar";
            }
            room.setDetails(kamarMandi);
            Long price = 8000000L;
            if (i % 2 == 0) {
                price = 800000L;
            }
            room.setPrice(price);
            room.setCreatedAt(LocalDateTime.now());
            roomRepository.save(room);
        }

        mockMvc.perform(get("/kos-agus/rooms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        "@anas_token_room"
                                )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<RoomResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());

            assertEquals(
                    "200 OK",
                    response.getStatus()
            );

            assertInstanceOf(
                    List.class,
                    response.getData()
            );

            System.out.println(response);

        });
    }

    @Test
    void getRoomNotFound() throws Exception {

        mockMvc.perform(get("/kos-agus/rooms/paijo-kos-1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        "@anas_token_room"
                                )
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());

            assertEquals(
                    "404 NOT_FOUND",
                    response.getStatus()
            );

            System.out.println(response);

        });
    }

    @Test
    void getRoomSuccess() throws Exception {

        Room room = new Room();
        for (int i = 1; i < 10; i++) {
            room.setId("room-keluarga" + i);
            room.setUser(user);
            room.setTitle("Room " + i);
            room.setAvailability(AvailabilityRoom.available);
            String kamarMandi = "Kamar mandi dalam";
            if (i % 2 == 0) {
                kamarMandi = "Kamar mandi Luar";
            }
            room.setDetails(kamarMandi);
            Long price = 8000000L;
            if (i % 2 == 0) {
                price = 800000L;
            }
            room.setPrice(price);
            room.setCreatedAt(LocalDateTime.now());
            roomRepository.save(room);
        }

        List<Room> rooms = roomRepository.findAll();
        Room thirdRoom = rooms.get(2);

        mockMvc.perform(get("/kos-agus/rooms/" + thirdRoom.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        "@anas_token_room"
                                )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<RoomResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());

            assertEquals(
                    "200 OK",
                    response.getStatus()
            );

            System.out.println(response);

        });
    }

    @Test
    void updateRoomNotFound() throws Exception {

        UpdateRoomRequest request = new UpdateRoomRequest();
        request.setTitle("dummy");
        request.setAvailability(AvailabilityRoom.available);
        request.setDetails("dummy");
        request.setPrice(1000L);

        mockMvc.perform(
                        patch("/kos-agus/rooms/update/mulyadi")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        "@anas_token_room"
                                )
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(
                        status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );

                    assertNotNull(response.getErrors());
                    assertEquals(
                            "404 NOT_FOUND",
                            response.getStatus()
                    );
                    System.out.println(response);
                });
    }


    @Test
    void updateRoomSuccess() throws Exception {

        Room room = new Room();
        room.setId("room-anas" + UUID.randomUUID().toString());
        room.setUser(user);
        room.setTitle("Room " + UUID.randomUUID().toString());
        room.setAvailability(AvailabilityRoom.available);
        room.setDetails("kamar mandi dalam");
        room.setPrice(1000000L);
        room.setCreatedAt(LocalDateTime.now());
        roomRepository.save(room);

        UpdateRoomRequest request = new UpdateRoomRequest();
        request.setTitle("Room bagyo");
        request.setAvailability(AvailabilityRoom.booked);
        request.setDetails("kamar mandi luar");
        request.setPrice(1000000L);

        mockMvc.perform(patch("/kos-agus/rooms/update/" + room.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header(
                                        "API-TOKEN-KOS-AGUS-APIK",
                                        "@anas_token_room"
                                )
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<RoomResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());

            assertEquals(
                    "200 OK",
                    response.getStatus()
            );

            Room roomUpdated = roomRepository.findById(room.getId()).orElseThrow(() -> new RuntimeException("room not found"));

            assertEquals(
                    "Room bagyo",
                    roomUpdated.getTitle()
            );

            System.out.println(response);

        });
    }

}