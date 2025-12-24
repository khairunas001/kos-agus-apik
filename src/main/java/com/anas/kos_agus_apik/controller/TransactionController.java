package com.anas.kos_agus_apik.controller;

import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.ResponseFactory;
import com.anas.kos_agus_apik.model.request.CreateTransactionsRequest;
import com.anas.kos_agus_apik.model.request.UpdatePaymentConfirmationRequest;
import com.anas.kos_agus_apik.model.response.TransactionResponse;
import com.anas.kos_agus_apik.model.web_response.WebResponseSuccess;
import com.anas.kos_agus_apik.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping(
            path = "/kos-agus/transactions/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    private ResponseEntity<WebResponseSuccess<TransactionResponse>> createRoom(User user, @RequestBody CreateTransactionsRequest request) {

        TransactionResponse transactionResponse = transactionService.createTransaction(user, request);

        return ResponseFactory.created(transactionResponse);
    }

    @PatchMapping(
            path = "/kos-agus/transactions/update/{transactions_id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    private ResponseEntity<WebResponseSuccess<TransactionResponse>> updatePaymentConfirmation(User user, @PathVariable("transactions_id") String transactionsId, @RequestBody UpdatePaymentConfirmationRequest request) {

        TransactionResponse transactionResponse = transactionService.updateTransactionConfirmation(user, request, transactionsId);

        return ResponseFactory.ok(transactionResponse);
    }
}
