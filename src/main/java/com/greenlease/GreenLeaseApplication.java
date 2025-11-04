package com.greenlease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GreenLeaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenLeaseApplication.class, args);
        System.out.println("=================================================");
        System.out.println("GreenLease Eco-Rating System Started!");
        System.out.println("Access the application at: http://localhost:8080/greenlease");
        System.out.println("=================================================");
    }
}