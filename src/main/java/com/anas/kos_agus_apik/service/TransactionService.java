package com.anas.kos_agus_apik.service;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.Transaction;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.request.CreateTransactionsRequest;
import com.anas.kos_agus_apik.model.response.TransactionResponse;
import com.anas.kos_agus_apik.repository.RoomRepository;
import com.anas.kos_agus_apik.repository.TransactionRepository;
import com.anas.kos_agus_apik.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TransactionResponse createTransaction(User user, CreateTransactionsRequest request) {

        // validasi request
        validationService.validate(request);

        // validasi role harus customers
        if (user.getRoles() != Role.customers) {
            throw new RuntimeException("only customers who can makes transactions");
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUser(user);

        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found")
        );
        transaction.setRoom(room);

        transaction.setAmount(request.getDurationMonth() * room.getPrice());

        //1. Ambil harga kamar
        //room.price = 1.000.000 / bulan

        // ️2. Hitung amount
        // amount = room.price * duration_month

        // 3. Hitung period
        // startDate = now()
        // endDate = now().plusMonths(duration_month)

        // 4.Auto update room
        // room.status = "ON_BOOKED"

        return TransactionResponse.builder()

                .build();
    }


}
