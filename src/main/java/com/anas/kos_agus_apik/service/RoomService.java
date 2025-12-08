package com.anas.kos_agus_apik.service;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.request.CreateRoomRequest;
import com.anas.kos_agus_apik.model.response.RoomResponse;
import com.anas.kos_agus_apik.repository.RoomRepository;
import com.anas.kos_agus_apik.repository.TokenRepository;
import com.anas.kos_agus_apik.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private RoomRepository roomRepository;


    @Transactional
    public RoomResponse createRoom(User user, CreateRoomRequest request) {

        // Validasi request
        validationService.validate(request);

        // Cek apakah user adalah admin
        if (user.getRoles() != Role.admin) {
            throw new RuntimeException("only admin can create room");
        }
        Room room = new Room();

        room.setId(UUID.randomUUID().toString());
        room.setUser(user);
        room.setTitle(request.getTitle());
        room.setAvailability(request.getAvailability());
        room.setDetails(request.getDetails());
        room.setPrice(request.getPrice());
        room.setCreatedAt(LocalDateTime.now());

        roomRepository.save(room);

        return RoomResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .availability(room.getAvailability())
                .details(room.getDetails())
                .price(room.getPrice())
                .build();
    }

    @Transactional
    public void deleteRoom(User user, String roomId) {

        // Cek apakah user adalah admin
        if (user.getRoles() != Role.admin) {
            throw new RuntimeException("only admin can create room");
        }

        // cek dulu apakah data masih ada sebelum delete
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("room not found"));

        roomRepository.delete(room);
    }

    @Transactional
    public List<RoomResponse> getAllRoom(User user) {

        // cek apakah user adalah admin
        if (user.getRoles() != Role.admin) {
            throw new RuntimeException("only admin can create room");
        }

        // ambil semua data pada table room
        List<Room> rooms = roomRepository.findAll();


        return rooms.stream()
                .map(room -> RoomResponse.builder()
                        .id(room.getId())
                        .title(room.getTitle())
                        .availability(room.getAvailability())
                        .details(room.getDetails())
                        .price(room.getPrice())
                        .build()
                ).toList();
    }
}
