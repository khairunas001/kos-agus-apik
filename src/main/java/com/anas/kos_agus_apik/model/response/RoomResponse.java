package com.anas.kos_agus_apik.model.response;

import com.anas.kos_agus_apik.entity.enum_class.AvailabilityRoom;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RoomResponse {

    @NotBlank
    private String id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AvailabilityRoom availability;

    @NotBlank
    @Size(max = 100)
    private String details;

    @NotNull
    private Long price;


}
