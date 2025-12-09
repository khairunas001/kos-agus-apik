package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.ResponseFactory;
import com.anas.kos_agus_apik.model.request.CreateRoomRequest;
import com.anas.kos_agus_apik.model.response.RoomResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponseSuccess;
import com.anas.kos_agus_apik.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping(
            path = "/kos-agus/rooms/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<WebResponseSuccess<RoomResponse>> createRoom(User user, @RequestBody CreateRoomRequest request) {

        RoomResponse response = roomService.createRoom(
                user,
                request
        );

        return ResponseFactory.created(response);
    }

    @DeleteMapping(
            path = "/kos-agus/rooms/delete/{room_id}"
    )
    private ResponseEntity<WebResponseSuccess<String>> deleteRoom(User user, @PathVariable("room_id") String roomID) {

        roomService.deleteRoom(
                user,
                roomID
        );

        return ResponseFactory.ok("room already deleted");

    }

    @GetMapping(
            path = "/kos-agus/rooms/{roomId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<WebResponseSuccess<RoomResponse>> getRoom(User user, @PathVariable String roomId) {

        RoomResponse response = roomService.getRoom(
                user,
                roomId
        );

        return ResponseFactory.ok(response);
    }

    @GetMapping(
            path = "/kos-agus/rooms",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<WebResponseSuccess<List<RoomResponse>>> getAllRooms(User user) {

        List<RoomResponse> response = roomService.getAllRoom(user);

        return ResponseFactory.ok(response);
    }

}
