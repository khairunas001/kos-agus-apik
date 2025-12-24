package com.anas.kos_agus_apik.service;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.Transaction;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.AvailabilityRoom;
import com.anas.kos_agus_apik.entity.enum_class.PaymentStatus;
import com.anas.kos_agus_apik.entity.enum_class.Role;
import com.anas.kos_agus_apik.model.request.CreateTransactionsRequest;
import com.anas.kos_agus_apik.model.request.UpdatePaymentConfirmationRequest;
import com.anas.kos_agus_apik.model.response.RoomResponse;
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

        // validasi room apakah ada
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found")
        );

        //validasri room jika sudah di booking atau tidak
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

        // hitung harga amount dengan mengalikan
        transaction.setAmount(request.getDurationMonth() * room.getPrice());

        // hitung periode
        transaction.setPeriod(LocalDate.now().plusMonths(request.getDurationMonth()));
        transaction.setPaymentDate(LocalDateTime.now());
        transaction.setPaymentStatus(PaymentStatus.pending);
        transaction.setPaymentMethod(request.getPaymentMethod());
        transactionRepository.save(transaction);

        // update room availibility menjadi di booked
        room.setAvailability(AvailabilityRoom.booked);

        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
//                .roomId(transaction.getRoom().getId())
                .room(
                        RoomResponse.builder()
                                .id(room.getId())          // 🔥 key = roomId
                                .title(room.getTitle())
                                .availability(room.getAvailability())
                                .details(room.getDetails())
                                .price(room.getPrice())
                                .build()
                )
                .amount(transaction.getAmount())
                .period(transaction.getPeriod())
                .paymentDate(transaction.getPaymentDate())
                .paymentStatus(transaction.getPaymentStatus())
                .paymentMethod(transaction.getPaymentMethod())
                .build();
    }

    @Transactional
    public TransactionResponse updateTransactionConfirmation(User user, UpdatePaymentConfirmationRequest request , String transactionsId) {

        validationService.validate(request);

        // validasi role harus admin
        if (user.getRoles() != Role.admin) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "only admin can confirm transactions"
            );
        }

        // cek room berdasarkan roomId
        Transaction transaction = transactionRepository.findById(transactionsId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "transactions not found")
        );

        // cek apakah sudah terbayarkan
        if (transaction.getPaymentStatus() == PaymentStatus.paid) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "transaction already paid"
            );
        }

        // cek apakah transaksi sudah dibatalkan
        if (transaction.getPaymentStatus() == PaymentStatus.cancelled) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "transaction already cancelled"
            );
        }

        // masih bingung
//        if (request.getPaymentStatus() != PaymentStatus.paid) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "admin can only confirm payment to PAID"
//            );
//        }

        transaction.setPaymentStatus(request.getPaymentStatus());
//        transactionRepository.save(transaction);

        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .room(
                        RoomResponse.builder()
                                .id(transaction.getRoom().getId())
                                .title(transaction.getRoom().getTitle())
                                .availability(transaction.getRoom().getAvailability())
                                .details(transaction.getRoom().getDetails())
                                .price(transaction.getRoom().getPrice())
                                .build()
                )
                .amount(transaction.getAmount())
                .period(transaction.getPeriod())
                .paymentDate(transaction.getPaymentDate())
                .paymentStatus(transaction.getPaymentStatus())
                .paymentMethod(transaction.getPaymentMethod())
                .build();
    }


}
