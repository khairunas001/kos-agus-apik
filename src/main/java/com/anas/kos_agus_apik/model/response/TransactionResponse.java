package com.anas.kos_agus_apik.model.response;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private String id;

    private String userId;

    private String roomId;

    private Long amount;

    private LocalDate period;

    private LocalDateTime paymentDate;

    private PaymentStatus paymentStatus;

    private String paymentMethod;
}
