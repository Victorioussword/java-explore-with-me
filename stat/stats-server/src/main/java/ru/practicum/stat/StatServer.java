package ru.practicum.stat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatServer {
    public static void main(String[] args) {
        System.setProperty("server.port", "8080");
        SpringApplication.run(StatServer.class, args);
    }
}
