package com.anas.kos_agus_apik.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WebResponse <T>{

    private String status;

    private T data;

    private String errors;

}
