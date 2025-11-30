package com.anas.kos_agus_apik;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
public class KosAgusApik {

    public static void main(String[] args) {
        // Set JVM default timezone ke UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        SpringApplication.run(KosAgusApik.class, args);
    }
}
