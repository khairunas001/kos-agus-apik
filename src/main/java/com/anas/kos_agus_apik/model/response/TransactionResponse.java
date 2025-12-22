package com.anas.kos_agus_apik.model.response;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({
        "id",
        "user_id",
        "room",
        "amount",
        "period",
        "payment_date",
        "payment_status",
        "payment_method"
})
public class TransactionResponse {

    private String id;

    @JsonProperty("user_id")
    private String userId;

    private RoomResponse room;

    private Long amount;

    private LocalDate period;

    @JsonProperty("payment_date")
    private LocalDateTime paymentDate;

    @JsonProperty("payment_status")
    private PaymentStatus paymentStatus;

    @JsonProperty("payment_method")
    private String paymentMethod;
}
