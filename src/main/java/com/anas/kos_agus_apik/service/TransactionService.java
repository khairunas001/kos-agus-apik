package com.anas.kos_agus_apik.service;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.Transaction;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.AvailabilityRoom;
import com.anas.kos_agus_apik.entity.enum_class.PaymentStatus;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found")
        );

        //validasri room
        if (room.getAvailability() == AvailabilityRoom.booked) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Room already booked"
            );
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUser(user);
        transaction.setRoom(room);

        //1. Ambil harga kamar
        //room.price = 1.000.000 / bulan("duration_month")
        // ️2. Hitung amount
        // amount = room.price * duration_month
        transaction.setAmount(request.getDurationMonth() * room.getPrice());

        // 3. Hitung period
        // startDate = now()
        // endDate = now().plusMonths(duration_month)
        transaction.setPeriod(LocalDate.now().plusMonths(request.getDurationMonth()));
        transaction.setPaymentDate(LocalDateTime.now());
        transaction.setPaymentStatus(PaymentStatus.pending);
        transaction.setPaymentMethod(request.getPaymentMethod());
        transactionRepository.save(transaction);

        // 4.Auto update room
        // room.status = "ON_BOOKED"
        room.setAvailability(AvailabilityRoom.booked);

        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .roomId(transaction.getRoom().getId())
                .amount(transaction.getAmount())
                .period(transaction.getPeriod())
                .paymentDate(transaction.getPaymentDate())
                .paymentStatus(transaction.getPaymentStatus())
                .paymentMethod(transaction.getPaymentMethod())
                .build();
    }


}
