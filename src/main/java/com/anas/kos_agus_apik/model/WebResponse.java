package com.anas.kos_agus_apik.model;

import lombok.Data;

public class WebResponse <T>{

    private String status;

    private T data;

    private String errors;

}
