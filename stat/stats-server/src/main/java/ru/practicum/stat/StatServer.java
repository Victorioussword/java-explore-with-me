package ru.practicum.stat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class StatServer {
    public static void main(String[] args) {
        System.setProperty("server.port", "9090");
        SpringApplication.run(StatServer.class, args);
    }
}