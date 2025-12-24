package com.anas.kos_agus_apik.model.request;

import com.anas.kos_agus_apik.entity.enum_class.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdatePaymentConfirmationRequest {

    @JsonProperty("payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
