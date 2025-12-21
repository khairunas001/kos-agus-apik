package com.anas.kos_agus_apik.model.request;

import com.anas.kos_agus_apik.entity.Room;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.entity.enum_class.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateTransactionsRequest {

    @NotBlank
    @JsonProperty("room_id")
    private String roomId;

    @NotNull
    @JsonProperty("duration_month")
    private Integer durationMonth;

    @NotBlank
    @JsonProperty("payment_method")
    private String paymentMethod;
}

