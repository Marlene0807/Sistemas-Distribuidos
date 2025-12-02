package com.ejemplo.carrera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarreraTortugasApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarreraTortugasApplication.class, args);
        System.out.println("🐢 Servidor de la carrera iniciado en http://localhost:8080");
    }
}
