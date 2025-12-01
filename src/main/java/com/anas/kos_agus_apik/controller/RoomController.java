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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
