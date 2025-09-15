package com.anas.kos_agus_apik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.TimeZone;

@SpringBootApplication
public class KosAgusApik {

    public static void main(String[] args) {

		// Set JVM default timezone ke UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        SpringApplication.run(
                KosAgusApik.class,
                args
        );

    }

}
