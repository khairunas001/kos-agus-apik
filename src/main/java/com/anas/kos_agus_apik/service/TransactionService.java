package com.anas.kos_agus_apik.service;

import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.request.CreateTransactionsRequest;
import com.anas.kos_agus_apik.model.response.TransactionResponse;
import com.anas.kos_agus_apik.repository.RoomRepository;
import com.anas.kos_agus_apik.repository.TransactionRepository;
import com.anas.kos_agus_apik.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        validationService.validate(request);

        //1. Ambil harga kamar
        //room.price = 1.000.000 / bulan

        // ️2. Hitung amount
        // amount = room.price * duration_month

        // 3. Hitung period
        // startDate = now()
        // endDate = now().plusMonths(duration_month)

        // 4.Auto update room
        // room.status = "ON_BOOKED"

        return TransactionResponse.builder().build();
    }


}
