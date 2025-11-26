package com.anas.kos_agus_apik.model.web_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WebResponseSuccess<T>{

    private String status;

    private T data;
}
